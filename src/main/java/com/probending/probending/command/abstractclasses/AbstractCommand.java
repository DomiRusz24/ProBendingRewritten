package com.probending.probending.command.abstractclasses;

import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCommand<T extends AbstractSubCommand> extends Command {

    private final ArrayList<T> subCommands = new ArrayList<>();

    public boolean canExecuteSubCommands(CommandSender sender, List<String> args) {
        return true;
    }

    public boolean canSelfExecute(CommandSender sender, List<String> args) {
        return args.isEmpty();
    }

    public void selfExecute(CommandSender sender, List<String> args) {
        selfExecute(sender);
    }

    public abstract void selfExecute(CommandSender sender);

    public abstract List<String> autoComplete(CommandSender sender, List<String> args);

    public String getAllHelp() {
        StringBuilder help = new StringBuilder();
        for (AbstractSubCommand subCommand : getSubCommands()) {
            help.append(" - ").append(subCommand.getUsage());
            help.append('\n');
        }
        return help.toString();
    }

    @Override
    public void help(CommandSender sender, boolean description) {
        sender.sendMessage(LANG_USAGE);
        sender.sendMessage(getAllHelp());
        if (description) {
            sender.sendMessage(Command.LANG_COMMAND_DESCRIPTION + description());
        }

    }

    public AbstractCommand<T> addSubCommand(T subCommand) {
        this.subCommands.add(subCommand);
        subCommand.setOriginalCommand(this);
        return this;
    }

    public ArrayList<T> getSubCommands() {
        return subCommands;
    }


}
