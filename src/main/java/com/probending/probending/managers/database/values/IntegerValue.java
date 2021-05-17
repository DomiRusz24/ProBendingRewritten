package com.probending.probending.managers.database.values;

public class IntegerValue extends DataBaseValue<Integer> {
    public IntegerValue(String name) {
        super(name, 0);
    }

    @Override
    public String getValue() {
        return "INT";
    }
}
