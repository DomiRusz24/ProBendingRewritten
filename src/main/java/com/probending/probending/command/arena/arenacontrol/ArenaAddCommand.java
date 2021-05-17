package com.probending.probending.command.arena.arenacontrol;

import com.probending.probending.ProBending;
import com.probending.probending.command.abstractclasses.ArenaSubCommand;
import com.probending.probending.command.abstractclasses.Command;
import com.probending.probending.core.annotations.Language;
import com.probending.probending.core.arena.Arena;
import com.probending.probending.core.enums.TeamTag;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ArenaAddCommand extends ArenaSubCommand {

    @Language("Command.ArenaControl.Add.Description")
    public static String LANG_DESCRIPTION = "Allows you to add players to teams.";


    @Override
    public void execute(CommandSender sender, Arena arena, List<String> args) {
        if (hasPermission(sender)) {
            if (correctLength(sender, args.size(), 2, 2)) {
                if (arena.inGame()) {
                    sender.sendMessage(Command.LANG_ARENA_IN_GAME);
                    return;
                }
                Player player = Bukkit.getPlayer(args.get(0));
                if (player == null) {
                    sender.sendMessage(Command.LANG_ERROR_PREFIX + Command.LANG_PLAYER_NOT_ONLINE.replaceAll("%player%", args.get(0)));
                    return;
                }
                if (ProBending.playerM.getActivePlayer(player) != null) {
                    return;
                }
                TeamTag team = TeamTag.getFromName(args.get(1));
                if (team == null) {
                    help(sender, false);
                    return;
                }
                if (arena.getTeam(team).addPlayer(player)) {
                    sender.sendMessage(Command.LANG_SUCCESS);
                } else {
                    sender.sendMessage(Command.LANG_FAIL);
                }
            }
        }
    }

    @Override
    protected String name() {
        return "add";
    }

    @Override
    protected String usage() {
        return "add <player> <" + Command.LANG_TEAM_BLUE + " / " + Command.LANG_TEAM_RED + ">";
    }

    @Override
    protected String description() {
        return LANG_DESCRIPTION;
    }

    @Override
    protected List<String> aliases() {
        return new ArrayList<>();
    }
}
