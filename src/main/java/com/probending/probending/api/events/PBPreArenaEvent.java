package com.probending.probending.api.events;

import com.probending.probending.core.arena.Arena;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PBPreArenaEvent extends Event {

    private final Arena arena;

    public PBPreArenaEvent(Arena arena) {
        this.arena = arena;
    }

    public Arena getArena() {
        return arena;
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
