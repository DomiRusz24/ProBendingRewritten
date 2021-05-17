package com.probending.probending.api.events;

import com.probending.probending.api.enums.KnockOutCause;
import com.probending.probending.core.players.ActivePlayer;
import com.probending.probending.core.arena.ActiveArena;
import com.probending.probending.core.enums.ArenaState;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class PBPlayerKnockOutEvent extends PBPlayerEvent implements Cancellable {

    private final KnockOutCause cause;

    private boolean cancelled;

    public PBPlayerKnockOutEvent(ActiveArena arena, ArenaState state, ActivePlayer player, KnockOutCause cause) {
        super(arena, state, player);
        this.cause = cause;
    }

    public KnockOutCause getCause() {
        return cause;
    }

    private static final HandlerList HANDLERS = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }
}
