package com.probending.probending.core.team;

import com.probending.probending.ProBending;
import com.probending.probending.config.CommandConfig;
import com.probending.probending.core.players.ActivePlayer;
import me.domirusz24.plugincore.config.annotations.Language;
import com.probending.probending.core.arena.ActiveArena;
import com.probending.probending.core.enums.Ring;
import com.probending.probending.core.enums.TeamTag;
import me.domirusz24.plugincore.core.team.AbstractTeam;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;



public class ActiveTeam extends AbstractTeam<ActivePlayer> {

    private final ActiveArena arena;

    private final TeamTag tag;

    private int points = 0;

    public ActiveTeam(ActiveArena arena, TeamTag tag) {
        super(tag.toString(), 3);
        this.arena = arena;
        this.tag = tag;
    }

    // ----

    public void onWin() {
        getPlayers(true).forEach(p -> {
            p.getPlayerData().setWins(p.getPlayerData().getWins() + 1);
            CommandConfig.Commands.ArenaWinPlayer.run(arena.getArena(), p.getPlayer());
        });
    }

    public void onLose() {
        getPlayers(true).forEach(p -> {
            p.getPlayerData().setLost(p.getPlayerData().getLost() + 1);
            CommandConfig.Commands.ArenaLosePlayer.run(arena.getArena(), p.getPlayer());
        });
    }


    @Override
    public boolean onAddPlayer(ActivePlayer player) {
        if (ProBending.teamM.getTeam(player) == null) {
            ProBending.teamM.TEAM_BY_PLAYER.put(player, this);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onRemovePlayer(ActivePlayer player) {
        ProBending.teamM.TEAM_BY_PLAYER.remove(player);
        player.unregister();
        return true;
    }

    @Override
    public boolean onPurgePlayers() {
        points = 0;
        return true;
    }

    // ----

    public void sendMessage(String message, boolean spectators) {
        getPlayers(spectators).forEach((player -> player.getPlayer().sendMessage(message)));
    }

    public void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut, boolean spectators) {
        getPlayers(spectators).forEach(player -> player.getPlayer().sendTitle(title, subtitle, fadeIn, stay, fadeOut));
    }

    // ----

    public List<ActivePlayer> getPlayers(boolean spectators) {
        if (spectators) {
            return players.stream().filter(Objects::nonNull).collect(Collectors.toList());
        } else {
            return players.stream().filter(Objects::nonNull).filter(player -> player.getState() != ActivePlayer.State.SPECTATING).collect(Collectors.toList());
        }
    }

    @Override
    public void sendMessage(String message) {
        sendMessage(message, true);
    }

    @Override
    public void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        sendTitle(title, subtitle, fadeIn, stay, fadeOut, true);
    }

    @Language("Team.InArena.TEAM_INFO")
    public static String LANG_TEAM_INFO = "--- %team_name% ---||Points: %team_points%||%team_players%||-------------------";

    @Language("Team.InArena.EMPTY_SLOT")
    public static String LANG_EMPTY_SLOT = "&7 - NONE";

    @Language("Team.InArena.PLAYER_SLOT")
    public static String LANG_PLAYER_SLOT = " - %probending_element%&r%player_name%: %probending_state%";

    @Override
    public String getPlayerInfoSyntax() {
        return LANG_PLAYER_SLOT;
    }

    @Override
    public String getNullPlayerInfoSyntax() {
        return LANG_EMPTY_SLOT;
    }

    @Override
    public String getSyntax() {
        return LANG_TEAM_INFO;
    }

    @Override
    protected String _onPlaceholderRequest(String message) {
        switch (message.toLowerCase()) {
            case "points":
                return String.valueOf(getPoints());
            case "color":
                return tag.getColor();
            case "team":
                return tag.toString();
            default:
                return null;
        }
    }

    // ----

    public ActiveArena getArena() {
        return arena;
    }

    public TeamTag getTag() {
        return tag;
    }

    // ----

    public void raisePoint() {
        points++;
    }

    public int getPoints() {
        return points;
    }

    public boolean isWipedOut() {
        return getPlayers(false).size() == 0;
    }

    public Ring getFurthestRing() {
        Ring teamMaxRing = null;
        for (ActivePlayer player : getPlayers(false)) {
            if (teamMaxRing == null) {
                teamMaxRing = player.getRing();
            } else if (teamMaxRing.offset(getTag(), player.getRing()) > 0) {
                teamMaxRing = player.getRing();
            }
        }
        return teamMaxRing;
    }

    public int getGap() {
        Ring enemyTeamMaxRing = getArena().getTeam(getTag().getOther()).getFurthestRing();
        Ring teamMaxRing = getFurthestRing();
        if (enemyTeamMaxRing == null || teamMaxRing == null) return 0;
        return Math.abs(teamMaxRing.getIndex() - enemyTeamMaxRing.getIndex()) - 1;
    }
}
