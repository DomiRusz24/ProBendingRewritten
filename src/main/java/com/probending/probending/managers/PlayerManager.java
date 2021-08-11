package com.probending.probending.managers;

import com.probending.probending.ProBending;
import me.domirusz24.plugincore.config.annotations.Language;
import com.probending.probending.core.players.*;
import com.probending.probending.core.team.ArenaTempTeam;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class PlayerManager extends PBManager {

    private final HashSet<ActivePlayer> ACTIVE_PLAYERS = new HashSet<>();

    private final HashSet<SpectatorPlayer> SPECTATOR_PLAYERS = new HashSet<>();

    private final HashSet<MenuPlayer> MENU_PLAYERS = new HashSet<>();

    private final HashSet<Player> FROZEN_PLAYERS = new HashSet<>();

    private final HashMap<String, Role> ROLE_BY_ID = new HashMap<>();

    private final HashMap<Player, Player> LAST_HIT = new HashMap<>();

    public PlayerManager(ProBending plugin) {
        super(plugin);
        loadRoles();
    }

    @Language("Role.Captain.Prefix")
    public static String LANG_CAPTAIN_PREFIX = "[C]";

    @Language("Role.Captain.Full")
    public static String LANG_CAPTAIN_FULL = "Captain";

    private void loadRoles() {
        new Role("captain", () -> LANG_CAPTAIN_FULL, () -> LANG_CAPTAIN_PREFIX, this);
    }

    // --- ActivePlayer ---

    public ActivePlayer getActivePlayer(Player player) {
        for (ActivePlayer p : ACTIVE_PLAYERS) {
            if (p.getPlayer().equals(player)) {
                return p;
            }
        }
        return null;
    }

    public boolean isPlaying(Player player) {
        for (ActivePlayer p : ACTIVE_PLAYERS) {
            if (p.getPlayer().equals(player)) return true;
        }
        return false;
    }

    public void addActivePlayer(ActivePlayer player) {
        ACTIVE_PLAYERS.add(player);
    }

    public void removeActivePlayer(ActivePlayer player) {
        ACTIVE_PLAYERS.remove(player);
    }

    // --- Spectator Player ----

    public SpectatorPlayer getSpectator(Player player) {
        for (SpectatorPlayer p : SPECTATOR_PLAYERS) {
            if (p.getPlayer().equals(player)) {
                return p;
            }
        }
        return null;
    }

    public void addSpectator(SpectatorPlayer player) {
        SPECTATOR_PLAYERS.add(player);
    }

    public void removeSpectator(SpectatorPlayer player) {
        SPECTATOR_PLAYERS.remove(player);
    }

    // --- PLAYER ---

    public void freezePlayer(Player player, boolean freeze) {
        if (freeze) {
            FROZEN_PLAYERS.add(player);
        } else {
            FROZEN_PLAYERS.remove(player);
        }
    }

    // --- MENU PLAYER ---

    public MenuPlayer getMenuPlayer(Player player) {
        for (MenuPlayer p : MENU_PLAYERS) {
            if (p.getPlayer().equals(player)) {
                return p;
            }
        }
        return null;
    }

    public void addMenuPlayer(MenuPlayer player) {
        MENU_PLAYERS.add(player);
    }

    public void removeMenuPlayer(MenuPlayer player) {
        MENU_PLAYERS.remove(player);
    }

    // -------------------

    public HashSet<Player> getFrozenPlayers() {
        return new HashSet<>(FROZEN_PLAYERS);
    }

    public Player getLastDamager(Player player) {
        return LAST_HIT.getOrDefault(player, null) == null ? null : LAST_HIT.get(player).isOnline() ? LAST_HIT.get(player) : null;
    }

    public void registerHit(Player damager, Player entity) {
        LAST_HIT.put(entity, damager);
    }

    public void unregisterPlayer(Player player) {

        // Active player
        ActivePlayer activePlayer = getActivePlayer(player);
        if (activePlayer != null) activePlayer.setState(ActivePlayer.State.LEFT);

        // TempTeam
        ArenaTempTeam team = ProBending.teamM.getTempTeam(player);
        if (team != null) team.removePlayer(player);

        // Player wrapper
        PBPlayerWrapper.unregister(player);

        // Last hit
        LAST_HIT.remove(player);

        ProBending.regionM.getRegions().forEach(r -> r.onLeave(player));
    }

    public Role getRole(String id) {
        return ROLE_BY_ID.getOrDefault(id, null);
    }

    public void registerRole(Role role) {
        ROLE_BY_ID.put(role.getId(), role);
    }

    public Collection<Role> getAllRoles() {
        return ROLE_BY_ID.values();
    }
}
