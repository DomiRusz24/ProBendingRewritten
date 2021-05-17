package com.probending.probending.managers.database.values;

public abstract class DataBaseValue<T> {

    private final String name;
    private final T defaultValue;

    public DataBaseValue(String name, T defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
    }

    public abstract String getValue();

    public String getName() {
        return name;
    }

    public T getDefaultValue() {
        return defaultValue;
    }
}
