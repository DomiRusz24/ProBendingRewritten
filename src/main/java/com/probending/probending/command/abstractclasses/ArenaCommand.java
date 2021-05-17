package com.probending.probending.command.abstractclasses;

import com.probending.probending.ProBending;
import com.probending.probending.core.arena.Arena;
import com.probending.probending.core.enums.TeamTag;
import org.bukkit.command.CommandSender;

import java.util.List;

public abstract class ArenaCommand extends AbstractCommand<ArenaSubCommand> {

    @Override
    public boolean canExecuteSubCommands(CommandSender sender, List<String> args) {
        boolean canExecute = false;
        if (args.size() > 1) {
            String name = args.get(0);
            Arena arena = ProBending.arenaM.getArena(name);
            canExecute = arena != null;
            if (!canExecute) {
                sender.sendMessage(Command.LANG_ARENA_DOES_NOT_EXIST.replaceAll("%arena%", name));
            }
        }
        return canExecute;
    }

    @Override
    public boolean canSelfExecute(CommandSender sender, List<String> args) {
        return args.size() < 2;
    }

    @Override
    public void selfExecute(CommandSender sender, List<String> args) {
        if (args.isEmpty()) {
            selfExecute(sender);
            return;
        }
        String name = args.get(0);
        Arena arena = ProBending.arenaM.getArena(name);
        if (arena != null) {
            selfExecute(sender, arena);
        } else {
            sender.sendMessage(Command.LANG_ARENA_DOES_NOT_EXIST.replaceAll("%arena%", name));
        }
    }

    public abstract void selfExecute(CommandSender sender, Arena arena);
}
