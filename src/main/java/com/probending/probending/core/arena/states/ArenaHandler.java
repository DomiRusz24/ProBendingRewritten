package com.probending.probending.core.arena.states;

import com.probending.probending.api.enums.KnockOutCause;
import com.probending.probending.api.events.PBPlayerDamagePBPlayerEvent;
import com.probending.probending.api.events.PBPlayerKnockOutEvent;
import com.probending.probending.api.events.PBPlayerUpdateStageEvent;
import com.probending.probending.core.players.ActivePlayer;
import com.probending.probending.core.arena.ActiveArena;

public class ArenaHandler extends AbstractArenaHandler {


    public ArenaHandler(ActiveArena arena) {
        super(arena);
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onPlayerKnockOut(PBPlayerKnockOutEvent event) {
        if (event.getCause() != KnockOutCause.LEFT) {
            event.getPlayer().setState(ActivePlayer.State.PLAYING);
            if (event.getCause().equals(KnockOutCause.FALL_OFF_ARENA)) {
                event.getPlayer().teleportTo(event.getPlayer().getRing());
            }
        }
    }

    @Override
    public void onPBPlayerDamagePBPlayer(PBPlayerDamagePBPlayerEvent event) {
        event.setDamage(0);
        event.setCancelled(true);
    }

    @Override
    public void onPlayerUpdateStage(PBPlayerUpdateStageEvent event) {
        event.getPlayer().dragTo(event.getPlayer().getRing());
    }

    @Override
    public void onUpdate() {

    }
}
