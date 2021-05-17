package com.probending.probending.core.players;

import com.probending.probending.ProBending;
import com.probending.probending.core.interfaces.PlaceholderObject;
import com.probending.probending.managers.database.DataBaseManager;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
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

    public PBPlayer(String uuid, String name, int wins, int lost, int kills, int ties) {
        this.uuid = uuid;
        this.name = name;
        this.wins = wins;
        this.lost = lost;
        this.kills = kills;
        this.ties = ties;
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
        ProBending.SqlM.playerTable.setIntegerField(uuid, ProBending.SqlM.playerTable.WINS, lost);
    }

    public void setKills(int kills) {
        this.kills = kills;
        ProBending.SqlM.playerTable.setIntegerField(uuid, ProBending.SqlM.playerTable.KILLS, lost);
    }

    public void setTies(int ties) {
        this.ties = ties;
        ProBending.SqlM.playerTable.setIntegerField(uuid, ProBending.SqlM.playerTable.TIES, lost);
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

    @Override
    public String onPlaceholderRequest(String param) {
        return PlaceholderAPI.setPlaceholders(Bukkit.getOfflinePlayer(name), param);
    }

    @Override
    public String placeHolderPrefix() {
        return "probending";
    }

    // ----------
}
