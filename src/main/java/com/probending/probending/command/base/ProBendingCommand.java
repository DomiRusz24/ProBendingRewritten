package com.probending.probending.command.base;

import com.probending.probending.ProBending;
import com.probending.probending.command.abstractclasses.BaseCommand;
import com.probending.probending.core.annotations.Language;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class ProBendingCommand extends BaseCommand {

    @Language("Command.ProBending.Description")
    public static String LANG_DESCRIPTION = "Main command used for ProBending";

    @Override
    public void selfExecute(CommandSender sender) {
        sender.sendMessage("----------------------------");
        sender.sendMessage("Plugin made by DomiRusz24");
        sender.sendMessage("Version: " + ProBending.plugin.getDescription().getVersion());
        sender.sendMessage("----------------------------");
    }

    @Override
    protected String name() {
        return "probending";
    }

    @Override
    protected String usage() {
        return "/pb ";
    }

    @Override
    protected String description() {
        return LANG_DESCRIPTION;
    }

    @Override
    protected List<String> aliases() {
        return Arrays.asList("pb", "probend");
    }
}