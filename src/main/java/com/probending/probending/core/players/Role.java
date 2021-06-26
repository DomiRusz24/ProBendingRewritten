package com.probending.probending.core.players;

import com.probending.probending.ProBending;
import com.probending.probending.managers.PlayerManager;

import java.util.HashMap;
import java.util.function.Supplier;

public class Role {

    private final String id;
    private final Supplier<String> name;
    private final Supplier<String> prefix;

    public Role(String id, Supplier<String> name, Supplier<String> prefix, PlayerManager manager) {
        this.id = id;
        this.name = name;
        this.prefix = prefix;
        manager.registerRole(this);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name.get();
    }

    public String getPrefix() {
        return prefix.get();
    }
}
