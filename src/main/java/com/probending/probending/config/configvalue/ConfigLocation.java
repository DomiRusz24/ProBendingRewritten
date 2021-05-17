package com.probending.probending.config.configvalue;

import com.probending.probending.config.AbstractConfig;
import org.bukkit.Location;

public class ConfigLocation extends AbstractConfigValue<Location> {

    public ConfigLocation(String path, AbstractConfig config) {
        super(path, null, config, false);
    }

    @Override
    public void setValue(Location value) {
        getConfig().setLocation(getPath(), value);
    }

    @Override
    public void setDefaultValue(Location value) {}

    @Override
    protected Location getConfigValue() {
        return getConfig().getLocation(getPath());
    }
}
