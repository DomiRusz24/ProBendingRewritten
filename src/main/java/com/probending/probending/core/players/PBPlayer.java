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

    private TeamInvite invite;

    public PBPlayer(String name, UUID uuid) {
        super(ProBending.plugin, name, uuid);
    }

    @Override
    protected void onSqlLoad() {
        if (!getTeamName().equals(PBPlayer.LANG_NO_TEAM) && getTeam() == null) {
            ProBending.teamTable.createTeam(getPlayer(), getTeamName());
        }
    }

    @Override
    protected void onPlayerJoin() {
        if (getTeam() != null) {
            for (PBMember player : getTeam().getPlayers()) {
                if (player.getName().equals(getName())) {
                    player.setPBPlayer(this);
                    break;
                }
            }
        }
    }

    @Override
    public void onLeave() {
        if (getTeam() != null) {
            for (PBMember player : getTeam().getPlayers()) {
                if (player.getName().equals(getName())) {
                    player.setPBPlayer(null);
                    break;
                }
            }
        }
        super.onLeave();
    }

    @Override
    public DataBaseTable getTable() {
        return ProBending.playerTable;
    }

    // ----------

    public static PBPlayer of(Player player) {
        return (PBPlayer) ProBending.playerDataM.getPlayer(player);
    }

    public static PBPlayer of(String name, UUID uuid) {
        return (PBPlayer) ProBending.playerDataM.getPlayer(name, uuid);
    }


    public static boolean exists(UUID uuid) {
        return ProBending.playerDataM.exists(uuid);
    }

    // ----------

    public void setLost(int lost) {
        getAttribute(PlayerAttribute.SQL).setIntegerValue(ProBending.playerTable.LOST, lost);
    }

    public void setWins(int wins) {
        getAttribute(PlayerAttribute.SQL).setIntegerValue(ProBending.playerTable.WINS, wins);
    }

    public void setKills(int kills) {
        getAttribute(PlayerAttribute.SQL).setIntegerValue(ProBending.playerTable.KILLS, kills);
    }

    public void setTies(int ties) {
        getAttribute(PlayerAttribute.SQL).setIntegerValue(ProBending.playerTable.TIES, ties);
    }

    public void setTeam(String team) {
        getAttribute(PlayerAttribute.SQL).setStringValue(ProBending.playerTable.TEAM, team);
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

    public boolean isSqlLoaded() {
        return getAttribute(PlayerAttribute.SQL).isReadyToAccess();
    }

    public int getLost() {
        return getAttribute(PlayerAttribute.SQL).getIntegerValue(ProBending.playerTable.LOST);
    }

    public int getWins() {
        return getAttribute(PlayerAttribute.SQL).getIntegerValue(ProBending.playerTable.WINS);
    }

    public int getKills() {
        return getAttribute(PlayerAttribute.SQL).getIntegerValue(ProBending.playerTable.KILLS);
    }

    public int getTies() {
        return getAttribute(PlayerAttribute.SQL).getIntegerValue(ProBending.playerTable.TIES);
    }

    @Language("PBPlayer.NoTeam")
    public static String LANG_NO_TEAM = "None";

    public String getTeamName() {
        String team = getAttribute(PlayerAttribute.SQL).getStringValue(ProBending.playerTable.TEAM);
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
