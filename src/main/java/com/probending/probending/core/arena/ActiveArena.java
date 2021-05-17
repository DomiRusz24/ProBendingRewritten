package com.probending.probending.core.arena;

import com.probending.probending.ProBending;
import com.probending.probending.api.enums.KnockOutCause;
import com.probending.probending.api.events.*;
import com.probending.probending.config.CommandConfig;
import com.probending.probending.core.annotations.Language;
import com.probending.probending.core.arena.prearena.PreArena;
import com.probending.probending.core.arena.states.*;
import com.probending.probending.core.interfaces.PlaceholderObject;
import com.probending.probending.core.players.AbstractPlayer;
import com.probending.probending.core.players.ActivePlayer;
import com.probending.probending.core.enums.ArenaState;
import com.probending.probending.core.enums.GameType;
import com.probending.probending.core.enums.Ring;
import com.probending.probending.core.enums.TeamTag;
import com.probending.probending.core.players.PBPlayer;
import com.probending.probending.core.players.SpectatorPlayer;
import com.probending.probending.core.team.ActiveTeam;
import com.probending.probending.core.team.Team;
import com.probending.probending.util.UtilMethods;
import com.projectkorra.projectkorra.BendingPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class ActiveArena implements PlaceholderObject {

    // --- LANGUAGE ---

    // -------------------------------- //

    private final Arena arena;

    private AbstractArenaHandler handler;
    private AbstractArenaHandler cancelHandler;

    private ArenaState state;
    private GameType gameType;

    private final ActiveTeam blueTeam;
    private final ActiveTeam redTeam;

    private final HashSet<SpectatorPlayer> SPECTATORS;

    private int arenaTick;

    public ActiveArena(Arena arena) {
        this.arena = arena;
        this.state = ArenaState.NONE;
        this.cancelHandler = new ArenaHandler(this);
        handler = new ArenaHandler(this);
        this.blueTeam = new ActiveTeam(this, TeamTag.BLUE);
        this.redTeam = new ActiveTeam(this, TeamTag.RED);
        this.SPECTATORS = new HashSet<>();
        arenaTick = 0;
    }

    // ----------

    public void start(Team blue, Team red, GameType type) {
        arenaTick = 0;
        this.gameType = type;
        blueTeam.purgePlayers();
        redTeam.purgePlayers();
        for (Player p : blue.getUnwrappedPlayers()) {
            ActivePlayer ap = new ActivePlayer(p, TeamTag.BLUE, this, ProBending.projectKorraM.getFirstElement(BendingPlayer.getBendingPlayer(p)));
            blueTeam.addPlayer(ap);
            CommandConfig.Commands.ArenaStartPlayer.run(p);
        }
        for (Player p : red.getUnwrappedPlayers()) {
            ActivePlayer ap = new ActivePlayer(p, TeamTag.RED, this, ProBending.projectKorraM.getFirstElement(BendingPlayer.getBendingPlayer(p)));
            redTeam.addPlayer(ap);
            CommandConfig.Commands.ArenaStartPlayer.run(p);
        }
        round = 1;
        state = ArenaState.MID_ROUND;
        getArena().setState(Arena.State.TAKEN);
        handler = new StartingState(this);
        ProBending.arenaM.ACTIVE_ARENAS.add(this);
        PBArenaStartEvent event = new PBArenaStartEvent(this.getArena());
        Bukkit.getPluginManager().callEvent(event);
        CommandConfig.Commands.ArenaStartSingle.run(this.getArena());
    }

    public void forceStop() {
        state = ArenaState.HALT;
    }

    public void forceUnstableStop() {
        forceUnstableStop(null);
    }

    public void forceUnstableStop(TeamTag winningTeam) {
        PBArenaStopEvent event = new PBArenaStopEvent(this, state, winningTeam);
        Bukkit.getPluginManager().callEvent(event);
        stop();
    }


    private void stop() {
        for (ActivePlayer player : getPlayers(true)) {
            CommandConfig.Commands.ArenaStopPlayer.run(player);
        }
        CommandConfig.Commands.ArenaStopSingle.run(this.getArena());
        redTeam.purgePlayers();
        blueTeam.purgePlayers();
        getSpectators().forEach(SpectatorPlayer::unregister);
        state = ArenaState.NONE;
        getArena().setState(Arena.State.READY);
        arenaTick = 0;
        ProBending.arenaM.ACTIVE_ARENAS.remove(this);
        getArena().getPreArena().setEnabled(true);
        getArena().getPreArena().setState(PreArena.State.WAITING);
        getArena().getJoinSign().update();
    }

    // ---------- ROUNDS ----------

    private int round = 1;

    public int getRound() {
        return round;
    }

    public void raiseRound() {
        round++;
    }

    public TeamTag getWinningTeamByRound() {
        int blueMax = 0;
        int redMax = 0;
        for (ActivePlayer player : getTeam(TeamTag.BLUE).getPlayers(false)) {
            int i = player.getRing().offset(TeamTag.BLUE, Ring.BLUE_FIRST);
            blueMax = Math.max(blueMax, i);
        }
        for (ActivePlayer player : getTeam(TeamTag.RED).getPlayers(false)) {
            int i = player.getRing().offset(TeamTag.RED, Ring.RED_FIRST);
            redMax = Math.max(redMax, i);
        }
        if (blueMax == redMax) {
            int blueCount = getTeam(TeamTag.BLUE).getPlayers(false).size();
            int redCount = getTeam(TeamTag.BLUE).getPlayers(false).size();
            if (blueCount > redCount) {
                return TeamTag.BLUE;
            } else if (blueCount < redCount) {
                return TeamTag.RED;
            } else {
                return null;
            }
        } else {
            if (blueMax > redMax) {
                return TeamTag.RED;
            } else {
                return TeamTag.BLUE;
            }
        }
    }

    public TeamTag getWinningTeamByGame() {
        int redPoints = getTeam(TeamTag.RED).getPoints();
        int bluePoints = getTeam(TeamTag.BLUE).getPoints();
        if (redPoints > bluePoints) {
            return TeamTag.RED;
        } else if (redPoints < bluePoints) {
            return TeamTag.BLUE;
        } else {
            return null;
        }
    }


    // ----------------------------

    public ArenaState getState() {
        return state;
    }

    public GameType getGameType() {
        return gameType;
    }

    public void setState(ArenaState state) {
        this.state = state;
    }

    public void setHandler(AbstractArenaHandler handler) {
        this.handler = handler;
    }

    public AbstractArenaHandler getCancelHandler() {
        return cancelHandler;
    }

    public void callDamageEvent(PBPlayerDamagePBPlayerEvent event) {
        handler.onPBPlayerDamagePBPlayer(event);
        if (!event.isCancelled()) {
            event.getEntity().raiseTiredness((int) event.getDamage() * ProBending.configM.getConfig().getHpToTirednessRatio());
            event.getEntity().setHasBeenHit(true);
        }
    }

    // ----------

    private void playerKnockOut(ActivePlayer player, KnockOutCause cause) {
        PBPlayerKnockOutEvent event = new PBPlayerKnockOutEvent(this, this.getState(), player, cause);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled() && cause != KnockOutCause.LEFT) {
            cancelHandler.onPlayerKnockOut(event);
        } else {
            handler.onPlayerKnockOut(event);
            if (cause == KnockOutCause.LEFT) removePlayer(player, false);
        }
    }

    private void playerUpdateStage(ActivePlayer player, Ring ring, int offset) {
        PBPlayerUpdateStageEvent event = new PBPlayerUpdateStageEvent(this, this.getState(), player, ring, offset);
        Bukkit.getPluginManager().callEvent(event);
        handler.onPlayerUpdateStage(event);
    }

    private void onSecond() {
        getPlayers(false).forEach(ActivePlayer::update);
        handler.onUpdate();
    }

    private void onWipeOut(TeamTag team) {
        handler.onWipeOut(team);
    }

    private void onGap(TeamTag tag, int gap, Ring furthest) {
        handler.onGap(tag, gap, furthest);
    }


    // --------------------


    private TeamTag teamToCheck = null;

    public void setTeamToCheck(TeamTag teamToCheck) {
        this.teamToCheck = teamToCheck;
    }

    // ----- UPDATE ----- \\

    private TeamTag winningTeam = null;

    public void update() {
        arenaTick++;
        teamToCheck = null;
        if (state == ArenaState.HALT) {
            state = ArenaState.STOP;
            handler = new EndingState(this, getWinningTeamByGame());
        }
        if (handler.needStart()) {
            handler.start();
        }
        playerLoop:
        {
            for (ActivePlayer player : getPlayers(true)) {
                if (player.getState() == ActivePlayer.State.LEFT) {
                    if (player.isSpectating()) {
                        removePlayer(player, false);
                    } else {
                        playerKnockOut(player, KnockOutCause.LEFT);
                    }
                    break;
                }
                if (player.getState() == ActivePlayer.State.SPECTATING) continue;

                if (player.getState() == ActivePlayer.State.DEAD) {
                    playerKnockOut(player, KnockOutCause.ZERO_HP);
                    break;
                }

                if (player.hasBeenHit()) {
                    Vector v = player.getPlayer().getVelocity();
                    double multi = 1 + (player.getTiredness() * 0.01);
                    v.setX(v.getX() * multi);
                    v.setZ(v.getZ() * multi);
                    player.getPlayer().setVelocity(v);
                    player.setHasBeenHit(false);
                }

                Ring current = player.getCurrentRing();

                switch (current) {
                    case OFF_WOOL:
                        playerKnockOut(player, KnockOutCause.LEFT);
                        break playerLoop;
                    case BLUE_BACK:
                        if (player.getTeamTag() == TeamTag.BLUE) {
                            playerKnockOut(player, KnockOutCause.FALL_OFF_ARENA);
                            break playerLoop;
                        } else {
                            player.teleportTo(player.getRing());
                            current = player.getRing();
                        }
                        break;
                    case RED_BACK:
                        if (player.getTeamTag() == TeamTag.RED) {
                            playerKnockOut(player, KnockOutCause.FALL_OFF_ARENA);
                            break playerLoop;
                        } else {
                            player.teleportTo(player.getRing());
                            current = player.getRing();
                        }
                        break;
                    case LINE:
                        current = player.getRing();
                        break;
                    case OUTSIDE:
                        player.teleportTo(player.getRing());
                        current = player.getRing();
                        break;
                }

                current = handler.onPlayerPreProcess(player, current);

                int offset = player.getRing().offset(player.getTeamTag(), current);
                if (offset != 0) {
                    playerUpdateStage(player, current, offset);
                    break playerLoop;
                }
            }
        }
        if (teamToCheck != null) {
            if (getTeam(teamToCheck).isWipedOut()) {
                if (arenaTick % 20 == 0) arenaTick--;
                onWipeOut(teamToCheck);
            } else {
                int gap = getTeam(teamToCheck).getGap();
                if (gap > 0) {
                    if (arenaTick % 20 == 0) arenaTick--;
                    onGap(teamToCheck, gap, getTeam(teamToCheck).getFurthestRing());
                }
            }
        } else if (getGameTime() % 20 == 0) onSecond();
    }

    public int getGameTime() {
        return arenaTick;
    }

    // ------------------- \\

    public Arena getArena() {
        return arena;
    }

    public ActiveTeam getTeam(TeamTag tag) {
        return tag == TeamTag.BLUE ? blueTeam : redTeam;
    }

    public void teleportToStartingLocations()  {
        for (TeamTag tag : TeamTag.values()) {
            int i = 0;
            Ring ring = tag == TeamTag.BLUE ? Ring.BLUE_FIRST : Ring.RED_FIRST;
            for (ActivePlayer p : getTeam(tag).getPlayers(false)) {
                p.setRing(ring);
                p.getPlayer().teleport(getArena().getStartingLocations(tag, i));
                i++;
            }
        }
    }

    // -----------

    public List<ActivePlayer> getPlayers(boolean spectators) {
        List<ActivePlayer> list = getTeam(TeamTag.BLUE).getPlayers(spectators);
        list.addAll(getTeam(TeamTag.RED).getPlayers(spectators));
        return list;
    }

    public Player[] getBukkitPlayers(boolean spectators) {
        List<ActivePlayer> players = getPlayers(spectators);
        Player[] arr = new Player[players.size()];
        for (int i = 0; i < players.size(); i++)
            arr[i] = players.get(i).getPlayer();
        return arr;
    }

    public List<SpectatorPlayer> getSpectators() {
        return new ArrayList<>(SPECTATORS);
    }

    public void removePlayer(Player player, boolean kill) {
        UtilMethods.freezePlayer(player, false);
        ActivePlayer p = ProBending.playerM.getActivePlayer(player);
        if (p != null && getPlayers(true).contains(p)) {
            removePlayer(player, kill);
        } else if (SPECTATORS.stream().filter(spec -> spec.getPlayer().equals(player)).toArray().length == 1) {
            removeSpectator(SPECTATORS.stream().filter(spec -> spec.getPlayer().equals(player)).collect(Collectors.toList()).get(0));
        }
    }

    @Language("ActiveArena.ActivePlayer.KnockOut1")
    public static String LANG_KNOCK_OUT = "Player %player% has been knocked out by %killer%!";

    @Language("ActiveArena.ActivePlayer.KnockOut2")
    public static String LANG_KNOCK_OUT_NO_KILLER = "Player %player% has been knocked out!";

    @Language("ActiveArena.ActivePlayer.LeaveGame")
    public static String LANG_LEAVE_GAME = "Player %player% has left the game!";

    public void removePlayer(ActivePlayer player, boolean kill) {
        if (getPlayers(true).contains(player)) {
            UtilMethods.freezePlayer(player.getPlayer(), false);
            if (player.isSpectating()) {
                player.getPlayer().teleport(ProBending.configM.getLocationsConfig().getSpawn());
                player.getTeam().removePlayer(player);
            } else if (kill) {
                Player killer = ProBending.playerM.getLastDamager(player.getPlayer());
                String message;
                if (killer != null) {
                    message = LANG_KNOCK_OUT.replaceAll("%player%", player.getPlayer().getName()).replaceAll("%killer%", killer.getName());
                    PBPlayer e = ProBending.playerM.getPlayer(killer);
                    CommandConfig.Commands.ArenaPlayerKillPlayer.run(killer);
                    e.setKills(e.getKills() + 1);
                } else {
                    message = LANG_KNOCK_OUT_NO_KILLER.replaceAll("%player%", player.getPlayer().getName());
                }
                sendMessage(message, true);
                player.setState(ActivePlayer.State.SPECTATING);
            } else {
                player.getPlayer().teleport(ProBending.configM.getLocationsConfig().getSpawn());
                player.getTeam().removePlayer(player);
                sendMessage(LANG_LEAVE_GAME.replaceAll("%player%", player.getPlayer().getName()), true);
            }
            CommandConfig.Commands.ArenaPlayerDiePlayer.run(player.getPlayer());
            player.getPlayer().getWorld().strikeLightningEffect(player.getPlayer().getLocation());
            setTeamToCheck(player.getTeamTag());
        }
    }


    public void addSpectator(SpectatorPlayer player) {
        SPECTATORS.add(player);
        player.getPlayer().teleport(getArena().getCenter());
        CommandConfig.Commands.ArenaSpectatorJoinPlayer.run(player.getPlayer());
    }

    public void removeSpectator(SpectatorPlayer player) {
        SPECTATORS.remove(player);
        CommandConfig.Commands.ArenaSpectatorQuitPlayer.run(player.getPlayer());
    }

    // -----------

    public void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut, boolean spectators) {
        blueTeam.sendTitle(title, subtitle, fadeIn, stay, fadeOut, spectators);
        redTeam.sendTitle(title, subtitle, fadeIn, stay, fadeOut, spectators);
        if (spectators) SPECTATORS.forEach(p -> p.getPlayer().sendTitle(title, subtitle, fadeIn, stay, fadeOut));
    }

    public void sendMessage(String message, boolean spectators) {
        blueTeam.sendMessage(message, spectators);
        redTeam.sendMessage(message, spectators);
        if (spectators) SPECTATORS.forEach(p -> p.getPlayer().sendMessage(message));
    }

    // -----------

    @Override
    public int hashCode() {
        return getArena().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() == this.getClass()) {
            return ((ActiveArena) obj).getArena().equals(getArena());
        }
        return false;
    }

    @Language("ActiveArena.Info.NotInRound")
    public static String LANG_NOT_IN_ROUND = "Not in round!";

    @Language("ActiveArena.Info.Tie")
    public static String LANG_TIE = "Tie!";

    @Override
    public String onPlaceholderRequest(String param) {
        switch (param.toLowerCase()) {
            case "state":
                return getState().toString();
            case "winningteam":
                return getWinningTeamByRound() == null ? LANG_TIE : getWinningTeamByRound() == TeamTag.BLUE ? TeamTag.BLUE.toString() : TeamTag.RED.toString();
            case "round":
                return String.valueOf(getRound());
            case "r_time":
                return handler.getTimeLeft() == -1 ? "0" : UtilMethods.secondsToMinutes((int) ((float) handler.getTimeLeft() / 20f));
            case "game_time":
                return UtilMethods.secondsToMinutes((int) ((float) arenaTick / 20f));
            case "game_type":
                return gameType.toString();
            case "spectators":
                return String.valueOf(SPECTATORS.size());
        }
        return null;
    }

    @Override
    public String placeHolderPrefix() {
        return "arena";
    }

    // -----------
}
