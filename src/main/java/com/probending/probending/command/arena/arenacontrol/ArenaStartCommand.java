package com.probending.probending.command.arena.arenacontrol;

import com.probending.probending.command.abstractclasses.ArenaSubCommand;
import com.probending.probending.command.abstractclasses.Command;
import me.domirusz24.plugincore.config.annotations.Language;
import com.probending.probending.core.arena.Arena;
import com.probending.probending.util.UtilMethods;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ArenaStartCommand extends ArenaSubCommand {

    @Language("Command.ArenaControl.Start.Description")
    public static String LANG_DESCRIPTION = "Allows you to start the arena.";

    @Override
    public void execute(CommandSender sender, Arena arena, List<String> args) {
        if (hasPermission(sender)) {
            if (correctLength(sender, args.size(), 0, 2)) {
                if (arena.inGame()) {
                    sender.sendMessage(Command.LANG_ARENA_IN_GAME);
                } else if (args.size() == 0) {
                    arena.start(false);
                    if (arena.inGame()) {
                        sender.sendMessage(Command.LANG_SUCCESS);
                    } else {
                        sender.sendMessage(Command.LANG_FAIL);
                    }
                } else if (args.size() == 1) {
                    if (args.get(0).equalsIgnoreCase("force")) {
                        arena.start(true);
                        if (arena.inGame()) {
                            sender.sendMessage(Command.LANG_SUCCESS);
                        } else {
                            sender.sendMessage(Command.LANG_FAIL);
                        }
                    } else {
                        help(sender, false);
                    }
                }
            }
        }
    }

    @Override
    public List<String> autoComplete(CommandSender sender, Arena arena, List<String> args) {
        List<String> complete = new ArrayList<>();
        if (args.size() == 1) {
            complete.add("force");
        }
        return UtilMethods.getPossibleCompletions(args, complete);
    }

    @Override
    protected String name() {
        return "start";
    }

    @Override
    protected String usage() {
        return "start <gametype> <force>";
    }

    @Override
    protected String description() {
        return LANG_DESCRIPTION;
    }

    @Override
    protected List<String> aliases() {
        return new ArrayList<>();
    }
}
