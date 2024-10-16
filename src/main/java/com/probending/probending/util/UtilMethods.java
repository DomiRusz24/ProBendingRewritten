package com.probending.probending.util;

import com.probending.probending.ProBending;
import com.probending.probending.core.players.PBPlayer;
import com.probending.probending.core.team.PBTeam;
import com.projectkorra.projectkorra.BendingPlayer;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class UtilMethods extends me.domirusz24.plugincore.util.UtilMethods {

    public UtilMethods(me.domirusz24.plugincore.util.UtilMethods utilMethods) {
        super(utilMethods);
    }

    public static PBTeam getSamePBTeam(PBPlayer... players) {
        return getSamePBTeam(Arrays.asList(players));
    }

    public static PBTeam getSamePBTeam(List<PBPlayer> players) {
        PBTeam team = null;
        for (PBPlayer player : players) {
            if (player == null) {
                System.out.println("PBPlayer is null somewhere");
                continue;
            }
            if (team == null) {
                team = player.getTeam();
            } else if (team != player.getTeam()) {
                return null;
            }
        }
        return team;
    }

    public static void freezePlayer(Player player, boolean freeze) {
        ProBending.playerM.freezePlayer(player, freeze);
        BendingPlayer bp = ProBending.projectKorraM.getBendingPlayer(player);
        if (freeze) {
            if (bp != null) bp.blockChi();
        } else {
            if (bp != null) bp.unblockChi();
        }
    }


}
