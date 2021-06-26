package com.probending.probending.command.abstractclasses;

import com.probending.probending.util.UtilMethods;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class BaseCommand extends AbstractCommand<BaseSubCommand> {

    @Override
    public List<String> autoComplete(CommandSender sender, List<String> args) {
        List<String> complete = new ArrayList<>();
        if (args.size() == 1) {
            complete.addAll(getSubCommands().stream().map(Command::getName).collect(Collectors.toList()));
        } else {
            for (BaseSubCommand command : getSubCommands()) {
                if (command.getName().equalsIgnoreCase(args.get(0)) || command.getAliases().contains(args.get(0).toLowerCase())) {
                    return command.autoComplete(sender, args.subList(1, args.size()));
                }
            }
        }
        return UtilMethods.getPossibleCompletions(args, complete);
    }
}
