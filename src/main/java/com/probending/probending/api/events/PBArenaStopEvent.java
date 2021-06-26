package com.probending.probending.api.events;

import com.probending.probending.core.arena.ActiveArena;
import com.probending.probending.core.enums.ArenaState;
import com.probending.probending.core.enums.TeamTag;
import org.bukkit.event.HandlerList;

public class PBArenaStopEvent extends PBArenaEvent {

    private final TeamTag winningTeam;

    public PBArenaStopEvent(ActiveArena arena, ArenaState state, TeamTag winningTeam) {
        super(arena, state);
        this.winningTeam = winningTeam;
    }

    public TeamTag getWinningTeam() {
        return winningTeam;
    }

    private static final HandlerList HANDLERS = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
