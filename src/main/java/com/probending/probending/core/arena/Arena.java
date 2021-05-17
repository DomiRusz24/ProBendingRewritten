package com.probending.probending.core.arena;

import com.probending.probending.ProBending;
import com.probending.probending.api.events.PBArenaStartEvent;
import com.probending.probending.command.abstractclasses.Command;
import com.probending.probending.config.ArenaConfig;
import com.probending.probending.core.annotations.Language;
import com.probending.probending.core.arena.prearena.PreArena;
import com.probending.probending.core.displayable.PBSign;
import com.probending.probending.core.enums.GameType;
import com.probending.probending.core.enums.Ring;
import com.probending.probending.core.enums.TeamTag;
import com.probending.probending.core.interfaces.PlaceholderObject;
import com.probending.probending.core.players.PBPlayerWrapper;
import com.probending.probending.core.team.ArenaTempTeam;
import com.probending.probending.core.team.Team;
import com.probending.probending.managers.ArenaManager;
import com.probending.probending.managers.ConfigManager;
import com.probending.probending.managers.ProjectKorraManager;
import com.probending.probending.util.UtilMethods;
import com.projectkorra.projectkorra.BendingPlayer;
import com.sk89q.worldedit.WorldEditException;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;
import java.util.logging.Level;

import static com.probending.probending.core.enums.TeamTag.BLUE;

public class Arena implements PlaceholderObject, ConfigManager.Reloadable {

    // --- LANGUAGE ---

    @Language("Arena.State.DISABLED")
    public static String LANG_DISABLED = "&7Disabled!";

    @Language("Arena.State.NOT_COMPLETE")
    public static String LANG_NOT_COMPLETE = "&cNot complete!";

    @Language("Arena.State.ERROR")
    public static String LANG_ERROR = "&4This arena has a malfunction.";

    @Language("Arena.State.EMPTY")
    public static String LANG_READY = "&2Ready to play.";

    @Language("Arena.State.TAKEN")
    public static String LANG_TAKEN = "&dTaken!";

    @Language("Arena.BenderState.NO_ABILITIES")
    public static String LANG_NO_ABILITIES = "%player_name% has no abilities!";

    @Language("Arena.BenderState.MULTIPLE_ABILITIES")
    public static String LANG_MULTI_ABILITY = "%player_name% has multiple abilities!";

    @Language("Arena.BenderState.ILLEGAL_ABILITIES")
    public static String LANG_ILLEGAL_ABILITIES = "%player_name% has illegal abilities!";

    //TODO: Arena Rules
    @Language("Arena.Rules")
    public static String LANG_ARENA_RULES = "PB rules || Some pb rules lol";

    public static String getArenaRules() {
        return LANG_ARENA_RULES;
    }

    // -------------------------------- //

    private final ActiveArena activeArena;

    public ActiveArena getActiveArena() {
        if (state == State.TAKEN) {
            return activeArena;
        } else {
            return null;
        }
    }

    private final PreArena preArena;

    public PreArena getPreArena() {
        return preArena;
    }

    // -------------------------------- //

    private State state;

    private final String name;

    private final ArenaConfig config;

    private final ArenaTempTeam blueTeam;

    private final ArenaTempTeam redTeam;

    public Arena(ArenaManager manager, String name) {
        if (name.length() >= 12 && name.contains(" ") && name.contains(".")) {
            state = State.ERROR;
            throw new IllegalArgumentException();
        }
        this.name = name;
        this.config = new ArenaConfig(this, "Arenas/" + name + "/" + name + ".yml", ProBending.plugin);
        this.blueTeam = new ArenaTempTeam(this, BLUE);
        this.redTeam = new ArenaTempTeam(this, TeamTag.RED);
        loadFromConfig();
        activeArena = new ActiveArena(this);
        manager.registerArena(this);
        this.preArena = new PreArena(this);
        state = State.NOT_COMPLETE;
        this.joinSign = config.getSign("JoinSign", name + "_sign");
        setUpSign();
        isReadyToPlay();
        registerReloadable();
    }

