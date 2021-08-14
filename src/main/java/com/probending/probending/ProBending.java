package com.probending.probending;

import com.comphenix.protocol.ProtocolLibrary;
import com.onarandombox.MultiverseCore.MultiverseCore;
import com.probending.probending.command.arena.ArenaConfigCommand;
import com.probending.probending.command.arena.ArenaControlCommand;
import com.probending.probending.command.arena.arenaconfig.ArenaGetCommand;
import com.probending.probending.command.arena.arenaconfig.ArenaSetCommand;
import com.probending.probending.command.arena.arenacontrol.*;
import com.probending.probending.command.base.ProBendingCommand;
import com.probending.probending.command.base.probending.CreateArenaCommand;
import com.probending.probending.command.base.probending.RulesCommand;
import com.probending.probending.command.base.probending.SetSpawnCommand;
import com.probending.probending.command.forceskip.ForceStartCommand;
import com.probending.probending.command.leave.LeaveCommand;
import com.probending.probending.command.pblevel.PBLevelCommand;
import com.probending.probending.command.pbteam.PBTeamCommand;
import com.probending.probending.command.pbteam.pbteam.*;
import com.probending.probending.config.CommandConfig;
import com.probending.probending.config.PluginConfig;
import com.probending.probending.config.PluginLocationsConfig;
import com.probending.probending.core.players.PBPlayer;
import com.probending.probending.managers.*;
import com.probending.probending.managers.database.PlayerDataTable;
import com.probending.probending.managers.database.TeamDataTable;
import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.util.TempBlock;
import me.domirusz24.plugincore.PluginCore;
import me.domirusz24.plugincore.command.ReloadSubCommand;
import me.domirusz24.plugincore.core.players.PlayerData;
import me.domirusz24.plugincore.managers.PAPIManager;
import me.domirusz24.plugincore.managers.database.DataBaseTable;
import me.domirusz24.plugincore.managers.database.values.DataBaseValue;
import net.bytebuddy.build.Plugin;
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

    public static PlayerDataTable playerTable;
    public static TeamDataTable teamTable;

    public static PluginLocationsConfig locationConfig;
    public static PluginConfig pluginConfig;
    public static CommandConfig commandConfig;

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
        playerTable = new PlayerDataTable();
        teamTable = new TeamDataTable();
        return new DataBaseTable[] {
                playerTable,
                teamTable
        };
    }

    @Override
    protected PAPIManager papiManager() {
        return new com.probending.probending.managers.PAPIManager(this);
    }

    @Override
    protected void loadConfigs() {
        locationConfig = new PluginLocationsConfig("locations.yml", plugin, configM);
        pluginConfig = new PluginConfig();
        commandConfig = new CommandConfig("commands.yml", plugin, configM);
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
        commandM.registerCommand(new ArenaControlCommand()
                .addSubCommand(new ArenaJoinCommand())
                .addSubCommand(new ArenaAddCommand())
                .addSubCommand(new ArenaRemoveCommand())
                .addSubCommand(new ArenaStartCommand())
                .addSubCommand(new ArenaStopCommand())
                .addSubCommand(new ArenaSpectateCommand())
        );

        commandM.registerCommand(new ArenaConfigCommand()
                .addSubCommand(new ArenaGetCommand())
                .addSubCommand(new ArenaSetCommand())
                .addSubCommand(new ArenaRefreshCommand())
        );

        commandM.registerCommand(new ProBendingCommand()
                .addSubCommand(new ReloadSubCommand())
                .addSubCommand(new RulesCommand())
                .addSubCommand(new CreateArenaCommand())
                .addSubCommand(new SetSpawnCommand())
        );

        commandM.registerCommand(new LeaveCommand());

        commandM.registerCommand(new PBLevelCommand());

        commandM.registerCommand(new ForceStartCommand());

        commandM.registerCommand(new PBTeamCommand()
                .addSubCommand(new CreateTeamCommand())
                .addSubCommand(new InviteTeamCommand())
                .addSubCommand(new TeamAcceptCommand())
                .addSubCommand(new TeamLeaveCommand())
                .addSubCommand(new TeamPlayCommand())
        );
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
