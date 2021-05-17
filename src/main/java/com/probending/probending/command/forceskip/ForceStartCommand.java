package com.probending.probending.command.forceskip;

import com.probending.probending.ProBending;
import com.probending.probending.command.abstractclasses.BaseCommand;
import com.probending.probending.command.abstractclasses.Command;
import com.probending.probending.core.annotations.Language;
import com.probending.probending.core.players.MenuPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class ForceStartCommand extends BaseCommand {

    @Language("Command.ForceStart")
    public static String LANG_DESCRIPTION = "Allows you to force start an arena.";

    @Override
    public void selfExecute(CommandSender sender) {
        if (isPlayer(sender) && hasPermission(sender)) {
            Player player = (Player) sender;
            MenuPlayer mPlayer = ProBending.playerM.getMenuPlayer(player);
            if (mPlayer != null) {
                mPlayer.setVoteSkip(true);
                sender.sendMessage(Command.LANG_SUCCESS);
            } else {
                sender.sendMessage(Command.LANG_FAIL);
            }
        }
    }

    @Override
    protected String name() {
        return "forcestart";
    }

    @Override
    protected List<String> aliases() {
        return Collections.singletonList("fs");
    }

    @Override
    protected String usage() {
        return "/fs";
    }

    @Override
    protected String description() {
        return LANG_DESCRIPTION;
    }
}
