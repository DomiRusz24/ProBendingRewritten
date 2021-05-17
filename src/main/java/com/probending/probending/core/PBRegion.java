package com.probending.probending.core;

import com.probending.probending.ProBending;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashSet;

public abstract class PBRegion {

    private final String ID;

    protected final HashSet<Player> players = new HashSet<>();

    protected final HashSet<Player> forcePlayers = new HashSet<>();

    private World world;

    private Location min, max;

    private double x1, x2, y1, y2, z1, z2;

    private boolean enabled = true;

    public PBRegion(String ID) {
        this.ID = ID;
        ProBending.regionM.addRegion(this);
    }

    protected abstract void onPlayerEnter(Player player);

    protected abstract void onPlayerLeave(Player player);

    protected abstract void onPlayerForceLeave(Player player);

    protected abstract void _onLeave(Player player);

    private final HashSet<Player> tempList = new HashSet<>();

    public void forceRemove(Player player) {
        if (players.contains(player)) {
            forcePlayers.add(player);
            onPlayerForceLeave(player);
        }
    }

    public void forceRemoveAll() {
        players.forEach(this::forceRemove);
    }

    public void onLeave(Player player) {
        if (players.contains(player)) {
            players.remove(player);
            if (!forcePlayers.contains(player)) {
                _onLeave(player);
            } else {
                forcePlayers.remove(player);
            }
        }
    }

    public void updatePlayers() {
        if (isEnabled()) {
            if (isSetUp()) {
                tempList.clear();
                tempList.addAll(players);
                players.clear();
                double x;
                double y;
                double z;
                for (Player player : world.getPlayers()) {
                    x = player.getLocation().getBlockX();
                    y = player.getLocation().getBlockY();
                    z = player.getLocation().getBlockZ();
                    if (x >= x1 && x2 >= x && y >= y1 && y2 >= y && z >= z1 && z2 >= z) {
                        if (!tempList.contains(player)) {
                            onPlayerEnter(player);
                        } else {
                            tempList.remove(player);
                        }
                        players.add(player);
                    }
                }
                tempList.forEach(p -> {
                    if (!forcePlayers.contains(p)) {
                        onPlayerLeave(p);
                    } else {
                        forcePlayers.remove(p);
                    }
                });
            }
        }
    }

    public boolean isSetUp() {
        return world != null;
    }

    public HashSet<Player> getPlayers() {
        return new HashSet<>(players);
    }

    public void setLocations(Location min, Location max) {
        if (min != null && max != null && min.getWorld().equals(max.getWorld())) {
            this.world = min.getWorld();
            x1 = Math.min(min.getX(), max.getX());
            x2 = Math.max(min.getX(), max.getX());
            y1 = Math.min(min.getY(), max.getY());
            y2 = Math.max(min.getY(), max.getY());
            z1 = Math.min(min.getZ(), max.getZ());
            z2 = Math.max(min.getZ(), max.getZ());
            this.min = new Location(world, x1, y1, z1);
            this.max = new Location(world, x2, y2, z2);
            updatePlayers();
        }
    }

    public Location getMin() {
        return min;
    }

    public Location getMax() {
        return max;
    }

    public World getWorld() {
        return world;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (enabled) {
            updatePlayers();
        } else {
            forceRemoveAll();
            players.clear();
            forcePlayers.clear();
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getID() {
        return ID;
    }
}
