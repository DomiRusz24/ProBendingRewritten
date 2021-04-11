package com.probending.probending.config;

import com.probending.probending.ProBending;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;
import org.graalvm.compiler.core.common.type.ArithmeticOpTable;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public abstract class AbstractConfig extends YamlConfiguration {

    // TODO: Language annotation config

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

    public void saveLocation(String path, Location location) {
        set(path + ".x", Math.floor(location.getX()));
        set(path + ".y", Math.floor(location.getY()));
        set(path + ".z", Math.floor(location.getZ()));
        set(path + ".yaw", location.getYaw());
        set(path + ".pitch", location.getPitch());
        set(path + ".world", location.getWorld().getName());
    }

    public Location getLocation(String path) {
        if (!contains(path + ".world")) {
            plugin.log(Level.WARNING, path + ".world does not exist!");
            return null;
        }
        double x = getInt(path + ".x");
        double y = getInt(path + ".y");
        double z = getInt(path + ".z");
        double yaw = getDouble(path + ".yaw");
        double pitch = getDouble(path + ".pitch");
        String w = getString(path + ".world");
        World world = getWorld(w);
        if (world == null) {
            plugin.log(Level.WARNING, "World \"" + w + "\" doesn't exist!");
            return null;
        }
        return new Location(world, x + 0.5, y, z + 0.5, (float) yaw, (float) pitch);
    }

    public void saveWESelection(String path, Location min, Location max) {
        if (min == null || max == null) {
            return;
        }
        set(path + ".world", min.getWorld().getName());
        for (int i = 0; i <= 1; i++) {
            Location location = i == 0 ? min : max;
            String minmax = i == 0 ? ".min" : ".max";
            set(path + minmax + ".x", Math.floor(location.getX()));
            set(path + minmax + ".y", Math.floor(location.getY()));
            set(path + minmax + ".z", Math.floor(location.getZ()));
        }
    }

    public Location[] getWESelection(String path) {
        if (!contains(path + ".world")) {
            return null;
        }
        Location min = new Location(plugin.getServer().getWorlds().get(0), 0, 0, 0);
        Location max = new Location(plugin.getServer().getWorlds().get(0), 0, 0, 0);
        String w = getString(path + ".world");
        World world = getWorld(w);
        for (int i = 0; i <= 1; i++) {
            String minmax = i == 0 ? ".min" : ".max";
            double x = getInt(path + minmax + ".x");
            double y = getInt(path + minmax + ".y");
            double z = getInt(path + minmax + ".z");
            if (i == 0) {
                min = new Location(world, x, y, z);
            } else {
                max = new Location(world, x, y, z);
            }
        }
        return new Location[]{min, max};
    }

    private World getWorld(String name) {
        World world = Bukkit.getWorld(name);
        if (world == null && ProBending.multiverse != null) {
            world = ProBending.multiverse.getMVWorldManager().getMVWorld(name).getCBWorld();
        }
        return world;
    }
}
