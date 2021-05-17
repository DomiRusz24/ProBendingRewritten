package com.probending.probending.core.displayable.interfaces;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public interface RightClickable {
    void onRightClick(Player player);

    boolean isRightClickedOn(PlayerInteractEvent event);

    boolean isRightClickedOn(PlayerInteractAtEntityEvent event);
}