    public Arena(ArenaManager manager, String name, File config) {
        if (name.length() >= 12 && name.contains(" ") && name.contains(".")) {
            state = State.ERROR;
            throw new IllegalArgumentException();
        }
        this.name = name;
        this.config = new ArenaConfig(this, config, ProBending.plugin);
        this.blueTeam = new ArenaTempTeam(this, BLUE);
        this.redTeam = new ArenaTempTeam(this, TeamTag.RED);
        loadFromConfig();
        activeArena = new ActiveArena(this);
        manager.registerArena(this);
        state = State.NOT_COMPLETE;
        this.joinSign = this.config.getSign("JoinSign", name + "_sign");
        this.preArena = new PreArena(this);
        preArena.setEnabled(true);
        setUpSign();
        isReadyToPlay();
        registerReloadable();
    }

    public ArenaConfig getConfig() {
        return config;
    }

    @Language("Arena.ArenaJoinSign")
    public static String LANG_ARENA_JOIN = "%arena_name%||%arena_prearena_state%||%arena_players% / 6||---------";

    private void setUpSign() {
        joinSign.addPlaceholder(this);
        joinSign.addValue(UtilMethods.stringToList(LANG_ARENA_JOIN));
        joinSign.setOnRightClick(p -> {
            if (state == State.READY && preArena.isReady()) {
                if (preArena.getBlueRegion().getPlayers().size() > preArena.getRedRegion().getPlayers().size()) {
                    p.teleport(preArena.getRedRegion().getCenter());
                } else {
                    p.teleport(preArena.getBlueRegion().getCenter());
                }
            }
        });
        joinSign.update();
    }

    private void loadFromConfig() {
        // Center
        center = config.getCenter();

        // Rollback location
        rollbackLocation = config.getRollbackLocation();

        // Ring locations
        for (Ring ring : Ring.values()) {
            if (ring.isTeleportRing()) {
                ringLocations.put(ring, config.getRingLocation(ring));
            }
        }

        // Starting locations
        for (TeamTag tag : TeamTag.values()) {
            for (int i = 0; i < 3; i++) {
                startingLocations.putIfAbsent(tag, new Location[3]);
                startingLocations.get(tag)[i] = config.getStartingLocations(tag, i + 1);
            }
        }
    }

    @Override
    public void onReload() {
        joinSign.resetValues();
        joinSign.addValue(UtilMethods.stringToList(LANG_ARENA_JOIN));
        joinSign.update();
    }

    private boolean isReadyToPlay() {
        if (ProBending.configM.getLocationsConfig().getSpawn() == null) {
            ProBending.plugin.log(Level.WARNING, "ProBending spawn is not set!");
            return false;
        }
        if (state == null || state == State.NOT_COMPLETE) {
            if (getCenter() == null) {
                ProBending.plugin.log(Level.WARNING, "Arena " + getName() + " center is not set!");
                return false;
            }
            if (rollbackLocation == null) {
                ProBending.plugin.log(Level.WARNING, "Arena " + getName() + " rollback is not set!");
                return false;
            }
            for (Ring ring : Ring.values()) {
                if (ring.isTeleportRing()) {
                    if (getRingLocation(ring) == null) {
                        ProBending.plugin.log(Level.WARNING, "Arena " + getName() + " ring " + ring.name() + " is not set!");
                        return false;
                    }
                }
            }

            for (TeamTag tag : TeamTag.values()) {
                for (int i = 0; i < 3; i++) {
                    if (getStartingLocations(tag, i) == null) {
                        ProBending.plugin.log(Level.WARNING, "Arena " + getName() + " player " + i + " in team " + tag.name() + " is not set!");
                        return false;
                    }
                }
            }
            state = State.READY;
            return true;
        } else return state == State.READY;
    }

