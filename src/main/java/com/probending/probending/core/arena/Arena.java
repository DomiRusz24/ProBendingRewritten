package com.probending.probending.core.arena;

import com.probending.probending.ProBending;
import com.probending.probending.api.events.PBArenaStartEvent;
import com.probending.probending.config.arena.ArenaCommandConfig;
import com.probending.probending.config.arena.ArenaConfig;
import com.probending.probending.config.arena.ArenaLocationConfig;
import com.probending.probending.command.Languages;
import me.domirusz24.plugincore.PluginCore;
import me.domirusz24.plugincore.config.annotations.Language;
import com.probending.probending.core.arena.prearena.PreArena;
import com.probending.probending.core.enums.Ring;
import com.probending.probending.core.enums.TeamTag;
import me.domirusz24.plugincore.core.displayable.CustomSign;
import me.domirusz24.plugincore.core.placeholders.PlaceholderObject;
import com.probending.probending.core.team.ArenaTempTeam;
import com.probending.probending.core.team.PreArenaTeam;
import com.probending.probending.core.team.Team;
import com.probending.probending.managers.ArenaManager;
import com.probending.probending.managers.ProjectKorraManager;
import com.probending.probending.util.UtilMethods;
import com.projectkorra.projectkorra.BendingPlayer;
import me.clip.placeholderapi.PlaceholderAPI;
import me.domirusz24.plugincore.managers.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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

    private final ArenaLocationConfig locationConfig;

    private final ArenaCommandConfig commandConfig;

    private final ArenaConfig arenaConfig;

    private final SpectateGetterRegion spectateGetterRegion;

    private final ArenaTempTeam blueTeam;

    private final ArenaTempTeam redTeam;

    public Arena(ArenaManager manager, String name) {
        if (name.length() >= 12 && name.contains(" ") && name.contains(".")) {
            state = State.ERROR;
            throw new IllegalArgumentException();
        }
        this.name = name;
        this.locationConfig = new ArenaLocationConfig(this, name + ".yml", ProBending.plugin);
        this.commandConfig = new ArenaCommandConfig(this, "commands.yml", ProBending.plugin);
        this.arenaConfig = new ArenaConfig(this, "config.yml", ProBending.plugin);
        this.blueTeam = new ArenaTempTeam(this, TeamTag.BLUE);
        this.redTeam = new ArenaTempTeam(this, TeamTag.RED);
        this.spectateGetterRegion = locationConfig.getRegion("SpectateGetter.Region", name + "_spectateGetter");
        loadFromConfig();
        activeArena = new ActiveArena(this);
        manager.registerArena(this);
        this.preArena = new PreArena(this);
        state = State.NOT_COMPLETE;
        this.joinSign = locationConfig.getSign("JoinSign", name + "_sign");
        setUpSign();
        isReadyToPlay();
        registerReloadable();
    }

    public ArenaLocationConfig getLocationConfig() {
        return locationConfig;
    }

    public ArenaConfig getArenaConfig() {
        return arenaConfig;
    }

    public ArenaCommandConfig getCommandConfig() {
        return commandConfig;
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
            } else if (state == State.TAKEN) {
                if (ProBending.playerM.getActivePlayer(p) == null && ProBending.playerM.getMenuPlayer(p) == null) {
                    if (getSpectatorSpawn() != null) p.teleport(getSpectatorSpawn());
                }
            }
        });
        joinSign.update();
    }

    private void loadFromConfig() {
        // Center
        center = locationConfig.getCenter();

        // Rollback location
        rollbackLocation = locationConfig.getRollbackLocation();

        // Ring locations
        for (Ring ring : Ring.values()) {
            if (ring.isTeleportRing()) {
                ringLocations.put(ring, locationConfig.getRingLocation(ring));
            }
        }

        // Starting locations
        for (TeamTag tag : TeamTag.values()) {
            for (int i = 0; i < 3; i++) {
                startingLocations.putIfAbsent(tag, new Location[3]);
                startingLocations.get(tag)[i] = locationConfig.getStartingLocations(tag, i + 1);
            }
        }
    }

    @Override
    public void onReload() {
        joinSign.resetValues();
        joinSign.addValue(UtilMethods.stringToList(LANG_ARENA_JOIN));
        joinSign.update();
    }

    public boolean isReadyToPlay() {
        if (ProBending.locationConfig.getSpawn() == null) {
            ProBending.plugin.log(Level.WARNING, "ProBending spawn is not set!");
            return false;
        }
        if (state == null || state == State.NOT_COMPLETE) {
            if (getCenter() == null) {
                ProBending.plugin.log(Level.WARNING, "Arena " + getName() + " center is not set!");
                return false;
            }
            /*
            if (rollbackLocation == null) {
                ProBending.plugin.log(Level.WARNING, "Arena " + getName() + " rollback is not set!");
                return false;
            }
             */
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
            if (!getPreArena().isReady()) {
                return false;
            }
            state = State.READY;
            return true;
        } else return state == State.READY;
    }

    // ----- START -----

    public void start(boolean force) {
        start(blueTeam, redTeam, force);
    }

    public void start(Team blue, Team red, boolean force) {
        if (state == State.READY) {
            if (isReadyToPlay()) {
                if (canStart(blue, red, force)) {
                    List<Player> players = blue.getUnwrappedPlayers();
                    players.addAll(red.getUnwrappedPlayers());
                    for (Player player : players) {
                        ProjectKorraManager.BendingState state = ProBending.projectKorraM.getBendingState(BendingPlayer.getBendingPlayer(player), getArenaConfig().getBannedAbilities());
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
                    activeArena.start(blue, red);
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

    public CustomSign getJoinSign() {
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
        locationConfig.setCenter(location);
        locationConfig.save();
        isReadyToPlay();
    }

    public void setSpectatorSpawn(Location location) {
        locationConfig.setSpectatorSpawn(location);
        locationConfig.save();
    }

    public Location getSpectatorSpawn() {
        return locationConfig.getSpectatorSpawn();
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
        locationConfig.setRingLocation(ring, location);
        locationConfig.save();
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
        locationConfig.setStartingLocations(tag, player + 1, location);
        locationConfig.save();
        isReadyToPlay();
    }

    // --- Rollback location ---

    private Location rollbackLocation = null;

    public Location getRollbackLocation() {
        return rollbackLocation == null ? null : rollbackLocation.clone();
    }

    public void setRollbackLocation(Location location) {
        rollbackLocation = location;
        locationConfig.setRollbackLocation(location);
        locationConfig.save();
        isReadyToPlay();
    }

    // --- Rollback ---

    public void setRollback(CommandSender sender, Location min, Location max) {
        setRollbackLocation(min);
        locationConfig.setRollback(sender, min, max);
        isReadyToPlay();
    }

    public boolean getRollback(CommandSender sender) {
        if (rollbackLocation == null) {
            return false;
        }
        return locationConfig.getRollback(sender, rollbackLocation);
    }

    // --- Sign ---

    private final CustomSign joinSign;

    public void setSignLocation(Sign sign) {
        joinSign.setSign(sign);
        locationConfig.setSign("JoinSign", joinSign);
        locationConfig.save();
    }

    // --- Region ---

    public void setRegionSelection(Location min, Location max, TeamTag tag) {
        preArena.getRegion(tag).setLocations(min, max);
        locationConfig.setRegion(preArena.getRegion(tag).getPath(), preArena.getRegion(tag));
        locationConfig.save();
    }

    public void setSpectatorRegion(Location min, Location max) {
        spectateGetterRegion.setLocations(min, max);
        locationConfig.setRegion(spectateGetterRegion);
        locationConfig.save();
    }

    public void setRegionCenter(Location center, TeamTag tag) {
        preArena.getRegion(tag).setCenter(center);
        locationConfig.setRegion(preArena.getRegion(tag).getPath(), preArena.getRegion(tag));
        locationConfig.save();
    }

    public void setRegionRegionCenter(Location center, TeamTag tag) {
        preArena.getRegion(tag).setRegionCenter(center);
        locationConfig.setRegion(preArena.getRegion(tag).getPath(), preArena.getRegion(tag));
        locationConfig.save();
    }

    // -------------------------

    public boolean throwIntoGame(Player... playersList) {
        return throwIntoGame(Arrays.asList(playersList));
    }

    public boolean throwIntoGame(List<Player> players) {
        if (getPreArena().getState() == PreArena.State.WAITING && getState().equals(Arena.State.READY)) {
            PreArena pArena = getPreArena();
            for (TeamTag tag : TeamTag.values()) {
                PreArenaTeam pTeam = pArena.getRegion(tag).getTeam();
                if ((pTeam.getSize() - pTeam.getCurrentSize()) >= players.size()) {
                    Location c = pArena.getRegion(tag).getRegionCenter();
                    players.forEach(p -> p.teleport(c));
                    return true;
                }
            }
        }
        return false;
    }


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
                return inGame() ? Languages.YES : Languages.NO;
            case "in_game_color":
                return inGame() ? "&a" : "&c";
            case "x":
                return String.valueOf(getCenter().getBlockX());
            case "y":
                return String.valueOf(getCenter().getBlockY());
            case "z":
                return String.valueOf(getCenter().getBlockZ());
            case "has_backup": {
                return getRollbackLocation() == null ? Languages.NO : Languages.YES;
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

    @Override
    public PluginCore getCorePlugin() {
        return ProBending.plugin;
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
