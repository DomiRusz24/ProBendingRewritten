package com.probending.probending;

import com.probending.probending.managers.ConfigManager;
import com.projectkorra.projectkorra.ProjectKorra;
import com.sk89q.worldedit.WorldEdit;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class ProBending extends JavaPlugin {

    // Plugin
    public static ProBending plugin;

    // Dependencies
    public static ProjectKorra projectKorra;
    public static WorldEdit worldEdit;


    // Managers
    public static ConfigManager configM;

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

    public void initialize() {
        log(Level.INFO, "ProBending " + plugin.getDescription().getVersion() + " has been loaded!");
        plugin = this;
    }

    public void loadDependencies() {
        projectKorra = ProjectKorra.plugin;
        worldEdit = WorldEdit.getInstance();
    }

    public void loadManagers() {
        configM = new ConfigManager(this);
    }


    public void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new PBListener(), this);
    }

    public void registerCommands() {

    }

    public void disable() {
        log(Level.INFO, "ProBending " + plugin.getDescription().getVersion() + " has been disabled!");
    }

    public void log(Level level, String msg) {
        this.getLogger().log(level, msg);
    }

    public void shutOffPlugin() {
        log(Level.SEVERE, "The plugin has been disabled due to a critical error!");
        setEnabled(false);
    }
}
