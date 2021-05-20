package com.probending.probending.command.pbteam.pbteam;

import com.probending.probending.ProBending;
import com.probending.probending.command.abstractclasses.BaseSubCommand;
import com.probending.probending.core.annotations.Language;
import com.probending.probending.core.players.PBPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CreateTeamCommand extends BaseSubCommand {

    @Language("Command.Team.Create.Description")
    public static String LANG_DESCRIPTION = "Allows you to create a Team.";

    @Language("Command.Team.Create.AlreadyExist")
    public static String LANG_ALREADY_EXIST = "Team %team% already exists!";
    @Language("Command.Team.Create.AlreadyInTeam")
    public static String LANG_ALREADY_IN_TEAM = "You are already in a team! (%team%)!";
    @Language("Command.Team.Create.Created")
    public static String LANG_CREATED = "Team %team% has been created!";

    @Override
    protected void execute(CommandSender sender, List<String> args) {
        if (hasPermission(sender) && isPlayer(sender) && correctLength(sender, args.size(), 1,1)) {
            String name = args.get(0);
            PBPlayer player = PBPlayer.of((Player) sender);
            if (player.getTeam() == null) {
                ProBending.SqlM.teamTable.exists(name, (bool) -> {
                    if (bool) {
                        sender.sendMessage(LANG_ALREADY_EXIST.replaceAll("%team%", name));
                    } else {
                        ProBending.SqlM.teamTable.createTeam((Player) sender, name, (team) -> {
                            sender.sendMessage(LANG_CREATED.replaceAll("%team%", team.getName()));
                        });

                    }
                });
            } else {
                sender.sendMessage(LANG_ALREADY_IN_TEAM.replaceAll("%team%", player.getTeam().getName()));
            }
        }
    }

    @Override
    protected String name() {
        return "create";
    }

    @Override
    protected String usage() {
        return "create <name>";
    }

    @Override
    protected String description() {
        return LANG_DESCRIPTION;
    }
}
