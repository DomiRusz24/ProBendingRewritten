package com.probending.probending.managers;

import com.probending.probending.ProBending;
import com.probending.probending.core.ActivePlayer;
import com.probending.probending.core.PBPlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class PlayerManager {

    private static final HashSet<PBPlayer> PB_PLAYERS = new HashSet<>();

    private static final HashSet<ActivePlayer> ACTIVE_PLAYERS = new HashSet<>();

    private ProBending plugin;

    public PlayerManager(ProBending plugin) {
        this.plugin = plugin;
    }

    // --- PBPlayer ---

    public PBPlayer getPlayer(UUID uuid) {
        for (PBPlayer p : PB_PLAYERS) {
            if (p.getUuid().equals(uuid.toString())) {
                return p;
            }
        }
        return null;
    }

    public void addPBPlayer(PBPlayer player) {
        PB_PLAYERS.add(player);
    }

    public void removePBPlayer(PBPlayer player) {
        PB_PLAYERS.remove(player);
    }

    // --- ActivePlayer ---

    public void addActivePlayer(ActivePlayer player) {
        ACTIVE_PLAYERS.add(player);
    }

    public void removeActivePlayer(ActivePlayer player) {
        ACTIVE_PLAYERS.remove(player);
    }

    public static ActivePlayer getActivePlayer(Player player) {
        for (ActivePlayer p : ACTIVE_PLAYERS) {
            if (p.getPlayer().equals(player)) {
                return p;
            }
        }
        return null;
    }
}
