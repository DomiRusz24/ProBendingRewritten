package com.probending.probending.core.arena.prearena;

import com.probending.probending.PBListener;
import com.probending.probending.core.annotations.Language;
import com.probending.probending.core.arena.Arena;
import com.probending.probending.core.enums.TeamTag;
import com.probending.probending.core.interfaces.PlaceholderObject;
import com.probending.probending.core.players.MenuPlayer;
import com.probending.probending.util.PerTick;
import com.probending.probending.util.UtilMethods;
import me.clip.placeholderapi.PlaceholderAPI;

public class PreArena implements PlaceholderObject, PerTick {

    private final Arena arena;

    private final ArenaGetterRegion blueRegion;

    private final ArenaGetterRegion redRegion;

    private boolean enabled = true;

    private State state = State.OFFLINE;

    public PreArena(Arena arena) {
        this.arena = arena;
        this.blueRegion = arena.getLocationConfig().getRegion("ArenaGetter.Blue", arena.getName() + "_blue", TeamTag.BLUE, this);
        this.redRegion = arena.getLocationConfig().getRegion("ArenaGetter.Red", arena.getName() + "_red", TeamTag.RED, this);
        PBListener.hookInListener(this);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        blueRegion.setEnabled(enabled);
        redRegion.setEnabled(enabled);
    }

    public boolean isReady() {
        if (state == State.OFFLINE) {
            if (blueRegion == null) return false;
            if (redRegion == null) return false;
            if (blueRegion.getCenter() == null || blueRegion.getRegionCenter() == null) {
                return false;
            }
            if (redRegion.getCenter() == null || redRegion.getRegionCenter() == null) {
                return false;
            }
            if (redRegion.getMin() == null) {
                return false;
            }
            if (blueRegion.getMin() == null) {
                return false;
            }
            if (arena.inGame()) {
                state = State.TAKEN;
            } else {
                state = State.WAITING;
            }
            getArena().getJoinSign().update();
        }
        return true;
    }

    @Language("Arena.PreArena.PlayerVoteStart")
    public static String LANG_VOTE = "%player_name% has voted to force start!";

    public void onVoteStart(MenuPlayer player) {
        String message = PlaceholderAPI.setPlaceholders(player.getPlayer(), LANG_VOTE);
        blueRegion.getTeam().sendMessage(message);
        redRegion.getTeam().sendMessage(message);
        updateForce();
    }

    public void onForceLeave(MenuPlayer player) {
        getArena().getJoinSign().update();
    }

    public void onLeave(MenuPlayer player) {
        getArena().getTeam(player.getTag()).removePlayer(player.getPlayer());
        if (state == State.COUNTDOWN) {
            state = State.WAITING;
            force = false;
            for (MenuPlayer mPlayer : blueRegion.getTeam().getPlayers()) {
                mPlayer.setVoteSkip(false);
            }

            for (MenuPlayer mPlayer : redRegion.getTeam().getPlayers()) {
                mPlayer.setVoteSkip(false);
            }
            countdown = 10;
        }
        getArena().getJoinSign().update();
    }

    public void onJoin(MenuPlayer player) {
        if (state == State.WAITING && isFull()) {
            state = State.COUNTDOWN;
        }
        getArena().getJoinSign().update();
        player.getScoreBoard().update();
    }

    public boolean isFull() {
        return blueRegion.getTeam().isFull() && redRegion.getTeam().isFull();
    }

    public int getCurrentSize() {
        return blueRegion.getTeam().getCurrentSize() + redRegion.getTeam().getCurrentSize();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public ArenaGetterRegion getBlueRegion() {
        return blueRegion;
    }

    public ArenaGetterRegion getRedRegion() {
        return redRegion;
    }

    public ArenaGetterRegion getRegion(TeamTag tag) {
        return tag == TeamTag.BLUE ? blueRegion : redRegion;
    }

    public Arena getArena() {
        return arena;
    }


    private boolean force = false;

    private int countdown = 10;

    public void updateForce() {
        int votes = 0;
        for (MenuPlayer player : blueRegion.getTeam().getPlayers()) {
            if (player.getVoteSkip()) {
                votes++;
            }
        }

        for (MenuPlayer player : redRegion.getTeam().getPlayers()) {
            if (player.getVoteSkip()) {
                votes++;
            }
        }

        if (votes == getCurrentSize() && votes > 1 && blueRegion.getTeam().getCurrentSize() > 0 && redRegion.getTeam().getCurrentSize() > 0) {
            if (votes >= getArena().getArenaConfig().getMinimalPlayerAmount()) {
                setForce(true);
            }
        }
    }

    public void setForce(boolean force) {
        if (state == State.WAITING && force) {
            this.force = true;
            setState(State.COUNTDOWN);
        } else if (state == State.COUNTDOWN && !force) {
            this.force = false;
            setState(State.WAITING);
        }
    }

    public void setState(State state) {
        if (state != State.OFFLINE) {
            this.state = state;
            getArena().getJoinSign().update();
        }
    }

    int tick = 0;

    @Override
    public void onTick() {
        if (state == State.COUNTDOWN || state == State.WAITING) {
            tick++;
            if (tick % 20 == 0) {
                if ((state == State.COUNTDOWN || force) && enabled) {
                    if (countdown == 0) {
                        force = false;
                        countdown = 10;
                        getArena().start(true);
                        if (!getArena().inGame()) {
                            for (MenuPlayer mPlayer : blueRegion.getTeam().getPlayers()) {
                                mPlayer.setVoteSkip(false);
                            }

                            for (MenuPlayer mPlayer : redRegion.getTeam().getPlayers()) {
                                mPlayer.setVoteSkip(false);
                            }
                            setForce(false);
                        }
                    } else {
                        blueRegion.getTeam().sendTitle(UtilMethods.getNumberPrefix(countdown) + countdown, "", 5, 10, 5);
                        redRegion.getTeam().sendTitle(UtilMethods.getNumberPrefix(countdown) + countdown, "", 5, 10, 5);
                        countdown--;
                    }
                }
            }
        }
    }

    public State getState() {
        return state;
    }

    @Language("PreArena.State.NOT_COMPLETE")
    public static String LANG_NOT_COMPLETE = "&cNot complete!";

    @Language("PreArena.State.ERROR")
    public static String LANG_ERROR = "&4This pre-arena has a malfunction.";

    @Language("PreArena.State.EMPTY")
    public static String LANG_READY = "&2Ready to play.";

    @Language("PreArena.State.TAKEN")
    public static String LANG_TAKEN = "&cTaken!";

    public enum State {
        OFFLINE,
        TAKEN,
        WAITING,
        COUNTDOWN;

        public String toString() {
            switch (this) {
                case OFFLINE:
                    return (LANG_NOT_COMPLETE);
                case WAITING:
                case COUNTDOWN:
                    return (LANG_READY);
                case TAKEN:
                    return LANG_TAKEN;
                default:
                    return (LANG_ERROR);
            }
        }
    }

    @Override
    public String onPlaceholderRequest(String param) {
        return arena.onPlaceholderRequest(param);
    }

    @Override
    public String placeHolderPrefix() {
        return "arena";
    }

}
