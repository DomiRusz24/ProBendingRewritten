package com.probending.probending.config;

import com.probending.probending.ProBending;
import com.probending.probending.core.interfaces.PlaceholderObject;
import com.probending.probending.managers.PAPIManager;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CommandConfig extends AbstractConfig {


    public CommandConfig(String path, ProBending plugin) {
        super(path, plugin);
        for (Commands value : Commands.values()) {
            value.reload(this);
        }
    }

    @Override
    public boolean save() {
        for (Commands value : Commands.values()) {
            value.saveDefault(this);
        }
        return super.save();
    }

    @Override
    public boolean reload() {
        boolean bool = super.reload();
        if (bool) {
            for (Commands value : Commands.values()) {
                value.reload(this);
            }
        }
        return bool;
    }

    public enum Commands {

        ArenaStartPlayer("Player.ArenaStart", Arrays.asList("msg %player_name% hello!")),
        ArenaStartSingle("Single.ArenaStart", Arrays.asList("broadcast %arena_name% has started!")),

        ArenaStopPlayer("Player.ArenaStop", Arrays.asList("msg %player_name% hello!")),
        ArenaStopSingle("Single.ArenaStop", Arrays.asList("broadcast %arena_name% has ended! Result: %arena_winningteam%")),

        ArenaWinPlayer("Player.Game.Win", Arrays.asList("")),
        ArenaLosePlayer("Player.Game.Lose", Arrays.asList("")),

        RoundPlayerGainStage("Player.GainStage", Arrays.asList("")),
        RoundPlayerLoseStage("Player.LoseStage", Arrays.asList("")),

        ArenaRoundWinPlayer("Player.Round.Win", Arrays.asList("")),
        ArenaRoundLosePlayer("Player.Round.Lose", Arrays.asList("")),

        ArenaPlayerDiePlayer("Player.KnockOut", Arrays.asList("")),
        ArenaPlayerKillPlayer("Player.KillOther", Arrays.asList("")),

        ArenaSpectatorJoinPlayer("Player.SpectatorJoin", Arrays.asList("")),
        ArenaSpectatorQuitPlayer("Player.SpectatorLeave", Arrays.asList(""));

        private final String configPath;

        private final List<String> defaultCommands;

        private List<String> commands;

        Commands(String configPath, List<String> defaultCommands) {
            this.configPath = configPath;
            this.defaultCommands = defaultCommands;
        }

        public List<String> getDefaultCommands() {
            return defaultCommands;
        }

        public String getConfigPath() {
            return configPath;
        }

        public void saveDefault(YamlConfiguration config) {
            if (!config.contains(configPath)) {
                config.set(configPath, defaultCommands);
            }
        }

        public void reload(YamlConfiguration config) {
            commands = config.getStringList(configPath);
            commands = commands.stream().filter(s -> !s.equals("")).collect(Collectors.toList());
        }

        public void reload() {
            reload(ProBending.configM.getCommandConfig());
        }

        public void run(YamlConfiguration file, Player... players) {
            if (file.contains(configPath)) {
                List<Player> playerList = Arrays.asList(players);
                commands.forEach(c -> playerList.forEach(p -> ProBending.plugin.getServer().dispatchCommand(ProBending.plugin.getServer().getConsoleSender(), PlaceholderAPI.setPlaceholders(p, c))));
            }
        }

        public void run(YamlConfiguration file, PlaceholderObject... object) {
            if (file.contains(configPath)) {
                if (object == null) {
                    commands.forEach(c -> ProBending.plugin.getServer().dispatchCommand(ProBending.plugin.getServer().getConsoleSender(), c));
                } else {
                    for (String command : commands) {
                        String com = command;
                        for (PlaceholderObject i : object) {
                            com = PAPIManager.setPlaceholders(i, com);
                        }
                        ProBending.plugin.getServer().dispatchCommand(ProBending.plugin.getServer().getConsoleSender(), com);
                    }
                }
            }
        }

        public void run(Player... players) {
            run(ProBending.configM.getCommandConfig(), players);
        }

        public void run(PlaceholderObject... object) {
            run(ProBending.configM.getCommandConfig(), object);
        }

        public void run() {
            run(ProBending.configM.getCommandConfig(), (PlaceholderObject) null);
        }
    }

}
