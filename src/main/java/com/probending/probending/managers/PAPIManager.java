package com.probending.probending.managers;

import com.probending.probending.ProBending;
import com.probending.probending.core.arena.Arena;
import me.domirusz24.plugincore.attributes.PlayerAttribute;
import me.domirusz24.plugincore.config.annotations.Language;
import me.domirusz24.plugincore.core.placeholders.PlaceholderObject;
import com.probending.probending.core.players.ActivePlayer;
import com.probending.probending.core.players.PBPlayer;
import com.probending.probending.core.team.PBTeam;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderHook;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.domirusz24.plugincore.util.UtilMethods;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PAPIManager extends me.domirusz24.plugincore.managers.PAPIManager {


    public PAPIManager(ProBending plugin) {
        super(plugin);
    }

    @Override
    protected String onPlaceholderRequest(String s, String s1) {
        return null;
    }

    public String onMiscPlaceholderRequest(String params) {
        if (params.startsWith("arena")) {
            String arenaName = params.split("_")[1];
            Arena arena = ProBending.arenaM.getArena(arenaName);
            if (arena == null) return null;
            return arena.onPlaceholderRequest("%" + params.replaceAll("arena." + arenaName, "") + "%");
        }
        return null;
    }

    @Override
    public String onPlaceholderRequest(Player player, String params) {

        if(player == null) {
            return onMiscPlaceholderRequest(params);
        }
        params = params.toLowerCase();
        ActivePlayer activePlayer = ProBending.playerM.getActivePlayer(player);
        if (activePlayer != null) {
            switch (params) {
                case "arena":
                    return activePlayer.getArena().getArena().getName();
                case "element":
                    return activePlayer.getElementPrefix();
                case "state":
                    return activePlayer.getState().toString();
                case "tired":
                    return String.valueOf(activePlayer.getTiredness());
            }
        }
        PBPlayer pbPlayer = (PBPlayer) ProBending.playerDataM.getPlayer(player);
        if (!pbPlayer.isSqlLoaded()) return "NOT LOADED";
        return setPBPlayerPlaceHolders(pbPlayer, params);
    }


    private static String setPBPlayerPlaceHolders(PBPlayer pbPlayer, String params) {
        switch (params) {
            case "name":
                return pbPlayer.getName();
            case "kills":
                return String.valueOf(pbPlayer.getKills());
            case "wins":
                return String.valueOf(pbPlayer.getWins());
            case "loses":
                return String.valueOf(pbPlayer.getLost());
            case "ties":
                return String.valueOf(pbPlayer.getTies());
            case "average":
                return String.valueOf((float) Math.round(((float) pbPlayer.getWins() / ((float) pbPlayer.getLost() + (float) pbPlayer.getTies())) * 100) * 0.01);
            case "team":
                return pbPlayer.getTeamName();
        }
        return null;
    }

    public static String setPlaceholder(PlaceholderObject object, String text) {
        return ProBending.papiM.setPlaceholders(object, text);
    }
}
