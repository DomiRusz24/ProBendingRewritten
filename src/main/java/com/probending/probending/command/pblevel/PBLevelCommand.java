package com.probending.probending.command.pblevel;

import com.probending.probending.ProBending;
import com.probending.probending.command.abstractclasses.BaseCommand;
import com.probending.probending.command.abstractclasses.Command;
import com.probending.probending.command.abstractclasses.PlayerCommand;
import com.probending.probending.core.annotations.Language;
import com.probending.probending.core.players.PBPlayer;
import com.probending.probending.managers.PAPIManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class PBLevelCommand extends PlayerCommand {

    @Language("Command.PBLevel.Description")
    public static String LANG_DESCRIPTION = "Allows you to view your and other players statistics.";

    @Language("Command.PBLeve.PlayerLevel")
    public static String LANG_INFORMATION = " --- %player_name% ---||Kills: %probending_kills%||Wins: %probending_wins%||Loses: %probending_loses%||W/L ratio: %probending_average%||Ties: %probending_ties||----------------------";

    @Override
    public void selfExecute(CommandSender sender) {
        if (isPlayer(sender)) {
            PBPlayer player = ProBending.playerM.getPlayer((Player) sender);
            sender.sendMessage(PAPIManager.setPlaceholders(player, LANG_INFORMATION));
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
    protected String usage() {
        return "/pblevel ";
    }

    @Override
    protected String description() {
        return LANG_DESCRIPTION;
    }

    @Override
    public void selfExecute(CommandSender sender, Player player) {
        PBPlayer p = ProBending.playerM.getPlayer(player);
        sender.sendMessage(PAPIManager.setPlaceholders(p, LANG_INFORMATION));
    }

    @Override
    public void selfExecute(CommandSender sender, String name) {
        sender.sendMessage(Command.LANG_PLAYER_NOT_ONLINE.replaceAll("%player%", name));
    }
}
