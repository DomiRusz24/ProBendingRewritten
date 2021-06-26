package com.probending.probending;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.onarandombox.MultiverseCore.MultiverseCore;
import com.probending.probending.managers.*;
import com.probending.probending.managers.HologramManager;
import com.probending.probending.managers.database.DataBaseManager;
import com.probending.probending.managers.nms.NMSManager;
import com.probending.probending.managers.nms.methods.ChangeItemStackData;
import com.probending.probending.managers.schematics.EasyRollBackManager;
import com.probending.probending.managers.schematics.SchematicManager;
import com.probending.probending.managers.schematics.WorldEditManager;
import com.projectkorra.projectkorra.ProjectKorra;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class ProBending extends JavaPlugin {

    // Plugin
    public static ProBending plugin;

    // Dependencies
    public static ProjectKorra projectKorra = null;
    public static MultiverseCore multiverse = null;
    public static ProtocolManager protocol = null;


    // Managers
    public static DataBaseManager SqlM;
    public static ConfigManager configM;
    public static PlayerManager playerM;
    public static TeamManager teamM;
    public static ArenaManager arenaM;
    public static SchematicManager schematicM;
    public static ProjectKorraManager projectKorraM;
    public static CommandManager commandM;
    public static PAPIManager placeHolderM;
    public static NMSManager nmsM;
    public static HologramManager hologramM;
    public static SignManager signM;
    public static RegionManager regionM;
    public static CustomItemManager itemM;

    @Override
    public void onEnable() {
        initialize();

        loadDependencies();
        loadManagers();

        registerEvents();

        registerCommands();
    }

    @Override
    public void onDisable() {
        disable();
    }

    // onEnable
    public void initialize() {
        plugin = this;
        log(Level.INFO, "ProBending " + plugin.getDescription().getVersion() + " has been loaded!");
    }


    // Plugin dependencies, everything external.
    public void loadDependencies() {

        // PK
        projectKorra = ProjectKorra.plugin;

        // Protocol
        protocol = ProtocolLibrary.getProtocolManager();

        // Multiverse
        multiverse = (MultiverseCore) hookInto("Multiverse-Core");


        // Holograms



    }

    private Plugin hookInto(String pluginName) {
        Plugin jPlugin = Bukkit.getPluginManager().getPlugin(pluginName);
        if (jPlugin != null) {
            log(Level.INFO, "Hooked into " + pluginName + "!");
            return jPlugin;
        } else {
            return null;
        }
    }

    private boolean isHookAble(String pluginName) {
        return Bukkit.getPluginManager().isPluginEnabled(pluginName);
    }


    // Managers that ease the use of Core classes and dependencies.
    public void loadManagers() {

        // Config
        configM = new ConfigManager(plugin);

        // SQL
        SqlM = new DataBaseManager(plugin);

        // NMS
        nmsM = new NMSManager(plugin, protocol, new ChangeItemStackData() {
            @Override
            public ItemStack setData(ItemStack item, byte data) {
                item.getData().setData(data);
                return item;
            }
        });

        // Items
        itemM = new CustomItemManager(plugin);

        placeHolderM = new PAPIManager(plugin);
        placeHolderM.register();

        // Player
        playerM = new PlayerManager(plugin);

        // Team
        teamM = new TeamManager(plugin);

        signM = new SignManager(plugin);

        hologramM = new HologramManager(plugin);

        regionM = new RegionManager(plugin);

        // Arena
        arenaM = new ArenaManager(plugin);

        // WorldEdit, FastAsyncWorldEdit
        if (isHookAble("WorldEdit")) {
            schematicM = new EasyRollBackManager(this, (WorldEditPlugin) hookInto("WorldEdit"));
        } else {
            log(Level.SEVERE, "Your server does not have WorldEdit! Stopping plugin...");
            shutOffPlugin();
            return;
        }
        // ProjectKorra
        projectKorraM = new ProjectKorraManager(plugin);

        // Command
        commandM = new CommandManager(plugin);

    }

    // Spigot events
    public void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new PBListener(), this);
    }

    // Spigot commands
    public void registerCommands() {
    }

    // onDisable
    public void disable() {
        log(Level.INFO, "ProBending " + plugin.getDescription().getVersion() + " has been disabled!");
        arenaM.ARENA_BY_NAME.values().forEach((a) -> {
            if (a.inGame()) {
                a.getActiveArena().forceUnstableStop();
            }
        });
        SqlM.onDisable();
    }

    // Use this for printing out info to the console.
    public void log(Level level, String msg) {
        this.getLogger().log(level, msg);
    }

    // Use this when something critical to the functioning of the plugin isn't working.
    public void shutOffPlugin() {
        log(Level.SEVERE, "The plugin has been disabled due to a critical error!");
        setEnabled(false);
    }

    // Config stuff
    @Override
    public YamlConfiguration getConfig() {
        return configM.getConfig();
    }

    @Override
    public void saveConfig() {
        configM.getConfig().save();
    }

    @Override
    public void reloadConfig() {
        configM.getConfig().reload();
    }
}
