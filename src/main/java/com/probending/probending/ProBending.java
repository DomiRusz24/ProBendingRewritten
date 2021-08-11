package com.probending.probending;

import com.comphenix.protocol.ProtocolLibrary;
import com.onarandombox.MultiverseCore.MultiverseCore;
import com.probending.probending.core.players.PBPlayer;
import com.probending.probending.managers.*;
import com.projectkorra.projectkorra.ProjectKorra;
import me.domirusz24.plugincore.PluginCore;
import me.domirusz24.plugincore.core.players.PlayerData;
import me.domirusz24.plugincore.managers.PAPIManager;
import me.domirusz24.plugincore.managers.database.DataBaseTable;
import org.bukkit.Bukkit;

import java.util.UUID;

public final class ProBending extends PluginCore {

    // Plugin
    public static ProBending plugin;

    // Dependencies
    public static ProjectKorra projectKorra = null;


    // Managers
    public static PlayerManager playerM;
    public static TeamManager teamM;
    public static ArenaManager arenaM;
    public static ProjectKorraManager projectKorraM;
    public static CustomItemManager itemM;

    // onEnable
    @Override
    public void _initialize() {
        plugin = this;
    }


    // Plugin dependencies, everything external.
    @Override
    public void _loadDependencies() {

        // PK
        projectKorra = ProjectKorra.plugin;

        // Protocol
        protocol = ProtocolLibrary.getProtocolManager();

        // Multiverse
        multiverse = (MultiverseCore) hookInto("Multiverse-Core");

    }

    @Override
    protected String databasePrefix() {
        return "pb";
    }

    @Override
    public String packageName() {
        return "com.probending.probending";
    }

    @Override
    public DataBaseTable[] getTables() {
        return new DataBaseTable[0];
    }

    @Override
    protected PAPIManager papiManager() {
        return new com.probending.probending.managers.PAPIManager(this);
    }

    @Override
    protected void loadConfigs() {

    }

    @Override
    public void sqlLoad() {

    }

    // Managers that ease the use of Core classes and dependencies.
    @Override
    public void _loadManagers() {

        // Items
        itemM = new CustomItemManager(plugin);

        // Player
        playerM = new PlayerManager(plugin);

        // Team
        teamM = new TeamManager(plugin);

        // Arena
        arenaM = new ArenaManager(plugin);

        // ProjectKorra
        projectKorraM = new ProjectKorraManager(plugin);

    }

    @Override
    protected void _loadCommands() {

    }

    // Spigot events
    @Override
    public void _registerEvents() {
        Bukkit.getPluginManager().registerEvents(new PBListener(), this);
    }

    // Spigot commands
    public void registerCommands() {
    }

    // onDisable
    @Override
    public void _disable() {
        arenaM.ARENA_BY_NAME.values().forEach((a) -> {
            if (a.inGame()) {
                a.getActiveArena().forceUnstableStop();
            }
        });
        SqlM.onDisable();
    }

    @Override
    protected void _shutOffPlugin() {

    }

    @Override
    public PlayerData registerPlayer(String s, UUID uuid) {
        return new PBPlayer(s, uuid);
    }
}
