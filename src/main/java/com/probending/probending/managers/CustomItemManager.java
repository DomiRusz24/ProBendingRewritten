package com.probending.probending.managers;

import com.probending.probending.ProBending;
import me.domirusz24.plugincore.PluginCore;
import me.domirusz24.plugincore.config.annotations.Language;
import com.probending.probending.util.UtilMethods;
import me.domirusz24.plugincore.core.displayable.ClickableItemStack;
import me.domirusz24.plugincore.managers.ConfigManager;
import me.domirusz24.plugincore.managers.Manager;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import java.util.HashSet;
import java.util.Set;

public class CustomItemManager extends Manager implements ConfigManager.Reloadable {

    private static ClickableItemStack LEAVE_COMMAND;

    private static ClickableItemStack WATER_BOTTLE;

    public CustomItemManager(ProBending plugin) {
        super(plugin);
        registerReloadable();
        loadItems();
    }

    @Language("Item.Leave.Title")
    public static String LANG_LEAVE_TITLE = "Leave!";

    @Language("Item.Leave.Desc")
    public static String LANG_LEAVE_DESC = "Click here to leave!";

    private void loadItems() {
        LEAVE_COMMAND = new ClickableItemStack(
                ProBending.plugin,
                (p) -> {
                    p.performCommand("leave");
                    },
                (p) -> {
                    p.performCommand("leave");
                    },
                UtilMethods.createItem(Material.MAGMA_CREAM, (byte) 0, LANG_LEAVE_TITLE, true, UtilMethods.stringToList(LANG_LEAVE_DESC))
                );

        ItemStack potion = UtilMethods.createItem(Material.POTION, (byte) 0, "", false, "");
        potion.setAmount(1);
        PotionMeta pmeta = (PotionMeta) potion.getItemMeta();
        pmeta.setBasePotionData(new PotionData(PotionType.WATER));
        potion.setItemMeta(pmeta);

        WATER_BOTTLE = new ClickableItemStack(
                ProBending.plugin,
                (p) -> {

                },
                (p) -> {

                },
                potion
        );
    }

    public static ClickableItemStack LEAVE_COMMAND() {
        return LEAVE_COMMAND;
    }

    public static ClickableItemStack WATER_BOTTLE() {
        return WATER_BOTTLE;
    }

    @Override
    public void onReload() {
        LEAVE_COMMAND.unregister();
        WATER_BOTTLE.unregister();
        loadItems();
    }

    @Override
    public PluginCore getCorePlugin() {
        return ProBending.plugin;
    }
}
