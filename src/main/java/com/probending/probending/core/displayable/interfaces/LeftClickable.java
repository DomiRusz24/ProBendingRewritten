package com.probending.probending.core.displayable.interfaces;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public interface LeftClickable {
    void onLeftClick(Player player);

    boolean isLeftClickedOn(PlayerInteractEvent event);

    boolean isLeftClickedOn(EntityDamageByEntityEvent event);
}