    // ----- START -----

    public void start(GameType type, boolean force) {
        start(blueTeam, redTeam, type, force);
    }

    public void start(Team blue, Team red, GameType type, boolean force) {
        if (state == State.READY) {
            if (isReadyToPlay()) {
                if (canStart(blue, red, force)) {
                    List<Player> players = blue.getUnwrappedPlayers();
                    players.addAll(players = red.getUnwrappedPlayers());
                    for (Player player : players) {
                        ProjectKorraManager.BendingState state = ProBending.projectKorraM.getBendingState(BendingPlayer.getBendingPlayer(player));
                        String message;
                        if (state == null) {
                            message = PlaceholderAPI.setPlaceholders(player, LANG_NO_ABILITIES);
                            blue.sendTitle(message, "", 5, 20, 5);
                            red.sendTitle(message, "", 5, 20, 5);
                            return;
                        }
                        switch (state) {
                            case NONE:
                                message = PlaceholderAPI.setPlaceholders(player, LANG_NO_ABILITIES);
                                blue.sendTitle(message, "", 5, 20, 5);
                                red.sendTitle(message, "", 5, 20, 5);
                                return;
                            case MULTI:
                                message = PlaceholderAPI.setPlaceholders(player, LANG_MULTI_ABILITY);
                                blue.sendTitle(message, "", 5, 20, 5);
                                red.sendTitle(message, "", 5, 20, 5);
                                return;
                            case ILLEGAL:
                                message = PlaceholderAPI.setPlaceholders(player, LANG_ILLEGAL_ABILITIES);
                                blue.sendTitle(message, "", 5, 20, 5);
                                red.sendTitle(message, "", 5, 20, 5);
                                return;
                        }
                    }

                    PBArenaStartEvent event = new PBArenaStartEvent(this);
                    Bukkit.getPluginManager().callEvent(event);

                    if (event.isCancelled()) {
                        return;
                    }

                    preArena.setEnabled(false);
                    preArena.setState(PreArena.State.TAKEN);
                    activeArena.start(blue, red, type);
                    red.purgePlayers();
                    blue.purgePlayers();
                    joinSign.update();
                }
            }
        }
    }

    public void stop() {
        if (state == State.TAKEN) {
            activeArena.forceStop();
        }
    }

    public boolean inGame() {
        return state == State.TAKEN;
    }

    public PBSign getJoinSign() {
        return joinSign;
    }

    public boolean canStart(boolean force) {
        return canStart(blueTeam, redTeam, force);
    }

    public boolean canStart(Team blue, Team red, boolean force) {
        return (force ? blue.getPlayers().size() + red.getPlayers().size() > 1 : blue.isFull() && red.isFull()) && state == State.READY;
    }

    // --- Name ---

    public String getName() {
        return name;
    }

    // --- TempTeams ---

    public ArenaTempTeam getTeam(TeamTag tag) {
        return tag == BLUE ? blueTeam : redTeam;
    }

    public boolean removePlayerFromTempTeam(Player player) {
        for (TeamTag team : TeamTag.values()) {
            if (getTeam(team).removePlayer(player)) {
                return true;
            }
        }
        return false;
    }

    // --- State management ---

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    // --- Center ---

    private Location center;

    public Location getCenter() {
        return center == null ? null : center.clone();
    }

    public void setCenter(Location location) {
        center = location;
        config.setCenter(location);
        config.save();
        isReadyToPlay();
    }

    // --- Ring locations ---

    private final HashMap<Ring, Location> ringLocations = new HashMap<>();

    public Location getRingLocation(Ring ring) {
        if (ring.isTeleportRing()) {
            return ringLocations.getOrDefault(ring, null) == null ? null : ringLocations.get(ring).clone();
        } else {
            return null;
        }
    }

    public void setRingLocation(Ring ring, Location location) {
        ringLocations.put(ring, location);
        config.setRingLocation(ring, location);
        config.save();
        isReadyToPlay();
    }

