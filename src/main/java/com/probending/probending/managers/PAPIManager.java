package com.probending.probending.managers;

import com.probending.probending.ProBending;
import com.probending.probending.core.interfaces.PlaceholderObject;
import com.probending.probending.core.players.ActivePlayer;
import com.probending.probending.core.players.PBPlayer;
import com.probending.probending.util.UtilMethods;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderHook;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityHeadRotation;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PAPIManager extends PlaceholderExpansion {

    private final ProBending plugin;

    public PAPIManager(ProBending plugin) {
        this.plugin = plugin;
        PlaceholderAPI.registerExpansion(this);
    }

    @Override
    public boolean persist(){
        return true;
    }

    @Override
    public boolean canRegister(){
        return true;
    }

    @Override
    public String getAuthor(){
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public String getIdentifier(){
        return "probending";
    }

    @Override
    public String getVersion(){
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, String params){

        if(player == null) {
            return "";
        }
        params = params.toLowerCase();
        ActivePlayer activePlayer = ProBending.playerM.getActivePlayer(player);
        if (activePlayer != null) {
            switch (params) {
                case "element":
                    return activePlayer.getElementPrefix();
                case "state":
                    return activePlayer.getState().toString();
                case "tired":
                    return String.valueOf(activePlayer.getTiredness());
            }
        }
        PBPlayer pbPlayer = ProBending.playerM.getPlayer(player);
        switch (params) {
            case "kills":
                return String.valueOf(pbPlayer.getKills());
            case "wins":
                return String.valueOf(pbPlayer.getWins());
            case "loses":
                return String.valueOf(pbPlayer.getLost());
            case "ties":
                return String.valueOf(pbPlayer.getTies());
            case "average":
                return String.valueOf((float) ((int) (((float) pbPlayer.getWins() / ((float) pbPlayer.getLost() + (float) pbPlayer.getTies())) * 100)) * 0.01);
        }
        return null;
    }

    // setting PlaceHolders

    public static String setPlaceholders(PlaceholderObject object, String text) {
        if (text == null) {
            return null;
        } else {
            if (object.placeHolderPrefix().equals("probending")) {
                text = object.onPlaceholderRequest(text);
            } else {
                Matcher m = PlaceholderAPI.getPlaceholderPattern().matcher(text);
                while (m.find()) {
                    String format = m.group(1);
                    if (format.startsWith(object.placeHolderPrefix())) {
                        int index = format.indexOf("_");
                        if (index > 0 && index < format.length()) {
                            String params = format.substring(index + 1);
                            String value = object.onPlaceholderRequest(params);
                            if (value != null) {
                                text = text.replaceAll(Pattern.quote(m.group()), Matcher.quoteReplacement(value));
                            } else {
                                text = text.replaceAll(Pattern.quote(m.group()), "null");
                            }
                        }
                    }
                }
            }
            return UtilMethods.translateColor(text);
        }
    }
}
