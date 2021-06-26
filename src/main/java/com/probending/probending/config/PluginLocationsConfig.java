package com.probending.probending.config;

import com.probending.probending.ProBending;
import com.probending.probending.config.configvalue.AbstractConfigValue;
import com.probending.probending.config.configvalue.ConfigLocation;
import com.probending.probending.managers.ConfigManager;
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
