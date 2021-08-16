package com.probending.probending.config;

import com.probending.probending.ProBending;
import com.probending.probending.core.arena.Arena;
import me.domirusz24.plugincore.config.AbstractConfig;
import me.domirusz24.plugincore.core.placeholders.PlaceholderObject;
import com.probending.probending.managers.PAPIManager;
import me.clip.placeholderapi.PlaceholderAPI;
import me.domirusz24.plugincore.managers.ConfigManager;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class CommandConfig extends AbstractConfig {

    public CommandConfig(String path, ProBending plugin, ConfigManager manager) {
        super(path, plugin, manager);
        save();
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

        ArenaStartPlayer("Player.ArenaStart", Arrays.asList("")),
        ArenaStartSingle("Single.ArenaStart", Arrays.asList("")),

        ArenaStopPlayer("Player.ArenaStop", Arrays.asList("")),
        ArenaStopSingle("Single.ArenaStop", Arrays.asList("")),

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

        private HashMap<YamlConfiguration, List<String>> commands = new HashMap<>();

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

        public void saveEmpty(YamlConfiguration config) {
            if (!config.contains(configPath)) {
                config.set(configPath, Arrays.asList(""));
            }
        }

        public void reload(YamlConfiguration config) {
            commands.put(config, config.getStringList(configPath));
            commands.put(config, commands.get(config).stream().filter(s -> !s.equals("")).collect(Collectors.toList()));
        }

        public void reload() {
            reload(ProBending.commandConfig);
        }

        public void run(YamlConfiguration file, Arena arena, Player... players) {
            run(file, players);
            run(arena.getCommandConfig(), players);
        }

        private void run(YamlConfiguration file, Player... players) {
            if (file.contains(configPath)) {
                List<Player> playerList = Arrays.asList(players);
                commands.get(file).forEach(c ->
                        playerList
                                .forEach(p ->
                                ProBending.plugin.getServer().dispatchCommand(
                                        ProBending.plugin.getServer().getConsoleSender(),
                                        PlaceholderAPI.setPlaceholders(p, c))));
            }
        }

        public void run(YamlConfiguration file, Arena arena, PlaceholderObject... object) {
            run(file, object);
            run(arena.getCommandConfig(), object);
        }

        private void run(YamlConfiguration file, PlaceholderObject... object) {
            if (file.contains(configPath)) {
                if (object == null) {
                    commands.get(file).forEach(c -> ProBending.plugin.getServer().dispatchCommand(ProBending.plugin.getServer().getConsoleSender(), c));
                } else {
                    for (String command : commands.get(file)) {
                        String com = command;
                        for (PlaceholderObject i : object) {
                            com = PAPIManager.setPlaceholders(i, com);
                        }
                        ProBending.plugin.getServer().dispatchCommand(ProBending.plugin.getServer().getConsoleSender(), com);
                    }
                }
            }
        }

        public void run(Arena arena, Player... players) {
            run(ProBending.commandConfig, arena, players);
        }

        public void run(Arena arena, PlaceholderObject... object) {
            run(ProBending.commandConfig, arena, object);
        }

        public void run(Arena arena) {
            run(ProBending.commandConfig, arena, arena);
        }
    }

}
