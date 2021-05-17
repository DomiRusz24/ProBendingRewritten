package com.probending.probending.config;

import com.probending.probending.ProBending;
import com.probending.probending.config.configvalue.AbstractConfigValue;
import com.probending.probending.core.PBRegion;
import com.probending.probending.core.displayable.PBSign;
import com.probending.probending.core.displayable.PBHologram;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

public abstract class AbstractConfig extends YamlConfiguration {

    private File file;

    private final String name;

    private final ProBending plugin;

    private final ArrayList<AbstractConfigValue<?>> values = new ArrayList<>();

    public AbstractConfig(String path, ProBending plugin) {
        this.plugin = plugin;
        this.file = new File(ProBending.plugin.getDataFolder(), path);
        this.name = file.getName();
        setUp();

    }

    public AbstractConfig(File file, ProBending plugin) {
        this.plugin = plugin;
        this.file = file;
        this.name = file.getName();
        setUp();
    }

    public void addValue(AbstractConfigValue<?> value) {
        values.add(value);
    }

    private void setUp() {
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
            plugin.log(Level.INFO, "Created file directory for " + file.getPath() + ".");
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException error) {
                error.printStackTrace();
                plugin.log(Level.SEVERE, "Couldn't create file " + file.getPath() + "! Shutting off plugin...");
                plugin.shutOffPlugin();
                return;
            } finally {
                plugin.log(Level.INFO, "Created file " + file.getPath() + ".");
            }
        }
        reload();
        options().copyDefaults(true);
    }

    public boolean reload() {
        try {
            load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            plugin.log(Level.WARNING, "Couldn't reload " + name + " config!");
            return false;
        }
        values.forEach(AbstractConfigValue::autoReload);
        return true;
    }

    public boolean save() {
        try {
            save(file);
        } catch (IOException e) {
            e.printStackTrace();
            plugin.log(Level.SEVERE, "Couldn't save " + name + " config!");
            return false;
        }
        return true;
    }

    @Override
    public void set(String path, Object object) {
        if (object instanceof Location) {
            setLocation(path, (Location) object);
        } else if (object instanceof PBHologram) {
            setHologram(path, (PBHologram) object);
        } else if (object instanceof PBSign) {
            setSign(path, (PBSign) object);
        } else {
            super.set(path, object);
        }
    }

    public void setLocation(String path, Location location) {
        set(path + ".x", Math.floor(location.getX()));
        set(path + ".y", Math.floor(location.getY()));
        set(path + ".z", Math.floor(location.getZ()));
        set(path + ".yaw", location.getYaw());
        set(path + ".pitch", location.getPitch());
        set(path + ".world", location.getWorld().getName());
    }

    public void setHologram(String path, PBHologram hologram) {
        setLocation(path, hologram.getLocation());
        set(path + ".visible", hologram.isVisible());
    }

    public PBHologram getHologram(String path, String ID) {
        PBHologram hologram = ProBending.hologramM.getHologram(ID);
        if (hologram == null) {
            Location location = getLocation(path);
            if (location != null) {
                boolean visible = getBoolean(path + ".visible");
                hologram = new PBHologram(ID);
                hologram.teleport(location);
                hologram.setVisible(visible);
            } else {
                hologram = new PBHologram(ID);
            }
        }
        return hologram;
    }

    public void setSign(String path, PBSign hologram) {
        setLocation(path, hologram.getLocation());
    }

    public PBSign getSign(String path, String ID) {
        PBSign sign = ProBending.signM.getSign(ID);
        if (sign == null) {
            sign = new PBSign(ID);
            Location location = getLocation(path);
            if (location != null) {
                Block block = location.getBlock();
                if (block.getType().equals(Material.WALL_SIGN) || block.getType().equals(Material.SIGN_POST)) {
                    sign.setSign((Sign) block.getState());
                }
            }
        }
        return sign;
    }

    public void setRegion(String path, PBRegion region) {
        if (region.getMin() != null) {
            setWESelection(path, region.getMin(), region.getMax());
        }
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

    public void setWESelection(String path, Location min, Location max) {
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

    public File getFile() {
        return file;
    }

    public ProBending getPlugin() {
        return plugin;
    }
}
