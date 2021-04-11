package com.probending.probending;

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.probending.probending.command.SpectateCommand;
import com.probending.probending.managers.MySQLManager;
import com.probending.probending.managers.PlayerManager;
import com.probending.probending.managers.ConfigManager;
import com.projectkorra.projectkorra.ProjectKorra;
import com.sk89q.worldedit.WorldEdit;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class ProBending extends JavaPlugin {

    // Plugin
    public static ProBending plugin;

    // Dependencies
    public static ProjectKorra projectKorra = null;
    public static WorldEdit worldEdit = null;
    public static MultiverseCore multiverse = null;


    // Managers
    public static MySQLManager SQLManager;
    public static ConfigManager configM;
    public static PlayerManager playerM;

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
        log(Level.INFO, "ProBending " + plugin.getDescription().getVersion() + " has been loaded!");
        plugin = this;
    }


    // Plugin dependencies, everything external.
    public void loadDependencies() {
        projectKorra = ProjectKorra.plugin;
        worldEdit = WorldEdit.getInstance();
        if (Bukkit.getPluginManager().getPlugin("Multiverse-Core") != null) {
            multiverse = (MultiverseCore) Bukkit.getPluginManager().getPlugin("Multiverse-Core");
            log(Level.INFO, "Hooked into Multiverse!");
        }
    }


    // Managers that ease the use of Core classes and dependencies.
    public void loadManagers() {

        // SQL
        SQLManager = new MySQLManager(this);
        configM = new ConfigManager(this);
        playerM = new PlayerManager(this);
    }

    // Spigot events
    public void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new PBListener(), this);
    }

    // Spigot commands
    public void registerCommands() {
        getCommand("spectator").setExecutor(new SpectateCommand());
        getCommand("spectator").setTabCompleter(new SpectateCommand());
    }

    // onDisable
    public void disable() {
        log(Level.INFO, "ProBending " + plugin.getDescription().getVersion() + " has been disabled!");
        SQLManager.onDisable();
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
