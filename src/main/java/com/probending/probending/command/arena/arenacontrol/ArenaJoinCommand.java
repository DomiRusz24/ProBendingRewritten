package com.probending.probending.command.arena.arenacontrol;

import com.probending.probending.ProBending;
import com.probending.probending.command.abstractclasses.ArenaSubCommand;
import com.probending.probending.command.abstractclasses.Command;
import com.probending.probending.core.annotations.Language;
import com.probending.probending.core.arena.Arena;
import com.probending.probending.core.enums.TeamTag;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ArenaJoinCommand extends ArenaSubCommand {

    @Language("Command.ArenaControl.Join.Description")
    public static String LANG_DESCRIPTION = "Allows you to join an arena.";


    @Override
    public void execute(CommandSender sender, Arena arena, List<String> args) {
        if (isPlayer(sender)) {
            if (correctLength(sender, args.size(), 1, 1)) {
                if (hasPermission(sender)) {
                    if (arena.inGame()) {
                        sender.sendMessage(Command.LANG_ARENA_IN_GAME);
                    } else {
                        Player player = (Player) sender;
                        if (ProBending.playerM.getActivePlayer(player) != null) {
                            sender.sendMessage(Command.LANG_PLAYER_IN_GAME);
                            return;
                        }
                        String team = args.get(0);
                        TeamTag tag = TeamTag.getFromName(team);
                        if (tag != null) {
                            if (arena.getTeam(tag).addPlayer((Player) sender)) {
                                sender.sendMessage(Command.LANG_SUCCESS);
                            } else {
                                sender.sendMessage(Command.LANG_FAIL);
                            }
                        } else {
                            help(sender, false);
                        }
                    }
                }
            }
        }
    }

    @Override
    protected String name() {
        return "join";
    }

    @Override
    protected String usage() {
        return "join <" + Command.LANG_TEAM_BLUE + " / " + Command.LANG_TEAM_RED + ">";
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
