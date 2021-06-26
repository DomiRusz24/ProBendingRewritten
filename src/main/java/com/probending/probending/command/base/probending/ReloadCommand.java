package com.probending.probending.command.base.probending;

import com.probending.probending.ProBending;
import com.probending.probending.command.abstractclasses.BaseSubCommand;
import com.probending.probending.command.abstractclasses.Command;
import com.probending.probending.core.annotations.Language;
import com.probending.probending.managers.ConfigManager;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class ReloadCommand extends BaseSubCommand {

    @Language("Command.ProBending.Reload.Description")
    public static String LANG_DESCRIPTION = "Reloads all configs";


    @Override
    public void execute(CommandSender sender, List<String> args) {
        if (hasPermission(sender)) {
            if (correctLength(sender, args.size(), 0, 0)) {
                if (ProBending.configM.reloadConfigs()) {
                    sender.sendMessage(Command.LANG_SUCCESS);
                } else {
                    sender.sendMessage(Command.LANG_ERROR_PREFIX + Command.LANG_FAIL_ERROR);
                }
            }
        }
    }

    @Override
    public List<String> autoComplete(CommandSender sender, List<String> args) {
        return new ArrayList<>();
    }

    @Override
    protected String name() {
        return "reload";
    }

    @Override
    protected String usage() {
        return "reload";
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
