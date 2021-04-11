package com.probending.probending.core;

import com.probending.probending.ProBending;
import com.probending.probending.managers.MySQLManager;

import java.util.UUID;

public class PBPlayer {

    // Player used for outside of game, just data.


    private String uuid;
    private String name;

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

    public String getName() {
        return name;
    }

    public String getUuid() {
        return uuid;
    }

    public void setLost(int lost) {
        this.lost = lost;
        ProBending.SQLManager.setIntegerField(UUID.fromString(uuid), MySQLManager.LOST, lost);
    }

    public void setWins(int wins) {
        this.wins = wins;
        ProBending.SQLManager.setIntegerField(UUID.fromString(uuid), MySQLManager.WINS, wins);
    }

    public void setKills(int kills) {
        this.kills = kills;
        ProBending.SQLManager.setIntegerField(UUID.fromString(uuid), MySQLManager.KILLS, kills);
    }

    public void setTies(int ties) {
        this.ties = ties;
        ProBending.SQLManager.setIntegerField(UUID.fromString(uuid), MySQLManager.TIES, ties);
    }

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
}
