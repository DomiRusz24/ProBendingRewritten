package com.probending.probending.api.events;

import com.probending.probending.core.arena.ActiveArena;
import com.probending.probending.core.arena.Arena;
import com.probending.probending.core.enums.ArenaState;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class PBArenaStartEvent extends PBPreArenaEvent implements Cancellable {

    public PBArenaStartEvent(Arena arena) {
        super(arena);
    }

    private boolean canceled = false;

    @Override
    public boolean isCancelled() {
        return canceled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        canceled = cancel;
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
