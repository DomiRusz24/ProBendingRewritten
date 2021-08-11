package com.probending.probending.command.base.probending;

import com.probending.probending.command.abstractclasses.BaseSubCommand;
import me.domirusz24.plugincore.config.annotations.Language;
import com.probending.probending.core.arena.ActiveArena;
import com.probending.probending.core.arena.Arena;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class RulesCommand extends BaseSubCommand {

    @Language("Command.ProBending.Rules.Description")
    public static String LANG_DESCRIPTION = "Displays arena rules.";

    @Override
    public void execute(CommandSender sender, List<String> args) {
        if (hasPermission(sender)) {
            if (correctLength(sender, args.size(), 0, 0)) {
                sender.sendMessage(Arena.getArenaRules());
            }
        }
    }

    @Override
    public List<String> autoComplete(CommandSender sender, List<String> args) {
        return new ArrayList<>();
    }

    @Override
    protected String name() {
        return "rules";
    }

    @Override
    protected String usage() {
        return "rules";
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
