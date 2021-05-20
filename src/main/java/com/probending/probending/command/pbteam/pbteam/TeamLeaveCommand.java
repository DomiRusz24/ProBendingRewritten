package com.probending.probending.command.pbteam.pbteam;

import com.probending.probending.command.abstractclasses.BaseSubCommand;
import com.probending.probending.command.abstractclasses.Command;
import com.probending.probending.core.annotations.Language;
import com.probending.probending.core.players.PBPlayer;
import com.probending.probending.core.team.PBTeam;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
                sender.sendMessage(Command.LANG_SUCCESS);
            } else {
                sender.sendMessage(InviteTeamCommand.LANG_NOT_IN_TEAM);
            }
        }
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
}
