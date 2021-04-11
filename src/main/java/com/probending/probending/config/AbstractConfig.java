package com.probending.probending.config;

import com.probending.probending.ProBending;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.graalvm.compiler.core.common.type.ArithmeticOpTable;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public abstract class AbstractConfig extends YamlConfiguration {

    private File file;

    private String name;

    private ProBending plugin;

    public AbstractConfig(String path, ProBending plugin) {
        this.plugin = plugin;
        file = new File(ProBending.plugin.getDataFolder(), path);
        if (!file.getParentFile().exists()) {
            file.mkdir();
            plugin.log(Level.INFO, "Created file directory for " + path + ".");
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException error) {
                error.printStackTrace();
                plugin.log(Level.SEVERE, "Couldn't create file " + path + "! Shutting off plugin...");
                plugin.shutOffPlugin();
            } finally {
                plugin.log(Level.INFO, "Created file " + path + ".");
                name = file.getName();
                options.copyDefaults(true);
                reload();
            }
        }
    }

    public void reload() {
        try {
            load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            plugin.log(Level.WARNING, "Couldn't reload " + name + " config!");
        }
    }

    public void save() {
        try {
            save(file);
        } catch (IOException e) {
            e.printStackTrace();
            plugin.log(Level.SEVERE, "Couldn't save " + name + " config!");
        }
    }
}
