package com.probending.probending.command.pbteam;

import com.probending.probending.command.abstractclasses.BaseCommand;
import com.probending.probending.core.annotations.Language;
import com.probending.probending.core.players.PBPlayer;
import com.probending.probending.core.team.PBTeam;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
}
