package com.probending.probending.core.players;

import com.probending.probending.ProBending;
import com.probending.probending.core.annotations.Language;
import com.probending.probending.core.interfaces.PlaceholderObject;
import com.probending.probending.core.team.PBTeam;
import com.probending.probending.core.team.TeamInvite;
import com.probending.probending.managers.PAPIManager;
import com.probending.probending.managers.database.DataBaseManager;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PBPlayer implements PlaceholderObject {

    // Player used for outside of game, just data.

    private final String uuid;
    private final String name;

    private int wins;
    private int lost;
    private int kills;
    private int ties;
    private String team;

    private TeamInvite invite;

    public PBPlayer(String uuid, String name, int wins, int lost, int kills, int ties, String team) {
        this.uuid = uuid;
        this.name = name;
        this.wins = wins;
        this.lost = lost;
        this.kills = kills;
        this.ties = ties;
        this.team = team;
        ProBending.playerM.addPBPlayer(this);
    }

    // ----------

    public static PBPlayer of(Player player) {
        return ProBending.playerM.getPlayer(player);
    }

    public static PBPlayer of(UUID uuid) {
        return ProBending.playerM.getPlayer(uuid);
    }

    public static PBPlayer of(String name) {
        return ProBending.playerM.getPlayer(name);
    }

    // ----------

    public String getName() {
        return name;
    }

    public String getUuid() {
        return uuid;
    }

    // ----------

    public void setLost(int lost) {
        this.lost = lost;
        ProBending.SqlM.playerTable.setIntegerField(uuid, ProBending.SqlM.playerTable.LOST, lost);
    }

    public void setWins(int wins) {
        this.wins = wins;
        ProBending.SqlM.playerTable.setIntegerField(uuid, ProBending.SqlM.playerTable.WINS, wins);
    }

    public void setKills(int kills) {
        this.kills = kills;
        ProBending.SqlM.playerTable.setIntegerField(uuid, ProBending.SqlM.playerTable.KILLS, kills);
    }

    public void setTies(int ties) {
        this.ties = ties;
        ProBending.SqlM.playerTable.setIntegerField(uuid, ProBending.SqlM.playerTable.TIES, ties);
    }

    public void setTeam(String team) {
        if (!team.equals(this.team)) {
            this.team = team;
            ProBending.SqlM.playerTable.setStringField(uuid, ProBending.SqlM.playerTable.TEAM, team);
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
            return PlaceholderAPI.setPlaceholders(Bukkit.getOfflinePlayer(UUID.fromString(uuid)), param);
        }
    }

    @Override
    public String placeHolderPrefix() {
        return "probending";
    }

    // ----------
}
