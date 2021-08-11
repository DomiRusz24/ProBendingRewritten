package com.probending.probending.config.arena;

import com.probending.probending.ProBending;
import com.probending.probending.config.configvalue.ConfigLocation;
import com.probending.probending.core.arena.Arena;
import com.probending.probending.core.arena.SpectateGetterRegion;
import com.probending.probending.core.arena.prearena.ArenaGetterRegion;
import com.probending.probending.core.arena.prearena.PreArena;
import com.probending.probending.core.enums.Ring;
import com.probending.probending.core.enums.TeamTag;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

import java.io.*;

public class ArenaLocationConfig extends AbstractArenaConfig {

    private String path;

    public ArenaLocationConfig(Arena arena, String path, ProBending plugin) {
        super(arena, path, plugin);
        this.path = "Arena.";
        set(this.path + "name", arena.getName());
    }

    public ArenaLocationConfig(Arena arena, File config, ProBending plugin) {
        super(arena, config, plugin);
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

    private final ConfigLocation spectatorCenter = new ConfigLocation("SpectateGetter.Center", this);

    public void setSpectatorSpawn(Location location) {
        spectatorCenter.setValue(location);
    }

    public Location getSpectatorSpawn() {
        return spectatorCenter.getValue();
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
        if (location == null) {
            return false;
        }
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
        if (region.getRegionCenter() != null) {
            setLocation(path + ".regionCenter", region.getRegionCenter());
        }
    }

    public void setRegion(SpectateGetterRegion region) {
        super.setRegion("SpectateGetter.Region", region);

    }

    public ArenaGetterRegion getRegion(String path, String ID, TeamTag tag, PreArena preArena) {
        ArenaGetterRegion region = (ArenaGetterRegion) ProBending.regionM.getRegion(ID);
        if (region == null) {
            region = new ArenaGetterRegion(ID, preArena, tag);
            Location[] sel = getWESelection(path);
            Location center = getLocation(path + ".center");
            Location regionCenter = getLocation(path + ".regionCenter");
            if (sel != null) {
                region.setLocations(sel[0], sel[1]);
            }
            if (center != null) {
                region.setCenter(center);
            }
            if (regionCenter != null) {
                region.setRegionCenter(regionCenter);
            }
        }
        return region;
    }

    public SpectateGetterRegion getRegion(String path, String ID) {
        SpectateGetterRegion region = (SpectateGetterRegion) ProBending.regionM.getRegion(ID);
        if (region == null) {
            region = new SpectateGetterRegion(ID, getArena());
            Location[] sel = getWESelection(path);
            if (sel != null) {
                region.setLocations(sel[0], sel[1]);
            }
        }
        return region;
    }

}
