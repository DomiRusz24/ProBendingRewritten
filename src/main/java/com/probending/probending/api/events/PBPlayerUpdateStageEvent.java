package com.probending.probending.api.events;

import com.probending.probending.core.players.ActivePlayer;
import com.probending.probending.core.arena.ActiveArena;
import com.probending.probending.core.enums.ArenaState;
import com.probending.probending.core.enums.Ring;
import org.bukkit.event.HandlerList;

public class PBPlayerUpdateStageEvent extends PBPlayerEvent {

    private final Ring currentStage;
    private final int ringOffset;


    public PBPlayerUpdateStageEvent(ActiveArena arena, ArenaState state, ActivePlayer player, Ring currentStage, int ringOffset) {
        super(arena, state, player);
        this.currentStage = currentStage;
        this.ringOffset = ringOffset;
    }

    public int getRingOffset() {
        return ringOffset;
    }

    public Ring getCurrentStage() {
        return currentStage;
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
