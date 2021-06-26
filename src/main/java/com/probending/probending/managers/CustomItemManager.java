package com.probending.probending.managers;

import com.probending.probending.ProBending;
import com.probending.probending.core.annotations.Language;
import com.probending.probending.core.displayable.PBItemStack;
import com.probending.probending.core.gui.PBGUI;
import com.probending.probending.core.gui.guis.TeamPlayGUI;
import com.probending.probending.util.UtilMethods;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import java.util.HashSet;
import java.util.Set;

public class CustomItemManager extends PBManager implements ConfigManager.Reloadable {

    private final HashSet<PBItemStack> CUSTOM_ITEMS = new HashSet<>();

    private PBItemStack LEAVE_COMMAND;

    private PBItemStack WATER_BOTTLE;

    public CustomItemManager(ProBending plugin) {
        super(plugin);
        registerReloadable();
        loadItems();
    }

    @Language("Item.Leave.Title")
    public static String LANG_LEAVE_TITLE = "Leave!";

    @Language("Item.Leave.Desc")
    public static String LANG_LEAVE_DESC = "Click here to leave!";

    public Set<PBItemStack> getCustomItems() {
        return CUSTOM_ITEMS;
    }

    public void registerItem(PBItemStack item) {
        CUSTOM_ITEMS.add(item);
    }

    private void loadItems() {
        LEAVE_COMMAND = new PBItemStack(
                (p) -> {
                    p.performCommand("leave");
                    },
                (p) -> {
                    p.performCommand("leave");
                    },
                UtilMethods.createItem(Material.MAGMA_CREAM, (byte) 0, LANG_LEAVE_TITLE, true, UtilMethods.stringToList(LANG_LEAVE_DESC))
                );
        registerItem(LEAVE_COMMAND);

        ItemStack potion = UtilMethods.createItem(Material.POTION, (byte) 0, "", false, "");
        potion.setAmount(64);
        PotionMeta pmeta = (PotionMeta) potion.getItemMeta();
        pmeta.setBasePotionData(new PotionData(PotionType.WATER));
        potion.setItemMeta(pmeta);

        WATER_BOTTLE = new PBItemStack(
                (p) -> {

                },
                (p) -> {

                },
                potion
        );
        registerItem(WATER_BOTTLE);





        PBGUI.EMPTY = UtilMethods.createItem(Material.GRAY_STAINED_GLASS_PANE, (byte) 0, "", false, "");
    }

    public PBItemStack LEAVE_COMMAND() {
        return LEAVE_COMMAND;
    }

    public PBItemStack WATER_BOTTLE() {
        return WATER_BOTTLE;
    }

    @Override
    public void onReload() {
        LEAVE_COMMAND.unregister();
        loadItems();
    }
}
