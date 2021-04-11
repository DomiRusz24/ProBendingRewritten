package com.probending.probending.managers;

import com.probending.probending.ProBending;
import com.probending.probending.config.Config;

public class ConfigManager {

    private ProBending plugin;

    private Config config;

    public ConfigManager(ProBending plugin) {
        this.plugin = plugin;
        loadConfigs();
    }

    private void loadConfigs() {

        // Config
        config = new Config("config.yml", plugin);
        config.addDefault("test", "test");

    }

    public Config getConfig() {
        return config;
    }
}
