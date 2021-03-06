package com.probending.probending.command.pblevel;

import com.probending.probending.ProBending;
import com.probending.probending.command.Languages;
import me.domirusz24.plugincore.PluginCore;
import me.domirusz24.plugincore.command.abstractclasses.PlayerCommand;
import me.domirusz24.plugincore.config.annotations.Language;
import com.probending.probending.core.players.PBPlayer;
import com.probending.probending.managers.PAPIManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

import java.util.Arrays;
import java.util.List;

public class PBLevelCommand extends PlayerCommand {

    @Language("Command.PBLevel.Description")
    public static String LANG_DESCRIPTION = "Allows you to view your and other players statistics.";

    @Language("Command.PBLeve.PlayerLevel")
    public static String LANG_INFORMATION = " --- %player_name% ---||Kills: %probending_kills%||Wins: %probending_wins%||Loses: %probending_loses%||W/L ratio: %probending_average%||Ties: %probending_ties||----------------------";

    @Override
    public void selfExecute(CommandSender sender) {
        if (isPlayer(sender) && hasPermission(sender)) {
            PBPlayer player = (PBPlayer) ProBending.playerDataM.getPlayer((Player) sender);
            sender.sendMessage(PAPIManager.setPlaceholder(player, LANG_INFORMATION));
        }
    }

    @Override
    protected String name() {
        return "pblevel";
    }

    @Override
    protected List<String> aliases() {
        return Arrays.asList("pblvl", "pbl", "level");
    }

    @Override
    public PermissionDefault getPermissionDefault() {
        return PermissionDefault.TRUE;
    }

    @Override
    protected String usage() {
        return "/pblevel ";
    }

    @Override
    protected String description() {
        return LANG_DESCRIPTION;
    }

    @Override
    public void selfExecute(CommandSender sender, Player player) {
        PBPlayer p = (PBPlayer) ProBending.playerDataM.getPlayer(player);
        sender.sendMessage(PAPIManager.setPlaceholder(p, LANG_INFORMATION));
    }

    @Override
    public void selfExecute(CommandSender sender, String name) {
        sender.sendMessage(Languages.PLAYER_NOT_ONLINE.replaceAll("%player%", name));
    }

    @Override
    public PluginCore getCorePlugin() {
        return ProBending.plugin;
    }
}
