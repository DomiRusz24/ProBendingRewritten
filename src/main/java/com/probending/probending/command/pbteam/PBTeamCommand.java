package com.probending.probending.command.pbteam;


import com.probending.probending.ProBending;
import me.domirusz24.plugincore.PluginCore;
import me.domirusz24.plugincore.command.abstractclasses.BaseCommand;
import me.domirusz24.plugincore.config.annotations.Language;
import com.probending.probending.core.players.PBPlayer;
import com.probending.probending.core.team.PBTeam;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

public class PBTeamCommand extends BaseCommand {

    @Language("Command.Team.Description")
    public static String LANG_DESCRIPTION = "Allows you to manage your PB team.";

    @Language("Command.Team.NotInTeam")
    public static String LANG_NOT_IN_TEAM = "You are not in a team!";

    @Override
    public void selfExecute(CommandSender sender) {
        if (isPlayer(sender) && hasPermission(sender)) {
            PBPlayer player = PBPlayer.of((Player) sender);
            PBTeam team = player.getTeam();
            if (team != null) {
                sender.sendMessage(team.getInfo());
            } else {
                sender.sendMessage(LANG_NOT_IN_TEAM);
            }
        }
    }

    @Override
    protected String name() {
        return "team";
    }

    @Override
    protected String usage() {
        return "/team ";
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
