package com.probending.probending.command.abstractclasses;

import org.bukkit.command.CommandSender;

import java.util.List;

public abstract class BaseSubCommand extends AbstractSubCommand {

    @Override
    public boolean matching(List<String> args) {
        if (!args.isEmpty()) {
            return args.get(0).equalsIgnoreCase(getName()) || getAliases().contains(args.get(0).toLowerCase());
        }
        return false;
    }

    @Override
    public void executeCommand(CommandSender sender, List<String> args) {
        execute(sender, args.subList(1, args.size()));
    }
}
