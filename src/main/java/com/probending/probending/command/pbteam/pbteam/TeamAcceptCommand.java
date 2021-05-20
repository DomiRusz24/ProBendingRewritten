package com.probending.probending.command.pbteam.pbteam;

import com.probending.probending.command.abstractclasses.BaseSubCommand;
import com.probending.probending.core.annotations.Language;
import com.probending.probending.core.players.PBPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class TeamAcceptCommand extends BaseSubCommand {

    @Language("Command.Team.Accept.Description")
    public static String LANG_DESCRIPTION = "Allows you to accept an invite to a team.";

    @Override
    protected void execute(CommandSender sender, List<String> args) {
        if (isPlayer(sender) && hasPermission(sender) && correctLength(sender, args.size(), 0, 0)) {
            PBPlayer pbPlayer = PBPlayer.of((Player) sender);
            pbPlayer.acceptInvite();
        }
    }

    @Override
    protected String name() {
        return "accept";
    }

    @Override
    protected String usage() {
        return "accept";
    }

    @Override
    protected String description() {
        return LANG_DESCRIPTION;
    }
}
