package com.probending.probending.api.events;

import com.probending.probending.core.players.ActivePlayer;
import com.probending.probending.core.arena.ActiveArena;
import com.probending.probending.core.enums.ArenaState;
import org.bukkit.event.HandlerList;

public class PBPlayerEvent extends PBArenaEvent {

    private final ActivePlayer player;

    public PBPlayerEvent(ActiveArena arena, ArenaState state, ActivePlayer player) {
        super(arena, state);
        this.player = player;
    }

    public ActivePlayer getPlayer() {
        return player;
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
