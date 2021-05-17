package com.probending.probending.api.events;

import com.probending.probending.ProBending;
import com.probending.probending.core.players.ActivePlayer;
import com.probending.probending.core.arena.ActiveArena;
import com.probending.probending.core.enums.ArenaState;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class PBPlayerDamagePBPlayerEvent extends PBArenaEvent implements Cancellable {

    private final EntityDamageByEntityEvent entityDamageEvent;

    private final ActivePlayer entity;
    private final ActivePlayer damager;

    public PBPlayerDamagePBPlayerEvent(ActiveArena arena, ArenaState state, EntityDamageByEntityEvent entityDamageEvent) {
        super(arena, state);
        this.entityDamageEvent = entityDamageEvent;
        entity = ProBending.playerM.getActivePlayer((Player) entityDamageEvent.getEntity());
        damager = ProBending.playerM.getActivePlayer((Player) entityDamageEvent.getDamager());
    }

    public double getDamage() {
        return entityDamageEvent.getDamage();
    }

    public void setDamage(double damage) {
        entityDamageEvent.setDamage(damage);
    }

    public void setDamage(EntityDamageEvent.DamageModifier type, double damage) {
        entityDamageEvent.setDamage(type, damage);
    }

    public EntityDamageEvent.DamageCause getCause() {
        return entityDamageEvent.getCause();
    }

    public double getFinalDamage() {
        return entityDamageEvent.getFinalDamage();
    }

    public ActivePlayer getEntity() {
        return entity;
    }

    public ActivePlayer getDamager() {
        return damager;
    }

    @Override
    public boolean isCancelled() {
        return entityDamageEvent.isCancelled();
    }

    @Override
    public void setCancelled(boolean cancel) {
        entityDamageEvent.setCancelled(cancel);
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
