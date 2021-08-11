package com.probending.probending.config;

import com.probending.probending.ProBending;

import java.io.File;

public class SpigotCommandsConfig extends AbstractConfig {

    public SpigotCommandsConfig(String path, ProBending plugin, ConfigManager manager) {
        super(path, plugin, manager);
    }

    public SpigotCommandsConfig(String path, ProBending plugin) {
        super(path, plugin);
    }

    public SpigotCommandsConfig(File file, ProBending plugin) {
        super(file, plugin);
    }
}
