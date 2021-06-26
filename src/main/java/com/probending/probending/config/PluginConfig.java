package com.probending.probending.config;

import com.probending.probending.ProBending;
import com.probending.probending.config.configvalue.ConfigValue;
import com.probending.probending.managers.ConfigManager;

public class PluginConfig extends AbstractConfig {

    private ConfigValue<Integer> levelY = new ConfigValue<>("Arena.levelY", 14, this);

    public PluginConfig(String path, ProBending plugin, ConfigManager manager) {
        super(path, plugin, manager);
        save();
    }

    public int getYLevel() {
        return levelY.getValue();
    }
}
