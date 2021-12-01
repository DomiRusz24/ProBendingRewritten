package com.probending.probending.command.base.probending;

import com.probending.probending.ProBending;
import com.probending.probending.command.Languages;
import me.domirusz24.plugincore.PluginCore;
import me.domirusz24.plugincore.command.abstractclasses.BaseSubCommand;
import me.domirusz24.plugincore.config.annotations.Language;
import com.probending.probending.core.arena.Arena;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

import java.util.ArrayList;
import java.util.List;

public class SetSpawnCommand extends BaseSubCommand {

    @Language("Command.ProBending.SetSpawn.Description")
    public static String LANG_DESCRIPTION = "Sets the lobby spawn for all arenas.";

    @Override
    protected String name() {
        return "setspawn";
    }

    @Override
    protected String usage() {
        return "setspawn";
    }

    @Override
    protected String description() {
        return LANG_DESCRIPTION;
    }

    @Override
    protected void execute(CommandSender sender, List<String> args) {
        if (isPlayer(sender)) {
            if (correctLength(sender, args.size(), 0, 0)) {
                ProBending.locationConfig.setSpawn(((Player) sender).getLocation());
                sender.sendMessage(Languages.SUCCESS);
            }
        }
    }

    @Override
    public List<String> autoComplete(CommandSender sender, List<String> args) {
        return new ArrayList<>();
    }

    @Override
    public PermissionDefault getPermissionDefault() {
        return null;
    }

    @Override
    public PluginCore getCorePlugin() {
        return ProBending.plugin;
    }
}
