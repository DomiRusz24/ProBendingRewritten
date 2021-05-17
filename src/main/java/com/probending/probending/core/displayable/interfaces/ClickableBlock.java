package com.probending.probending.core.displayable.interfaces;

import com.probending.probending.core.enums.Ring;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public interface ClickableBlock extends LeftClickable, RightClickable {

    Location getLocation();

    @Override
    default boolean isLeftClickedOn(PlayerInteractEvent event) {
        if (isClickedOn(event.getClickedBlock())) {
            event.setCancelled(true);
            return true;
        } else {
            return false;
        }
    }

    @Override
    default boolean isLeftClickedOn(EntityDamageByEntityEvent event) {
        return false;
    }

    @Override
    default boolean isRightClickedOn(PlayerInteractEvent event) {
        if (isClickedOn(event.getClickedBlock())) {
            event.setCancelled(true);
            return true;
        } else {
            return false;
        }
    }

    default boolean isClickedOn(Block block) {
        if (block != null) {
            return block.getLocation().equals(getLocation());
        }
        return false;
    }

    @Override
    default boolean isRightClickedOn(PlayerInteractAtEntityEvent event) {
        return false;
    }
}
