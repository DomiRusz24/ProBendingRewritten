package com.probending.probending.core;

import com.probending.probending.ProBending;
import com.projectkorra.projectkorra.BendingPlayer;
import org.bukkit.entity.Player;

public class ActivePlayer {

    // Player used for in-game.

    private Player player;

    private BendingPlayer bPlayer;

    private PBPlayer playerData;

    public ActivePlayer(Player player) {
        this.player = player;
        bPlayer = BendingPlayer.getBendingPlayer(player);
        playerData = ProBending.playerM.getPlayer(player.getUniqueId());
    }

    public PBPlayer getPlayerData() {
        return playerData;
    }

    public Player getPlayer() {
        return player;
    }

    public BendingPlayer getBendingPlayer() {
        return bPlayer;
    }
}
