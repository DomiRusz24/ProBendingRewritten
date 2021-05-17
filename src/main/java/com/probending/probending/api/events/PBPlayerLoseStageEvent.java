package com.probending.probending.api.events;

import com.probending.probending.core.players.ActivePlayer;
import com.probending.probending.core.arena.ActiveArena;
import com.probending.probending.core.enums.ArenaState;
import com.probending.probending.core.enums.Ring;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class PBPlayerLoseStageEvent extends PBPlayerUpdateStageEvent implements Cancellable {

    private boolean canceled = false;

    public PBPlayerLoseStageEvent(ActiveArena arena, ArenaState state, ActivePlayer player, Ring currentStage, int ringOffset) {
        super(arena, state, player, currentStage, ringOffset);
    }

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
