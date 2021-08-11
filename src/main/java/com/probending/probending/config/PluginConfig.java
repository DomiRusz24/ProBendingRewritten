package com.probending.probending.config;

import com.probending.probending.ProBending;
import me.domirusz24.plugincore.config.configvalue.ConfigValue;

public class PluginConfig {

    private ConfigValue<Integer> levelY;

    public PluginConfig() {
        levelY = new ConfigValue<>("Arena.levelY", 14, ProBending.configM.getConfig());
        ProBending.configM.getConfig().save();
    }

    public int getYLevel() {
        return levelY.getValue();
    }
}
