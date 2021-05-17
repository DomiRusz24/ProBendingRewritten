package com.probending.probending.api.events;

import com.probending.probending.core.arena.ActiveArena;
import com.probending.probending.core.enums.ArenaState;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PBArenaEvent extends Event {

    private final ActiveArena arena;

    private final ArenaState state;


    public PBArenaEvent(ActiveArena arena, ArenaState state) {
        this.arena = arena;
        this.state = state;
    }

    public ActiveArena getArena() {
        return arena;
    }

    public ArenaState getArenaState() {
        return state;
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
