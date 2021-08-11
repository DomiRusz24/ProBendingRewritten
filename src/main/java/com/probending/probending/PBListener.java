package com.probending.probending;

import com.probending.probending.api.events.PBPlayerDamagePBPlayerEvent;
import com.probending.probending.core.players.ActivePlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class PBListener implements Listener {
    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        ActivePlayer player = ProBending.playerM.getActivePlayer(event.getEntity());
        if (player != null) {
            event.getEntity().setHealth(20);
            player.setState(ActivePlayer.State.DEAD);
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (ProBending.playerM.getFrozenPlayers().contains(event.getPlayer())) {
            if (event.getTo() == null) return;
            if (event.getFrom().toVector().getBlockX() != event.getTo().toVector().getBlockX() || event.getFrom().toVector().getBlockZ() != event.getTo().toVector().getBlockZ()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager().getType().equals(EntityType.PLAYER) && event.getEntity().getType().equals(EntityType.PLAYER)) {
            ActivePlayer damager = ProBending.playerM.getActivePlayer((Player) event.getDamager());
            ActivePlayer entity = ProBending.playerM.getActivePlayer((Player) event.getEntity());
            if (damager != null && entity != null) {
                if (damager.getArena().equals(entity.getArena())) {
                    if (damager.getTeamTag().equals(entity.getTeamTag())) {
                        event.setCancelled(true);
                    } else {
                        PBPlayerDamagePBPlayerEvent pbPlayerEvent = new PBPlayerDamagePBPlayerEvent(damager.getArena(), damager.getArena().getState(), event);
                        Bukkit.getPluginManager().callEvent(pbPlayerEvent);
                        damager.getArena().callDamageEvent(pbPlayerEvent);
                        if (damager.getArena().getArena().getArenaConfig().getDamage()) {
                            pbPlayerEvent.setDamage(0);
                        }
                    }
                } else {
                    event.setCancelled(true);
                }
                if (!event.isCancelled()) {
                    ProBending.playerM.registerHit(damager.getPlayer(), entity.getPlayer());
                }
            } else if (damager != null || entity != null) {
                event.setCancelled(true);
            }
        }
    }
}
