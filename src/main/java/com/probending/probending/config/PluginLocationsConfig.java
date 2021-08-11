package com.probending.probending.config;

import com.probending.probending.ProBending;
import me.domirusz24.plugincore.config.AbstractConfig;
import me.domirusz24.plugincore.config.configvalue.ConfigLocation;
import me.domirusz24.plugincore.managers.ConfigManager;
import org.bukkit.Location;

public class PluginLocationsConfig extends AbstractConfig {

    private ConfigLocation spawn;

    public PluginLocationsConfig(String path, ProBending plugin, ConfigManager manager) {
        super(path, plugin, manager);
        spawn = new ConfigLocation("Arena.spawn", this);
    }

    public void setSpawn(Location location) {
        spawn.setValue(location);
        save();
    }

    public Location getSpawn() {
        return spawn.getValue();
    }
}
