package com.probending.probending.core.gui.shortcuts;

import com.probending.probending.core.gui.GUIItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class EmptyItem extends GUIItem {
    public EmptyItem(ItemStack item, int slot) {
        super(
                (t) -> {},
                (t) -> {},
                item,
                slot);
    }
}
