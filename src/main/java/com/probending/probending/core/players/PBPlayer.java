package com.probending.probending.core.players;

import com.probending.probending.ProBending;
import me.domirusz24.plugincore.attributes.PlayerAttribute;
import me.domirusz24.plugincore.config.annotations.Language;
import com.probending.probending.core.team.PBTeam;
import com.probending.probending.core.team.TeamInvite;
import me.clip.placeholderapi.PlaceholderAPI;
import me.domirusz24.plugincore.core.placeholders.PlaceholderObject;
import me.domirusz24.plugincore.core.players.PlayerData;
import me.domirusz24.plugincore.managers.database.DataBaseTable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PBPlayer extends PlayerData {

    // Player used for outside of game, just data.

    private int wins;
    private int lost;
    private int kills;
    private int ties;
    private String team;

    private TeamInvite invite;

    public PBPlayer(String name, UUID uuid) {
        super(name, uuid);
    }

    @Override
    protected void onSqlLoad() {

    }

    @Override
    protected void onPlayerJoin() {

    }

    @Override
    public DataBaseTable getTable() {
        return ProBending.playerTable;
    }

    // ----------

    public static PBPlayer of(Player player) {
        return (PBPlayer) ProBending.playerDataM.getPlayer(player);
    }

    // ----------

    public void setLost(int lost) {
        this.lost = lost;
        getAttribute(PlayerAttribute.SQL).setIntegerValue(ProBending.playerTable.LOST, lost);
    }

    public void setWins(int wins) {
        this.wins = wins;
        getAttribute(PlayerAttribute.SQL).setIntegerValue(ProBending.playerTable.WINS, wins);
    }

    public void setKills(int kills) {
        this.kills = kills;
        getAttribute(PlayerAttribute.SQL).setIntegerValue(ProBending.playerTable.KILLS, kills);
    }

    public void setTies(int ties) {
        this.ties = ties;
        getAttribute(PlayerAttribute.SQL).setIntegerValue(ProBending.playerTable.TIES, ties);
    }

    public void setTeam(String team) {
        if (!team.equals(this.team)) {
            this.team = team;
            getAttribute(PlayerAttribute.SQL).setStringValue(ProBending.playerTable.TEAM, team);
        }
    }

    public void setInvite(TeamInvite invite) {
        this.invite = invite;
        invite.send();
    }

    @Language("Team.NoInvite")
    public static String LANG_NO_INVITE = "You have not got any invite!";

    public void acceptInvite() {
        if (invite != null) {
            invite.accept();
            invite = null;
        } else {
            getOnlinePlayer().getPlayer().sendMessage(LANG_NO_INVITE);
        }
    }

    public TeamInvite getInvite() {
        return invite;
    }

    // ----------

    public int getLost() {
        return lost;
    }

    public int getWins() {
        return wins;
    }

    public int getKills() {
        return kills;
    }

    public int getTies() {
        return ties;
    }

    @Language("PBPlayer.NoTeam")
    public static String LANG_NO_TEAM = "None";

    public String getTeamName() {
        return team.equals("NULL") ? LANG_NO_TEAM : team;
    }

    public PBTeam getTeam() {
        return ProBending.teamM.getPBTeam(name);
    }

    // ----------

    @Override
    public int hashCode() {
        return getUuid().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj instanceof PBPlayer) {
            return ((PBPlayer) obj).getUuid().equals(getUuid());
        }
        return false;
    }

    public PBPlayerWrapper getOnlinePlayer() {
        Player player = Bukkit.getPlayer(name);
        if (player != null) {
            return PBPlayerWrapper.of(player);
        } else {
            return null;
        }
    }

    @Override
    public String onPlaceholderRequest(String param) {
        PBPlayerWrapper player = getOnlinePlayer();
        if (player != null) {
            return PlaceholderAPI.setPlaceholders(player.getPlayer(), param);
        } else {
            return PlaceholderAPI.setPlaceholders(Bukkit.getOfflinePlayer(uuid), param);
        }
    }

    @Override
    public String placeHolderPrefix() {
        return "probending";
    }

    // ----------
}
