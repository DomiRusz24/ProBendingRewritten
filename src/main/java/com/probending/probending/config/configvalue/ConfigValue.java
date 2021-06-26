package com.probending.probending.config.configvalue;

import com.probending.probending.config.AbstractConfig;

public class ConfigValue<T> extends AbstractConfigValue<T> {
    public ConfigValue(String path, T defaultValue, AbstractConfig config) {
        super(path, defaultValue, config);
    }

    public ConfigValue(String path, T defaultValue, AbstractConfig config, boolean autoReload) {
        super(path, defaultValue, config, autoReload);
    }

    @Override
    public void _setValue(T value) {
        getConfig().set(getPath(), value);
    }

    @Override
    public void _setDefaultValue(T value) {
        getConfig().addDefault(getPath(), value);
    }

    @Override
    protected T getConfigValue() {
        return (T) getConfig().get(getPath(), defaultValue);
    }
}
