package com.probending.probending;

import com.probending.probending.api.events.PBPlayerDamagePBPlayerEvent;
import com.probending.probending.core.arena.ActiveArena;
import com.probending.probending.core.arena.Arena;
import com.probending.probending.core.players.ActivePlayer;
import com.projectkorra.projectkorra.event.BendingPlayerCreationEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PBListener implements Listener {

    @EventHandler
    public void onBendingPlayer(BendingPlayerCreationEvent event) {
        ProBending.projectKorraM.getBendingPlayerByUUID().put(event.getBendingPlayer().getPlayer().getUniqueId(), event.getBendingPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        ProBending.projectKorraM.getBendingPlayerByUUID().remove(event.getPlayer().getUniqueId());
    }

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

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player && event.getEntity() instanceof Player)) return;

        Player debug = (Player) event.getDamager();

        //TODO: REMOVE DEBUGER

        ActivePlayer damager = ProBending.playerM.getActivePlayer((Player) event.getDamager());
        ActivePlayer entity = ProBending.playerM.getActivePlayer((Player) event.getEntity());

        if (damager == null && entity == null) {
            debug.sendMessage("1");
            return;
        }
        if (damager == null || entity == null) {
            debug.sendMessage("2 (you: " + (damager == null) + " it: " + (entity == null) + ")");
            event.setCancelled(true);
            return;
        }

        if (!damager.getArena().equals(entity.getArena())) {
            debug.sendMessage("3");
            event.setCancelled(true);
            return;
        }

        if (damager.getTeamTag() == entity.getTeamTag()) {
            debug.sendMessage("4");
            event.setCancelled(true);
            return;
        }

        ActiveArena arena = damager.getArena();

        PBPlayerDamagePBPlayerEvent pbPlayerEvent = new PBPlayerDamagePBPlayerEvent(arena, arena.getState(), event);
        Bukkit.getPluginManager().callEvent(pbPlayerEvent);

        if (pbPlayerEvent.isCancelled()) {
            debug.sendMessage("6");
            event.setCancelled(true);
            return;
        }

        ProBending.playerM.registerHit(damager.getPlayer(), entity.getPlayer());

        damager.getArena().callDamageEvent(pbPlayerEvent);
    }
}
