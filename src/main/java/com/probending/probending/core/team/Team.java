package com.probending.probending.core.team;

import com.probending.probending.ProBending;
import me.domirusz24.plugincore.PluginCore;
import me.domirusz24.plugincore.config.annotations.Language;
import com.probending.probending.core.arena.Arena;
import com.probending.probending.core.players.PBPlayer;
import com.probending.probending.core.players.PBPlayerWrapper;
import me.domirusz24.plugincore.core.team.AbstractTeam;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Team extends AbstractTeam<PBPlayerWrapper> {

    public Team(String name, int size) {
        super(ProBending.plugin, name, size);
    }

    public boolean removePlayer(Player player) {
        return removePlayer(PBPlayerWrapper.of(player));
    }

    public boolean addPlayer(Player player) {
        return addPlayer(PBPlayerWrapper.of(player));
    }


    public List<Player> getUnwrappedPlayers() {
        return getPlayers().stream().map(PBPlayerWrapper::getPlayer).collect(Collectors.toList());
    }

    public List<Player> getUnwrappedNullPlayers() {
        return getNullPlayers().stream().map(wrapped -> {return wrapped == null ? null : wrapped.getPlayer();}).collect(Collectors.toList());
    }

    @Override
    public boolean onAddPlayer(PBPlayerWrapper player) {
        return true;
    }

    @Override
    public boolean onRemovePlayer(PBPlayerWrapper player) {
        return true;
    }

    @Override
    public boolean onPurgePlayers() {
        return true;
    }

    @Override
    public void sendMessage(String message) {
        getPlayers().forEach((player -> player.getPlayer().sendMessage(message)));
    }

    @Override
    public void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        getPlayers().forEach(player -> player.getPlayer().sendTitle(title, subtitle, fadeIn, stay, fadeOut));
    }

    @Language("Team.Normal.TEAM_INFO")
    public static String LANG_TEAM_INFO = "--- %team_name% ---||Size: %team_size%||%team_players%||-------------------";

    @Language("Team.Normal.EMPTY_SLOT")
    public static String LANG_EMPTY_SLOT = "&7 - NONE||";

    @Language("Team.Normal.PLAYER_SLOT")
    public static String LANG_PLAYER_SLOT = " - %player_name%||";

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
        return null;
    }


    // ----

}
