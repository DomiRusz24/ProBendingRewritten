package com.probending.probending.command.forceskip;

import com.probending.probending.ProBending;

import com.probending.probending.command.Languages;
import me.domirusz24.plugincore.PluginCore;
import me.domirusz24.plugincore.command.abstractclasses.BaseCommand;
import me.domirusz24.plugincore.config.annotations.Language;
import com.probending.probending.core.players.MenuPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

import java.util.Collections;
import java.util.List;

public class ForceStartCommand extends BaseCommand {

    @Language("Command.ForceStart")
    public static String LANG_DESCRIPTION = "Allows you to force start an arena.";

    @Override
    public void selfExecute(CommandSender sender) {
        if (isPlayer(sender)) {
            Player player = (Player) sender;
            MenuPlayer mPlayer = ProBending.playerM.getMenuPlayer(player);
            if (mPlayer != null) {
                if (!mPlayer.getVoteSkip()) {
                    mPlayer.setVoteSkip(true);
                }
            } else {
                sender.sendMessage(Languages.FAIL);
            }
        }
    }

    @Override
    protected String name() {
        return "forcestart";
    }

    @Override
    protected List<String> aliases() {
        return Collections.singletonList("fs");
    }

    @Override
    protected String usage() {
        return "/fs";
    }

    @Override
    protected String description() {
        return LANG_DESCRIPTION;
    }

    @Override
    public PermissionDefault getPermissionDefault() {
        return PermissionDefault.TRUE;
    }

    @Override
    public PluginCore getCorePlugin() {
        return ProBending.plugin;
    }
}
