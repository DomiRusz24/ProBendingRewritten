package com.probending.probending.config.configvalue;


import com.probending.probending.config.AbstractConfig;
import org.bukkit.Location;

public abstract class AbstractConfigValue<T> {

    private T value;

    protected final T defaultValue;

    private final AbstractConfig config;

    private final String path;

    private final boolean autoReload;

    public AbstractConfigValue(String path, T defaultValue, AbstractConfig config) {
        this.config = config;
        this.path = path;
        this.defaultValue = defaultValue;
        if (defaultValue != null) {
            _setDefaultValue(defaultValue);
        }
        value = getConfigValue();
        this.autoReload = true;
        config.addValue(this);
    }

    public AbstractConfigValue(String path, T defaultValue, AbstractConfig config, boolean autoReload) {
        this.config = config;
        this.path = path;
        this.defaultValue = defaultValue;
        if (defaultValue != null) {
            config.addDefault(getPath(), defaultValue);
        }
        value = getConfigValue();
        this.autoReload = autoReload;
        config.addValue(this);
    }

    public AbstractConfig getConfig() {
        return config;
    }

    public String getPath() {
        return path;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
        _setValue(value);
    }

    public void saveDefault() {
        if (defaultValue != null) {
            config.addDefault(getPath(), defaultValue);
        }
    }

    public abstract void _setValue(T value);

    public abstract void _setDefaultValue(T value);

    protected abstract T getConfigValue();

    public void reload() {
        value = getConfigValue();
    }

    public void autoReload() {
        if (autoReload) value = (T) config.get(getPath(), defaultValue);
    }
}
