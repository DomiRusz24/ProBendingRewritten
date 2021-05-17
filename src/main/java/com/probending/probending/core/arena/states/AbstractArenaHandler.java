package com.probending.probending.core.arena.states;

import com.probending.probending.api.events.PBPlayerDamagePBPlayerEvent;
import com.probending.probending.api.events.PBPlayerKnockOutEvent;
import com.probending.probending.api.events.PBPlayerUpdateStageEvent;
import com.probending.probending.core.arena.ActiveArena;
import com.probending.probending.core.arena.states.midroundstate.GameEnd;
import com.probending.probending.core.enums.ArenaState;
import com.probending.probending.core.enums.Ring;
import com.probending.probending.core.enums.TeamTag;
import com.probending.probending.core.players.ActivePlayer;

public abstract class AbstractArenaHandler {

    private boolean needStart = true;

    private final ActiveArena arena;

    public AbstractArenaHandler(ActiveArena arena) {
        this.arena = arena;
    }

    public ActiveArena getArena() {
        return arena;
    }

    public int getTimeLeft() {
        return -1;
    }

    public int getTime() {
        return 0;
    }

    public abstract void onStart();

    public abstract void onPlayerKnockOut(PBPlayerKnockOutEvent event);

    public abstract void onPBPlayerDamagePBPlayer(PBPlayerDamagePBPlayerEvent event);

    public abstract void onPlayerUpdateStage(PBPlayerUpdateStageEvent event);

    public abstract void onUpdate();

    public void onWipeOut(TeamTag team) {
        getArena().setState(ArenaState.MID_ROUND);
        getArena().setHandler(new MidRoundState(getArena(), new EndingState(getArena(), team.getOther()), ArenaState.STOP, team.getOther(), new GameEnd()));
    }

    public void onGap(TeamTag tag, int gap, Ring furthest) {
        arena.setHandler(new GainingStageState(getArena(), tag.getOther(), this.getTime(), gap, furthest));
    }

    public boolean isTieBreaker() {
        return false;
    }

    public Ring onPlayerPreProcess(ActivePlayer player, Ring current) {
        if (!isTieBreaker()) {
            if (current == Ring.BLUE_TIEBREAKER) {
                return Ring.BLUE_FIRST;
            } else if (current == Ring.RED_TIEBREAKER) {
                return Ring.RED_FIRST;
            }
        }
        return current;
    }

    public boolean needStart() {
        return needStart;
    }

    public void start() {
        needStart = false;
        onStart();
    }
}
