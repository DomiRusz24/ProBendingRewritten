package com.probending.probending.core.gui;

import com.probending.probending.ProBending;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;

public class GUIItem {

    protected Consumer<Player> leftClick, rightClick;

    protected ItemStack item;

    protected int slot;

    public GUIItem(Consumer<Player> leftClick, Consumer<Player> rightClick, ItemStack item, int slot) {
        this.leftClick = leftClick;
        this.rightClick = rightClick;
        this.item = item.clone();
        this.slot = slot;
    }

    public ItemStack getItem() {
        return item;
    }

    public GUIItem setItem(ItemStack item) {
        this.item = item.clone();
        return this;
    }

    public GUIItem setName(String name) {
        ItemMeta m = item.getItemMeta();
        m.setDisplayName(name);
        item.setItemMeta(m);
        return this;
    }

    public GUIItem setDescription(String... lines) {
        ItemMeta m = item.getItemMeta();
        m.setLore(Arrays.asList(lines));
        item.setItemMeta(m);
        return this;
    }

    public GUIItem setMaterial(Material material) {
        item.setType(material);
        return this;
    }

    public GUIItem setData(byte data) {
        item = ProBending.nmsM.setData(item, data);
        return this;
    }

    public GUIItem setGlow(boolean glow) {
        if (glow) {
            ItemMeta meta = item.getItemMeta();
            meta.addEnchant(Enchantment.LUCK, 1, true);
            item.setItemMeta(meta);
        } else {
            item.removeEnchantment(Enchantment.LUCK);
        }
        return this;
    }

    public boolean isTheSame(ItemStack compare) {
        if (compare.getType() == item.getType()) {
            if (compare.getItemMeta() != null && item.getItemMeta() != null) {
                return Objects.equals(compare.getItemMeta().getDisplayName(), item.getItemMeta().getDisplayName());
            } else {
                return compare.getItemMeta() == null && item.getItemMeta() == null;
            }
        }
        return false;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public void onLeftClick(Player player) {
        leftClick.accept(player);
    }

    public void onRightClick(Player player) {
        rightClick.accept(player);
    }


}
