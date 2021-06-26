package com.probending.probending.command.abstractclasses;

import com.probending.probending.ProBending;
import com.probending.probending.core.arena.Arena;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class PlayerSubCommand extends AbstractSubCommand {
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
            Player player = Bukkit.getPlayer(name);
            if (player != null) {
                execute(sender, player, args.subList(2, args.size()));
            } else {
                execute(sender, name, args.subList(2, args.size()));
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

    public abstract void execute(CommandSender sender, Player player, List<String> args);

    public abstract void execute(CommandSender sender, String name, List<String> args);

    public abstract List<String> autoComplete(CommandSender sender, Player player, List<String> args);
}
