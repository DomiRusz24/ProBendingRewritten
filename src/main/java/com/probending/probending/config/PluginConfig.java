package com.probending.probending.config;

import com.probending.probending.ProBending;
import com.probending.probending.config.configvalue.AbstractConfigValue;
import com.probending.probending.config.configvalue.ConfigLocation;
import com.probending.probending.config.configvalue.ConfigValue;
import com.probending.probending.core.enums.GameType;
import org.bukkit.Location;

public class PluginConfig extends AbstractConfig {

    private ConfigValue<Integer> levelY = new ConfigValue<>("Arena.levelY", 14, this);
    private ConfigValue<Integer> tirednessMax = new ConfigValue<>("Arena.ActivePlayer.TirednessMax", 200, this);
    private ConfigValue<Integer> hpToTirednessRatio = new ConfigValue<>("Arena.ActivePlayer.HPToTirednessRatio", 4, this);

    public PluginConfig(String path, ProBending plugin) {
        super(path, plugin);
        GameType.saveDefaults(this);
        save();
    }

    public int getYLevel() {
        return levelY.getValue();
    }

    public int getTirednessMax() {
        return tirednessMax.getValue();
    }

    public int getHpToTirednessRatio() {
        return hpToTirednessRatio.getValue();
    }
}
