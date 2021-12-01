package com.probending.probending.command.pbteam.pbteam;

import com.probending.probending.ProBending;
import com.probending.probending.command.Languages;
import me.domirusz24.plugincore.PluginCore;
import me.domirusz24.plugincore.command.abstractclasses.BaseSubCommand;
import me.domirusz24.plugincore.config.annotations.Language;
import com.probending.probending.core.players.PBPlayer;
import com.probending.probending.core.team.PBTeam;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

import java.util.ArrayList;
import java.util.List;

public class TeamLeaveCommand extends BaseSubCommand {

    @Language("Command.Team.Leave.Description")
    public static String LANG_DESCRIPTION = "Allows you to leave your team.";

    @Override
    protected void execute(CommandSender sender, List<String> args) {
        if (isPlayer(sender) && correctLength(sender, args.size(), 0, 0) && hasPermission(sender)) {
            PBPlayer player = PBPlayer.of((Player) sender);
            PBTeam team = player.getTeam();
            if (team != null) {
                team.removePlayer(player);
                sender.sendMessage(Languages.SUCCESS);
            } else {
                sender.sendMessage(InviteTeamCommand.LANG_NOT_IN_TEAM);
            }
        }
    }

    @Override
    public List<String> autoComplete(CommandSender sender, List<String> args) {
        return new ArrayList<>();
    }

    @Override
    protected String name() {
        return "leave";
    }

    @Override
    protected String usage() {
        return "leave";
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
