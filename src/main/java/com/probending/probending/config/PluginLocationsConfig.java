package com.probending.probending.config;

import com.probending.probending.ProBending;
import com.probending.probending.config.configvalue.AbstractConfigValue;
import com.probending.probending.config.configvalue.ConfigLocation;
import org.bukkit.Location;

public class PluginLocationsConfig extends AbstractConfig {

    private ConfigLocation spawn;

    public PluginLocationsConfig(String path, ProBending plugin) {
        super(path, plugin);
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
