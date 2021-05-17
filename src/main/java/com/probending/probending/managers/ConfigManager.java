package com.probending.probending.managers;

import com.probending.probending.ProBending;
import com.probending.probending.config.CommandConfig;
import com.probending.probending.config.PluginConfig;
import com.probending.probending.config.LanguageConfig;
import com.probending.probending.config.PluginLocationsConfig;

import java.util.ArrayList;

public class ConfigManager extends PBManager {

    private PluginConfig config;

    private LanguageConfig languageConfig;

    private CommandConfig commandConfig;

    private PluginLocationsConfig locationsConfig;

    private ArrayList<Reloadable> reloadables = new ArrayList<>();

    public ConfigManager(ProBending plugin) {
        super(plugin);
        loadConfigs();
    }

    // Plugin load
    private void loadConfigs() {
        // Config
        config = new PluginConfig("config.yml", plugin);

        // Locations config
        locationsConfig = new PluginLocationsConfig("locations.yml", plugin);

        // Language config
        languageConfig = new LanguageConfig("language.yml", plugin);

        // Command Config
        commandConfig = new CommandConfig("commands.yml", plugin);
        commandConfig.save();
    }

    // Plugin reload
    public boolean reloadConfigs() {
        boolean success = true;
        boolean temp;
        temp = config.reload();
        if (!temp) {
            success = false;
        }
        temp = languageConfig.reload();
        if (!temp) {
            success = false;
        }
        temp = commandConfig.reload();
        if (!temp) {
            success = false;
        }
        temp = languageConfig.reload();
        if (!temp) {
            success = false;
        }
        if (success) {
            reloadables.forEach(Reloadable::onReload);
        }
        return success;
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


