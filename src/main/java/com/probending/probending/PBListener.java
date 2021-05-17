package com.probending.probending;

import com.comphenix.protocol.PacketType;
import com.probending.probending.api.events.PBPlayerDamagePBPlayerEvent;
import com.probending.probending.core.displayable.interfaces.LeftClickable;
import com.probending.probending.core.displayable.interfaces.RightClickable;
import com.probending.probending.core.players.ActivePlayer;
import com.probending.probending.util.PerTick;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.Hash;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashSet;

public class PBListener implements Listener {

    private static final HashSet<LeftClickable> LEFT_CLICKABLES = new HashSet<>();
    private static final HashSet<RightClickable> RIGHT_CLICKABLES = new HashSet<>();
    private static final HashSet<PerTick> PER_TICKABLE = new HashSet<>();

    public static void hookInListener(LeftClickable clickable) {
        LEFT_CLICKABLES.add(clickable);
    }

    public static void hookInListener(RightClickable clickable) {
        RIGHT_CLICKABLES.add(clickable);
    }

    public static void hookInListener(PerTick tickable) {
        PER_TICKABLE.add(tickable);
    }

    public static void removeListener(LeftClickable clickable) {
        LEFT_CLICKABLES.remove(clickable);
    }

    public static void removeListener(RightClickable clickable) {
        RIGHT_CLICKABLES.remove(clickable);
    }

    public static void removeListener(PerTick tickable) {
        PER_TICKABLE.remove(tickable);
    }

    public PBListener() {
        new BukkitRunnable() {
            @Override
            public void run() {
                PER_TICKABLE.forEach(PerTick::onTick);
            }
        }.runTaskTimer(ProBending.plugin, 0, 1);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        ProBending.SqlM.playerTable.createTeamPlayer(event.getPlayer());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        ProBending.playerM.unregisterPlayer(event.getPlayer());
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
            if (!event.getFrom().toVector().toBlockVector().equals(event.getTo().toVector().toBlockVector())) {
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
        } else if (event.getDamager().getType().equals(EntityType.PLAYER)) {
            Player damager = (Player) event.getDamager();
            LEFT_CLICKABLES.forEach(p -> {
                if (p.isLeftClickedOn(event)) {
                    p.onLeftClick(damager);
                }
            });
        }
    }

    @EventHandler()
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            RIGHT_CLICKABLES.forEach(p -> {
                if (p.isRightClickedOn(event)) {
                    p.onRightClick(event.getPlayer());
                }
            });
        } else if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            LEFT_CLICKABLES.forEach(p -> {
                if (p.isLeftClickedOn(event)) {
                    p.onLeftClick(event.getPlayer());
                }
            });
        }
    }

    @EventHandler
    public void onEntityInteract(PlayerInteractAtEntityEvent event) {
        RIGHT_CLICKABLES.forEach(p -> {
            if (p.isRightClickedOn(event)) {
                p.onRightClick(event.getPlayer());
            }
        });
    }

    @EventHandler()
    public void onArmorStandInteract(PlayerArmorStandManipulateEvent event) {

    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        event.setCancelled(ProBending.commandM.preformCommand(event.getPlayer(), event.getMessage()));
    }

    @EventHandler
    public void onSeverCommand(ServerCommandEvent event) {
        event.setCancelled(ProBending.commandM.preformCommand(event.getSender(), event.getCommand()));
    }
}
