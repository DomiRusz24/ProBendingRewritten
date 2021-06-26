package com.probending.probending.command.abstractclasses;

import org.bukkit.command.CommandSender;

import java.util.List;

public abstract class AbstractSubCommand extends Command {

    private AbstractCommand<?> originalCommand;

    public void setOriginalCommand(AbstractCommand<?> originalCommand) {
        this.originalCommand = originalCommand;
    }

    @Override
    protected String getPermission() {
        return originalCommand.getPermission() + "." + getName();
    }

    @Override
    public String getUsage() {
        return originalCommand.getUsage() + usage();
    }

    public abstract boolean matching(List<String> args);

    public abstract void executeCommand(CommandSender sender, List<String> args);

    protected abstract void execute(CommandSender sender, List<String> args);

    public abstract List<String> autoComplete(CommandSender sender, List<String> args);
}
