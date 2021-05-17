package com.probending.probending.managers;

import com.probending.probending.PBListener;
import com.probending.probending.ProBending;
import com.probending.probending.core.arena.ActiveArena;
import com.probending.probending.core.arena.Arena;
import com.probending.probending.core.displayable.PBHologram;
import com.probending.probending.core.players.SpectatorPlayer;
import com.probending.probending.core.team.ActiveTeam;
import com.probending.probending.util.PerTick;
import com.probending.probending.util.UtilMethods;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.*;

public class ArenaManager extends PBManager implements PerTick {

    public final HashMap<String, Arena> ARENA_BY_NAME = new HashMap<>();

    public final HashSet<ActiveArena> ACTIVE_ARENAS = new HashSet<>();

    // ------

    public ArenaManager(ProBending plugin) {
        super(plugin);
        registerArenas();
        PBListener.hookInListener(this);
    }

    // ------

    private void registerArenas() {
        File dir = new File(plugin.getDataFolder(), "Arenas/");
        dir.mkdirs();
        for (File folder : UtilMethods.getDirectories(dir)) {
            for (File file : UtilMethods.getFiles(folder)) {
                if (file.getName().contains(".")) {
                    if (file.getName().substring(file.getName().lastIndexOf('.') + 1).equals("yml")) {
                        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
                        if (config.isSet("Arena.name")) {
                            registerArena(new Arena(this, config.getString("Arena.name"), file));
                        }
                        break;
                    }
                }
            }
        }
    }

    // ------

    public void registerArena(Arena arena) {
        ARENA_BY_NAME.put(arena.getName(), arena);
    }

    // ------ ARENA ------

    public Arena getArena(String name) {
        return ARENA_BY_NAME.getOrDefault(name, null);
    }

    public Collection<Arena> getArenas() {
        return ARENA_BY_NAME.values();
    }

    // ------ ARENA LIST ------



    // ------ ACTIVE ARENA ------

    public ActiveArena getActiveArena(String name) {
        Arena arena = ARENA_BY_NAME.getOrDefault(name, null);
        if (arena != null) {
            return arena.getActiveArena();
        }
        return null;
    }

    public ActiveArena getActiveArena(Player player) {
        ActiveTeam team = ProBending.teamM.getTeam(player);
        if (team != null) {
            return team.getArena();
        } else {
            SpectatorPlayer playerSpec = ProBending.playerM.getSpectator(player);
            if (playerSpec != null) return playerSpec.getArena();
        }
        return null;
    }

    public Set<ActiveArena> getActiveArenas() {
        return new HashSet<>(ACTIVE_ARENAS);
    }

    @Override
    public void onTick() {
        ACTIVE_ARENAS.forEach(ActiveArena::update);
    }

    // --------------------------



}
