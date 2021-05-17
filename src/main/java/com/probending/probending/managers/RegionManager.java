package com.probending.probending.managers;

import com.probending.probending.ProBending;
import com.probending.probending.core.PBRegion;
import com.probending.probending.core.arena.ActiveArena;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;
import java.util.HashMap;

public class RegionManager extends PBManager {

    private final HashMap<String, PBRegion> Region_BY_ID = new HashMap<>();

    public Collection<PBRegion> getRegions() {
        return Region_BY_ID.values();
    }

    public RegionManager(ProBending plugin) {
        super(plugin);
        new BukkitRunnable() {
            @Override
            public void run() {
                getRegions().forEach(PBRegion::updatePlayers);
            }
        }.runTaskTimer(plugin, 10, 2);
    }

    public void addRegion(PBRegion Region) {
        Region_BY_ID.put(Region.getID(), Region);
    }

    public PBRegion getRegion(String ID) {
        return Region_BY_ID.getOrDefault(ID, null);
    }
}
