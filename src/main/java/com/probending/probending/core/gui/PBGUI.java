package com.probending.probending.core.gui;

import com.probending.probending.ProBending;
import com.probending.probending.core.gui.shortcuts.EmptyItem;
import com.probending.probending.util.UtilMethods;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class PBGUI {

    public static ItemStack EMPTY;

    public static ItemStack getEmptyItemStack() {
        return EMPTY.clone();
    }

    private Inventory inventory;

    private HashMap<Integer, GUIItem> items = new HashMap<>();

    private ArrayList<Player> viewers = new ArrayList<>();

    private final ItemStack emptySlot;

    public PBGUI(String name, int size) {
        inventory = Bukkit.createInventory(null, size, name);
        this.emptySlot = emptySlot();
    }

    public PBGUI(String name, InventoryType type) {
        inventory = Bukkit.createInventory(null, type, name);
        this.emptySlot = emptySlot();
    }

    public void refresh() {
        if (emptySlot != null) {
            for (int i = 0; i < inventory.getSize(); i++) {
                if (inventory.getItem(i) == null || inventory.getItem(i).getType() == Material.AIR) {
                    registerItem(new EmptyItem(emptySlot, i));
                }
            }
        }
        for (Integer slot : items.keySet()) {
            inventory.setItem(slot, items.get(slot).getItem().clone());
        }
    }

    public void addPlayer(Player player) {
        ConcurrentLinkedQueue<PBGUI> pages = ProBending.playerM.GUIS_BY_PLAYER.getOrDefault(player, null);
        viewers.add(player);
        if (pages == null || pages.isEmpty()) {
            ProBending.playerM.GUIS_BY_PLAYER.put(player, new ConcurrentLinkedQueue<>());
            ProBending.playerM.GUIS_BY_PLAYER.get(player).add(this);
            firstOpen(player);
        } else {
            PBGUI old = pages.peek();
            if (!old.equals(this)) {
                ProBending.playerM.GUIS_BY_PLAYER.get(player).add(this);
                old.onPlayerSwitch(player);
            }
            pageOpen(player);
        }
        player.openInventory(inventory);
    }

    public void removePlayer(Player player) {
        if (ProBending.playerM.getLatestGUI(player) == this) {
            onPlayerClose(player);
            Bukkit.getScheduler().runTask(ProBending.plugin, player::closeInventory);
        }

    }

    public void onPlayerClose(Player player) {
        if (ProBending.playerM.getLatestGUI(player) == this) {
            for (PBGUI pbgui : ProBending.playerM.GUIS_BY_PLAYER.get(player)) {
                pbgui.viewers.remove(player);
                pbgui.close(player);
            }
            ProBending.playerM.GUIS_BY_PLAYER.remove(player);
        }
    }


    private void onPlayerSwitch(Player player) {
        viewers.remove(player);
        changeGUI(player);
    }

    public void registerItem(GUIItem item) {
        items.put(item.getSlot(), item);
    }

    public ItemStack createItem(Material type, byte data, String name, boolean glow, String... desc) {
        return UtilMethods.createItem(type, data, name, glow, desc);
    }

    public void onLeftClick(Player player, ItemStack item) {
        for (GUIItem guiItem : items.values()) {
            if (guiItem.isTheSame(item)) {
                guiItem.onLeftClick(player);
                break;
            }
        }
    }

    public void onRightClick(Player player, ItemStack item) {
        for (GUIItem guiItem : items.values()) {
            if (guiItem.isTheSame(item)) {
                guiItem.onRightClick(player);
                break;
            }
        }
    }

    public GUIItem getItem(int slot) {
        if (items.containsKey(slot)) {
            return items.get(slot);
        } else {
            if (emptySlot == null) return null;
            GUIItem item = new EmptyItem(emptySlot, slot);
            registerItem(item);
            return item;
        }
    }

    public ArrayList<Player> getViewers() {
        return viewers;
    }

    public Collection<GUIItem> getItems() {
        return items.values();
    }

    public Inventory getInventory() {
        return inventory;
    }

    public String getTitle() {
        return inventory.getTitle();
    }

    protected abstract void firstOpen(Player player);

    protected abstract void pageOpen(Player player);

    protected abstract void close(Player player);

    protected abstract void changeGUI(Player player);

    protected abstract ItemStack emptySlot();

}
