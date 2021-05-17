package com.probending.probending.managers;

import com.probending.probending.ProBending;
import com.probending.probending.core.displayable.PBHologram;
import java.util.Collection;
import java.util.HashMap;

public class HologramManager extends PBManager {

    private final HashMap<String, PBHologram> HOLOGRAM_BY_ID = new HashMap<>();

    private Collection<PBHologram> getHolograms() {
        return HOLOGRAM_BY_ID.values();
    }

    public HologramManager(ProBending plugin) {
        super(plugin);
    }

    public void addHologram(PBHologram hologram) {
        HOLOGRAM_BY_ID.put(hologram.getID(), hologram);
    }

    public PBHologram getHologram(String ID) {
        return HOLOGRAM_BY_ID.getOrDefault(ID, null);
    }




}
