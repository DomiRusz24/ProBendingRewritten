package com.probending.probending.managers;

import com.probending.probending.ProBending;
import com.probending.probending.core.arena.ActiveArena;
import com.probending.probending.core.arena.Arena;
import com.probending.probending.core.arena.prearena.PreArena;
import com.probending.probending.core.enums.TeamTag;
import com.probending.probending.core.players.SpectatorPlayer;
import com.probending.probending.core.team.ActiveTeam;
import com.probending.probending.core.team.PreArenaTeam;
import com.probending.probending.util.UtilMethods;
import me.domirusz24.plugincore.PluginCore;
import me.domirusz24.plugincore.util.PerTick;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;

public class ArenaManager extends PBManager implements PerTick {

    public final HashMap<String, Arena> ARENA_BY_NAME = new HashMap<>();

    public final HashSet<ActiveArena> ACTIVE_ARENAS = new HashSet<>();

    // ------

    public ArenaManager(ProBending plugin) {
        super(plugin);
        registerArenas();
        registerPerTick();
    }

    // ------

    private void registerArenas() {
        File dir = new File(plugin.getDataFolder(), "Arenas/");
        dir.mkdirs();
        for (File folder : UtilMethods.getDirectories(dir)) {
            String name = null;
            File locationConfig = null;
            File config = null;
            File commandConfig = null;
            for (File file : UtilMethods.getFiles(folder)) {
                if (file.getName().contains(".")) {
                    if (file.getName().substring(file.getName().lastIndexOf('.') + 1).equals("yml")) {
                        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
                        if (yaml.isSet("Arena.name")) {
                            name = yaml.getString("Arena.name");
                            locationConfig = file;
                        }
                    }
                }
            }
            if (locationConfig != null && name != null) {
                registerArena(new Arena(this, name));
            }
        }
    }

    public boolean throwIntoGame(Player... playersList) {
       return throwIntoGame(Arrays.asList(playersList));
    }

    public boolean throwIntoGame(List<Player> players) {
        for (Arena arena : ProBending.arenaM.getArenas()) {
            if (arena.getArenaConfig().isPublic() && arena.getPreArena().getState() == PreArena.State.WAITING && arena.getState().equals(Arena.State.READY)) {
                PreArena pArena = arena.getPreArena();
                for (TeamTag tag : TeamTag.values()) {
                    PreArenaTeam pTeam = pArena.getRegion(tag).getTeam();
                    if ((pTeam.getSize() - pTeam.getCurrentSize()) >= players.size()) {
                        Location c = pArena.getRegion(tag).getRegionCenter();
                        players.forEach(p -> p.teleport(c));
                        return true;
                    }
                }
            }
        }
        return false;
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
        getActiveArenas().forEach(ActiveArena::update);
    }

    // --------------------------

    @Override
    public PluginCore getCorePlugin() {
        return ProBending.plugin;
    }



}
