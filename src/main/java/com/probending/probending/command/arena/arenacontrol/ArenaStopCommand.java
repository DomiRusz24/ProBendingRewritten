package com.probending.probending.command.arena.arenacontrol;

import com.probending.probending.command.abstractclasses.ArenaSubCommand;
import com.probending.probending.command.abstractclasses.Command;
import me.domirusz24.plugincore.config.annotations.Language;
import com.probending.probending.core.arena.Arena;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class ArenaStopCommand extends ArenaSubCommand {

    @Language("Command.ArenaControl.Stop.Description")
    public static String LANG_DESCRIPTION = "Allows you to stop the arena.";

    @Override
    public void execute(CommandSender sender, Arena arena, List<String> args) {
        if (hasPermission(sender)) {
            if (correctLength(sender, args.size(), 0, 0)) {
                if (arena.inGame()) {
                    arena.stop();
                } else {
                    sender.sendMessage(Command.LANG_ARENA_NOT_IN_GAME);
                }
            }
        }
    }

    @Override
    public List<String> autoComplete(CommandSender sender, Arena arena, List<String> args) {
        return null;
    }

    @Override
    protected String name() {
        return "stop";
    }

    @Override
    protected String usage() {
        return "stop";
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
