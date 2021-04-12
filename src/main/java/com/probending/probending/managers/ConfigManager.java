package com.probending.probending.managers;

import com.probending.probending.ProBending;
import com.probending.probending.config.Config;
import com.probending.probending.config.LanguageConfig;

public class ConfigManager {

    private ProBending plugin;

    private Config config;

    private LanguageConfig languageConfig;

    public ConfigManager(ProBending plugin) {
        this.plugin = plugin;
        loadConfigs();
    }

    // Plugin load
    private void loadConfigs() {
        // Config
        config = new Config("config.yml", plugin);

        // Language config
        languageConfig = new LanguageConfig("language.yml", plugin);

    }

    // Plugin reload
    public void reloadConfigs() {
        config.reload();
        languageConfig.reload();
    }

    public Config getConfig() {
        return config;
    }

    public LanguageConfig getLanguageConfig() {
        return languageConfig;
    }
}
