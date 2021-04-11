package com.probending.probending;

import com.probending.probending.managers.PlayerManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PBListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        ProBending.SQLManager.createTeamPlayer(event.getPlayer());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        ProBending.playerM.removePBPlayer(ProBending.playerM.getPlayer(event.getPlayer().getUniqueId()));
    }
}