    // --- Starting locations ---

    private HashMap<TeamTag, Location[]> startingLocations = new HashMap<>();

    public Location getStartingLocations(TeamTag tag, int player) {
        startingLocations.putIfAbsent(tag, new Location[3]);
        if (startingLocations.containsKey(tag)) {
            return startingLocations.get(tag)[player] == null ? null : startingLocations.get(tag)[player].clone();
        } else {
            return null;
        }
    }

    public void setStartingLocations(TeamTag tag, int player, Location location) {
        startingLocations.putIfAbsent(tag, new Location[3]);
        startingLocations.get(tag)[player] = location;
        config.setStartingLocations(tag, player + 1, location);
        config.save();
        isReadyToPlay();
    }

    // --- Rollback location ---

    private Location rollbackLocation = null;

    public Location getRollbackLocation() {
        return rollbackLocation == null ? null : rollbackLocation.clone();
    }

    public void setRollbackLocation(Location location) {
        rollbackLocation = location;
        config.setRollbackLocation(location);
        config.save();
        isReadyToPlay();
    }

    // --- Rollback ---

    public void setRollback(CommandSender sender, Location min, Location max) {
        setRollbackLocation(min);
        config.setRollback(sender, min, max);
        isReadyToPlay();
    }

    public boolean getRollback(CommandSender sender) {
        return config.getRollback(sender, rollbackLocation);
    }

    // --- Sign ---

    private final PBSign joinSign;

    public void setSignLocation(Sign sign) {
        joinSign.setSign(sign);
        config.setSign("JoinSign", joinSign);
        config.save();
    }

    // --- Region ---

    public void setRegionSelection(Location min, Location max, TeamTag tag) {
        preArena.getRegion(tag).setLocations(min, max);
        config.setRegion(preArena.getRegion(tag).getPath(), preArena.getRegion(tag));
        config.save();
    }

    public void setRegionCenter(Location center, TeamTag tag) {
        preArena.getRegion(tag).setCenter(center);
        config.setRegion(preArena.getRegion(tag).getPath(), preArena.getRegion(tag));
        config.save();
    }

    // -------------------------


    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() == this.getClass()) {
            return ((Arena) obj).getName().equals(getName());
        }
        return false;
    }

    @Override
    public String onPlaceholderRequest(String param) {
        switch (param.toLowerCase()) {
            case "name":
                return getName();
            case "in_game":
                return inGame() ? Command.LANG_YES : Command.LANG_NO;
            case "x":
                return String.valueOf(getCenter().getBlockX());
            case "y":
                return String.valueOf(getCenter().getBlockY());
            case "z":
                return String.valueOf(getCenter().getBlockZ());
            case "has_backup": {
                return getRollbackLocation() == null ? Command.LANG_NO : Command.LANG_YES;
            }
            case "prearena_state": {
                return preArena.getState().toString();
            }
            case "players":
                return inGame() ? String.valueOf(getActiveArena().getPlayers(true).size()) : String.valueOf(redTeam.getCurrentSize() + blueTeam.getCurrentSize());
            default: {
                if (inGame()) {
                    return activeArena.onPlaceholderRequest(param);
                } else if (param.toLowerCase().equals("state")) {
                    return getState().toString();
                } else {
                    return null;
                }
            }
        }
    }

    @Override
    public String placeHolderPrefix() {
        return "arena";
    }


    // -------------------------

    public enum State {
        DISABLED,
        NOT_COMPLETE,
        ERROR,
        READY,
        TAKEN;

        public String toString() {
            switch (this) {
                case DISABLED:
                    return LANG_DISABLED;
                case NOT_COMPLETE:
                    return (LANG_NOT_COMPLETE);
                case READY:
                    return (LANG_READY);
                case TAKEN:
                    return (LANG_TAKEN);
                default:
                    return (LANG_ERROR);
            }
        }
    }
}
