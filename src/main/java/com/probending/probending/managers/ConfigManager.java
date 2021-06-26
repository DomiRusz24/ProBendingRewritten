package com.probending.probending.managers;

import com.probending.probending.ProBending;
import com.probending.probending.config.*;

import java.util.ArrayList;
import java.util.List;

public class ConfigManager extends PBManager {

    private PluginConfig config;

    private LanguageConfig languageConfig;

    private CommandConfig commandConfig;

    private PluginLocationsConfig locationsConfig;

    private SpigotCommandsConfig spigotCommandsConfig;

    private List<AbstractConfig> configs = new ArrayList<>();

    private List<Reloadable> reloadables = new ArrayList<>();

    public ConfigManager(ProBending plugin) {
        super(plugin);
        loadConfigs();
    }

    // Plugin load
    private void loadConfigs() {
        // Config
        config = new PluginConfig("config.yml", plugin, this);

        // Locations config
        locationsConfig = new PluginLocationsConfig("locations.yml", plugin, this);

        // Language config
        languageConfig = new LanguageConfig("language.yml", plugin, this);

        // Command Config
        commandConfig = new CommandConfig("commands.yml", plugin, this);
        commandConfig.save();

        spigotCommandsConfig = new SpigotCommandsConfig("spigotCommands.yml", plugin, this);
    }

    // Plugin reload
    public boolean reloadConfigs() {
        boolean success = true;
        boolean temp;
        for (AbstractConfig config : configs) {
            temp = config.reload();
            if (!temp) {
                success = false;
            }
        }
        if (success) {
            reloadables.forEach(Reloadable::onReload);
        }
        return success;
    }

    public void registerConfig(AbstractConfig config) {
        configs.add(config);
    }

    public PluginConfig getConfig() {
        return config;
    }

    public LanguageConfig getLanguageConfig() {
        return languageConfig;
    }

    public CommandConfig getCommandConfig() {
        return commandConfig;
    }

    public PluginLocationsConfig getLocationsConfig() {
        return locationsConfig;
    }

    public SpigotCommandsConfig getSpigotCommandsConfig() {
        return spigotCommandsConfig;
    }

    public void registerReloadable(Reloadable reloadable) {
        reloadables.add(reloadable);
    }

    public interface Reloadable {
        default void registerReloadable() {
            ProBending.configM.registerReloadable(this);
        }

        void onReload();
    }
}


