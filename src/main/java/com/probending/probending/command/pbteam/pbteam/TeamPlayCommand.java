package com.probending.probending.command.pbteam.pbteam;

import com.probending.probending.ProBending;
import com.probending.probending.command.abstractclasses.BaseSubCommand;
import com.probending.probending.command.abstractclasses.Command;
import com.probending.probending.command.arena.arenacontrol.ArenaAddCommand;
import me.domirusz24.plugincore.config.annotations.Language;
import com.probending.probending.core.players.PBPlayer;
import com.probending.probending.core.team.PBTeam;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TeamPlayCommand extends BaseSubCommand {

    @Language("Command.Team.Play.Description")
    public static String LANG_DESCRIPTION = "Allows you to play with your team.";

    @Language("Command.Team.Player.NoArenas")
    public static String LANG_NO_ARENAS = "There are no available arenas!";


    @Override
    protected void execute(CommandSender sender, List<String> args) {
        if (isPlayer(sender) && correctLength(sender, args.size(), 0, 0) && hasPermission(sender)) {
            Player player = (Player) sender;
            PBTeam team = PBPlayer.of(player).getTeam();
            if (team != null) {
                if (ProBending.playerM.getActivePlayer(player) == null && ProBending.teamM.getTempTeam(player) == null) {
                    team.getPlayGUI().addPlayer((Player) sender);
                } else {
                    sender.sendMessage(Languages.PLAYER_IN_GAME);
                }
            } else {
                sender.sendMessage(InviteTeamLanguages.NOT_IN_TEAM);
            }
        }
    }

    @Override
    public List<String> autoComplete(CommandSender sender, List<String> args) {
        return new ArrayList<>();
    }

    @Override
    protected String name() {
        return "play";
    }

    @Override
    protected String usage() {
        return "play";
    }

    @Override
    protected String description() {
        return LANG_DESCRIPTION;
    }
}
