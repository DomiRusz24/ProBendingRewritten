package com.probending.probending.command.pbteam.pbteam;

import com.probending.probending.command.Languages;
import me.domirusz24.plugincore.command.abstractclasses.BaseSubCommand;
import me.domirusz24.plugincore.config.annotations.Language;
import com.probending.probending.core.players.PBPlayer;
import com.probending.probending.core.team.PBTeam;
import com.probending.probending.core.team.TeamInvite;
import com.probending.probending.util.UtilMethods;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InviteTeamCommand extends BaseSubCommand {

    @Language("Command.Team.Invite.Description")
    public static String LANG_DESCRIPTION = "Allows you to invite a Player to your team.";

    @Language("Command.Team.Invite.OtherPlayerAlreadyInTeam")
    public static String LANG_OTHER_IN_TEAM = "This player is already in your team!";

    @Language("Command.Team.Invite.NotInTeam")
    public static String LANG_NOT_IN_TEAM = "You are not in any team!";

    @Override
    protected void execute(CommandSender sender, List<String> args) {
        if (isPlayer(sender) && hasPermission(sender) &&  correctLength(sender, args.size(), 1, 1)) {
            Player target = Bukkit.getPlayer(args.get(0));
            Player player = (Player) sender;
            if (target != null) {
                PBTeam team = PBPlayer.of(player).getTeam();
                if (team != null) {
                    if (team.getMember(PBPlayer.of(player)).hasRole("captain")) {
                        if (!team.contains(PBPlayer.of(target))) {
                            PBPlayer pbTarget = PBPlayer.of(target);
                            pbTarget.setInvite(new TeamInvite(player, pbTarget, team, 60000));
                            sender.sendMessage(Languages.SUCCESS);
                        } else {
                            sender.sendMessage(LANG_OTHER_IN_TEAM);
                        }
                    } else {
                        sender.sendMessage(Languages.INSUFFICIENT_PERMS);
                    }
                } else {
                    sender.sendMessage(LANG_NOT_IN_TEAM);
                }
            } else {
                sender.sendMessage(Languages.PLAYER_NOT_ONLINE.replaceAll("%player%", args.get(0)));
            }

        }
    }

    @Override
    public List<String> autoComplete(CommandSender sender, List<String> args) {
        if (args.size() == 1) {
            return UtilMethods.getPossibleCompletions(args, Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList()));
        }
        return new ArrayList<>();
    }

    @Override
    protected String name() {
        return "invite";
    }

    @Override
    protected String usage() {
        return "invite <Player>";
    }

    @Override
    protected String description() {
        return LANG_DESCRIPTION;
    }

    @Override
    public PermissionDefault getPermissionDefault() {
        return PermissionDefault.TRUE;
    }
}
