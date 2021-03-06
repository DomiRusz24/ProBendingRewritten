package com.probending.probending.command;

import com.probending.probending.ProBending;
import com.probending.probending.core.arena.Arena;
import me.domirusz24.plugincore.command.abstractclasses.AbstractSubCommand;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public abstract class ArenaSubCommand extends AbstractSubCommand {
    @Override
    public boolean matching(List<String> args) {
        if (args.size() > 1) {
            return args.get(1).equalsIgnoreCase(getName()) || getAliases().contains(args.get(1).toLowerCase());
        }
        return false;
    }

    @Override
    protected void execute(CommandSender sender, List<String> args) {
        if (args.size() > 1) {
            String name = args.get(0);
            Arena arena = ProBending.arenaM.getArena(name);
            if (arena != null) {
                execute(sender, arena, args.subList(2, args.size()));
            }
        }
    }

    @Override
    public List<String> autoComplete(CommandSender sender, List<String> args) {
        return new ArrayList<>();
    }

    @Override
    public void executeCommand(CommandSender sender, List<String> args) {
        execute(sender, args);
    }

    public abstract void execute(CommandSender sender, Arena arena, List<String> args);

    public abstract List<String> autoComplete(CommandSender sender, Arena arena, List<String> args);
}
