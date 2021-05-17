package com.probending.probending.command.base.probending;

import com.probending.probending.ProBending;
import com.probending.probending.command.abstractclasses.ArenaSubCommand;
import com.probending.probending.command.abstractclasses.BaseSubCommand;
import com.probending.probending.command.abstractclasses.Command;
import com.probending.probending.core.annotations.Language;
import com.probending.probending.core.arena.Arena;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class SetSpawnCommand extends BaseSubCommand {

    @Language("Command.ProBending.SetSpawn.Description")
    public static String LANG_DESCRIPTION = "Sets the lobby spawn for all arenas.";

    @Override
    protected String name() {
        return "setspawn";
    }

    @Override
    protected String usage() {
        return "setspawn";
    }

    @Override
    protected String description() {
        return LANG_DESCRIPTION;
    }

    @Override
    protected void execute(CommandSender sender, List<String> args) {
        if (isPlayer(sender)) {
            if (correctLength(sender, args.size(), 0, 0)) {
                ProBending.configM.getLocationsConfig().setSpawn(((Player) sender).getLocation());
                sender.sendMessage(Command.LANG_SUCCESS);
            }
        }
    }
}
