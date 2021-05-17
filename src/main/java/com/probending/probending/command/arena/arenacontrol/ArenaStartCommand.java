package com.probending.probending.command.arena.arenacontrol;

import com.probending.probending.command.abstractclasses.ArenaSubCommand;
import com.probending.probending.command.abstractclasses.Command;
import com.probending.probending.core.annotations.Language;
import com.probending.probending.core.arena.Arena;
import com.probending.probending.core.enums.GameType;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

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
                    arena.start(GameType.DEFAULT, false);
                    if (arena.inGame()) {
                        sender.sendMessage(Command.LANG_SUCCESS);
                    } else {
                        sender.sendMessage(Command.LANG_FAIL);
                    }
                } else if (args.size() == 1) {
                    GameType type = GameType.getFromName(args.get(0));
                    if (type != null) {
                        arena.start(type, false);
                        if (arena.inGame()) {
                            sender.sendMessage(Command.LANG_SUCCESS);
                        } else {
                            sender.sendMessage(Command.LANG_FAIL);
                        }
                    } else {
                        help(sender, false);
                    }
                } else if (args.size() == 2) {
                    if (args.get(1).equalsIgnoreCase("force")) {
                        GameType type = GameType.getFromName(args.get(0));
                        if (type != null) {
                            arena.start(type, true);
                            if (arena.inGame()) {
                                sender.sendMessage(Command.LANG_SUCCESS);
                            } else {
                                sender.sendMessage(Command.LANG_FAIL);
                            }
                        } else {
                            help(sender, true);
                        }
                    }
                }
            }
        }
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
