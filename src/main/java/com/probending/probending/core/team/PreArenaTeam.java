package com.probending.probending.core.team;

import com.probending.probending.ProBending;
import me.domirusz24.plugincore.config.annotations.Language;
import com.probending.probending.core.arena.prearena.ArenaGetterRegion;
import com.probending.probending.core.enums.TeamTag;
import com.probending.probending.core.players.MenuPlayer;
import me.domirusz24.plugincore.core.team.AbstractTeam;

public class PreArenaTeam extends AbstractTeam<MenuPlayer> {

    private final ArenaGetterRegion region;

    private final TeamTag tag;

    public PreArenaTeam(String name, int size, ArenaGetterRegion region, TeamTag tag) {
        super(ProBending.plugin, name, size);
        this.tag = tag;
        this.region = region;
    }

    @Override
    public boolean onAddPlayer(MenuPlayer player) {
        if (ProBending.teamM.getArenaTeam(player.getPlayer()) != null) {
            ProBending.teamM.getTempTeam(player.getPlayer()).removePlayer(player.getPlayer());
            ProBending.teamM.getArenaTeam(player.getPlayer()).removePlayer(player);
        }
        ProBending.teamM.PREARENATEAM_BY_PLAYER.put(player.getPlayer(), this);
        return true;
    }

    @Override
    public boolean onRemovePlayer(MenuPlayer player) {
        ProBending.teamM.PREARENATEAM_BY_PLAYER.remove(player.getPlayer(), this);
        player.unregister();
        return true;
    }

    @Override
    public boolean onPurgePlayers() {
        return true;
    }

    @Override
    public void sendMessage(String message) {
        for (MenuPlayer player : getPlayers()) {
            player.getPlayer().sendMessage(message);
        }
    }

    @Override
    public void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        for (MenuPlayer player : getPlayers()) {
            player.getPlayer().sendTitle(title, subtitle, fadeIn, stay, fadeOut);
        }
    }

    @Language("Team.PreArena.TEAM_INFO")
    public static String LANG_TEAM_INFO = "--- %team_name% ---||Size: %team_size%||%team_players%||-------------------";

    @Language("Team.PreArena.EMPTY_SLOT")
    public static String LANG_EMPTY_SLOT = "&7 - NONE||";

    @Language("Team.PreArena.PLAYER_SLOT")
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

    public ArenaGetterRegion getRegion() {
        return region;
    }

    @Override
    protected String _onPlaceholderRequest(String message) {
        message = message.toLowerCase();
        if ("color".equals(message)) {
            return tag.getColor();
        }
        return null;
    }
}
