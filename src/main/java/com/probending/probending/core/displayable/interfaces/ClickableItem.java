package com.probending.probending.core.displayable.interfaces;

import com.probending.probending.util.UtilMethods;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public interface ClickableItem extends LeftClickable, RightClickable {

    ItemStack getItemStack();

    @Override
    default boolean isLeftClickedOn(PlayerInteractEvent event) {
        if (isClickedOn(event.getItem())) {
            event.setCancelled(true);
            return true;
        } else {
            return false;
        }
    }

    @Override
    default boolean isLeftClickedOn(EntityDamageByEntityEvent event) {
        return isClickedOn(((Player) event.getDamager()).getInventory().getItemInMainHand());
    }

    @Override
    default boolean isRightClickedOn(PlayerInteractEvent event) {
        if (isClickedOn(event.getItem())) {
            event.setCancelled(true);
            return true;
        } else {
            return false;
        }
    }

    @Override
    default boolean isRightClickedOn(PlayerInteractAtEntityEvent event) {
        return isClickedOn(event.getPlayer().getInventory().getItemInMainHand());
    }

    default boolean isClickedOn(ItemStack item) {
        return item != null && getItemStack().getType().equals(item.getType()) && Objects.equals(getItemStack().getItemMeta(), item.getItemMeta());
    }


}
