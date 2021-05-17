package com.probending.probending.managers;

import com.probending.probending.ProBending;

public class PBManager {

    protected ProBending plugin;

    public PBManager(ProBending plugin) {
        this.plugin = plugin;
    }

    public ProBending getPlugin() {
        return plugin;
    }
}
