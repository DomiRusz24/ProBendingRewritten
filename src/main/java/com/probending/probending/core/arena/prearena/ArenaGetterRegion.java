package com.probending.probending.core.arena.prearena;

import com.probending.probending.ProBending;
import me.domirusz24.plugincore.config.annotations.Language;
import com.probending.probending.core.arena.Arena;
import com.probending.probending.core.enums.TeamTag;
import me.domirusz24.plugincore.core.placeholders.PlaceholderObject;
import com.probending.probending.core.players.MenuPlayer;
import com.probending.probending.core.team.PreArenaTeam;
import me.domirusz24.plugincore.core.region.CustomRegion;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

public class ArenaGetterRegion extends CustomRegion {

    private Location center;

    private Location regionCenter;

    private final TeamTag tag;

    private final PreArena arena;

    private final String path;

    private final PreArenaTeam team;

    public ArenaGetterRegion(String ID, PreArena arena, TeamTag tag) {
        super(ProBending.plugin, ID);
        this.tag = tag;
        this.arena = arena;
        this.team = new PreArenaTeam(tag.toString(), 3, this, tag);
        path = "ArenaGetter." + (tag == TeamTag.BLUE ? "Blue" : "Red");
    }

    public Location getCenter() {
        return center;
    }

    public void setCenter(Location center) {
        this.center = center;
        arena.isReady();
    }

    public Location getRegionCenter() {
        return regionCenter;
    }

    public void setRegionCenter(Location center) {
        this.regionCenter = center;
        arena.isReady();
    }

    @Override
    public void setLocations(Location min, Location max) {
        super.setLocations(min, max);
        arena.isReady();
    }

    @Language("ArenaPreGame.TeamFull")
    public static String LANG_TEAM_FULL = "This team is already full or the countdown has started!";

    @Language("ArenaPreGame.Leave")
    public static String LANG_TEAM_LEAVE = "You left this team!";

    @Language("ArenaPreGame.Join.RED")
    public static String LANG_JOIN_RED = "You have joined the red team!";

    @Language("ArenaPreGame.Join.BLUE")
    public static String LANG_JOIN_BLUE = "You have joined the blue team!";

    @Override
    public void onPlayerEnter(Player player) {
        for (MenuPlayer menuPlayer : team.getPlayers()) {
            if (menuPlayer.getPlayer().equals(player)) return;
        }
        if (arena.getArena().getState() == Arena.State.READY && (arena.getState() == PreArena.State.WAITING || arena.getState() == PreArena.State.COUNTDOWN)) {
            if (team.isFull() || arena.getState() == PreArena.State.COUNTDOWN) {
                if (getCenter() != null) {
                    try {
                        player.setVelocity(getCenter().clone().subtract(player.getLocation()).toVector().normalize());
                    } catch (IllegalArgumentException e) {
                        player.teleport(getCenter());
                    }
                }
                player.sendTitle(LANG_TEAM_FULL, "", 5, 30, 5);
            } else {
                if (tag == TeamTag.BLUE) {
                    player.sendTitle(LANG_JOIN_BLUE, "", 5, 30, 5);
                } else {
                    player.sendTitle(LANG_JOIN_RED, "", 5, 30, 5);
                }
                arena.getArena().getTeam(tag).addPlayer(player);
                MenuPlayer mPlayer = new MenuPlayer(player, this, tag);
                team.addPlayer(mPlayer);
                arena.onJoin(mPlayer);
            }
        }
    }

    @Override
    public void onPlayerLeave(Player player) {
        for (MenuPlayer menuPlayer : team.getPlayers()) {
            if (menuPlayer.getPlayer().equals(player)) {
                if (arena.getState() == PreArena.State.COUNTDOWN) {
                    player.setVelocity(getRegionCenter().clone().subtract(player.getLocation()).toVector().normalize());
                } else {
                    player.sendTitle(LANG_TEAM_LEAVE, "", 5, 30, 5);
                    arena.getArena().getTeam(tag).removePlayer(player);
                    team.removePlayer(menuPlayer);
                    arena.onLeave(menuPlayer);
                }
                break;
            }
        }
    }

    @Override
    protected void onPlayerForceLeave(Player player) {
        for (MenuPlayer menuPlayer : team.getPlayers()) {
            if (menuPlayer.getPlayer().equals(player)) {
                team.removePlayer(menuPlayer);
                arena.onForceLeave(menuPlayer);
                break;
            }
        }
    }

    @Override
    protected void _onLeave(Player player) {
        onPlayerLeave(player);
    }

    public List<MenuPlayer> getMenuPlayers() {
        return team.getPlayers();
    }

    public String getPath() {
        return path;
    }

    public PreArena getArena() {
        return arena;
    }

    public TeamTag getTag() {
        return tag;
    }

    public PreArenaTeam getTeam() {
        return team;
    }
}
