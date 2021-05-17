package com.probending.probending.config;

import com.probending.probending.ProBending;
import com.probending.probending.core.PBRegion;
import com.probending.probending.core.arena.Arena;
import com.probending.probending.core.arena.prearena.ArenaGetterRegion;
import com.probending.probending.core.arena.prearena.PreArena;
import com.probending.probending.core.enums.Ring;
import com.probending.probending.core.enums.TeamTag;
import com.sk89q.worldedit.*;
import javafx.util.Pair;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

import java.io.*;

public class ArenaConfig extends AbstractConfig {

    private Arena arena;

    private String path;

    public ArenaConfig(Arena arena, String path, ProBending plugin) {
        super(path, plugin);
        this.arena = arena;
        this.path = "Arena.";
        set(this.path + "name", arena.getName());
    }

    public ArenaConfig(Arena arena, File config, ProBending plugin) {
        super(config, plugin);
        this.arena = arena;
        this.path = "Arena.";
        set(this.path + "name", arena.getName());
    }

    // --- Center ---

    public Location getCenter() {
        return getLocation(path + "center");
    }

    public void setCenter(Location location) {
        setLocation(path + "center", location);
    }

    // --- Ring locations ---

    public Location getRingLocation(Ring ring) {
        return getLocation(path + ring.name());
    }

    public void setRingLocation(Ring ring, Location location) {
        setLocation(path + ring.name(), location);
    }

    // --- Starting locations ---

    public Location getStartingLocations(TeamTag tag, int player) {
        return getLocation(path + tag.name() + ".spawn" + player);
    }

    public void setStartingLocations(TeamTag tag, int player, Location location) {
        setLocation(path + tag.name() + ".spawn" + player, location);
    }

    // --- Rollback location ---

    public Location getRollbackLocation() {
        return getLocation(path + "rollbackLocation");
    }

    public void setRollbackLocation(Location location) {
        setLocation(path + "rollbackLocation", location);
    }

    // --- Rollback ---

    public void setRollback(CommandSender sender, Location min, Location max) {
        ProBending.schematicM.saveSchematic(sender, min, max, getFile().getParentFile(), "rollback");
    }

    public boolean getRollback(CommandSender sender, Location location) {
        return ProBending.schematicM.getSchematic(sender, location, getFile().getParentFile(), "rollback");
    }

    // --- Name ---

    public void setName(String name) {
        set(path + "name", name);
    }

    public String getName() {
        return getString(path + "name");
    }

    // --- Region ---

    public void setRegion(String path, ArenaGetterRegion region) {
        super.setRegion(path, region);
        if (region.getCenter() != null) {
            setLocation(path + ".center", region.getCenter());
        }
    }

    public ArenaGetterRegion getRegion(String path, String ID, TeamTag tag, PreArena preArena) {
        ArenaGetterRegion region = (ArenaGetterRegion) ProBending.regionM.getRegion(ID);
        if (region == null) {
            region = new ArenaGetterRegion(ID, preArena, tag);
            Location[] sel = getWESelection(path);
            Location center = getLocation(path + ".center");
            if (sel != null) {
                region.setLocations(sel[0], sel[1]);
            }
            if (center != null) {
                region.setCenter(center);
            }
        }
        return region;
    }

}
