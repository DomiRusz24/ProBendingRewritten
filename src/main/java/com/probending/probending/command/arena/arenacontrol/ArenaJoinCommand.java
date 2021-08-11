package com.probending.probending.command.arena.arenacontrol;

import com.probending.probending.ProBending;

import com.probending.probending.command.ArenaSubCommand;
import me.domirusz24.plugincore.command.Languages;
import me.domirusz24.plugincore.config.annotations.Language;
import com.probending.probending.core.arena.Arena;
import com.probending.probending.core.enums.TeamTag;
import com.probending.probending.util.UtilMethods;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

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
                        sender.sendMessage(Languages.ARENA_IN_GAME);
                    } else {
                        Player player = (Player) sender;
                        if (ProBending.playerM.getActivePlayer(player) != null) {
                            sender.sendMessage(Languages.PLAYER_IN_GAME);
                            return;
                        }
                        String team = args.get(0);
                        TeamTag tag = TeamTag.getFromName(team);
                        if (tag != null) {
                            if (arena.getTeam(tag).addPlayer((Player) sender)) {
                                sender.sendMessage(Languages.SUCCESS);
                            } else {
                                sender.sendMessage(Languages.FAIL);
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
    public List<String> autoComplete(CommandSender sender, Arena arena, List<String> args) {
        List<String> complete = new ArrayList<>();
        if (args.size() == 1) {
            complete.add(Languages.TEAM_BLUE);
            complete.add(Languages.TEAM_RED);
        }
        return UtilMethods.getPossibleCompletions(args, complete);
    }

    @Override
    protected String name() {
        return "join";
    }

    @Override
    protected String usage() {
        return "join <" + Languages.TEAM_BLUE + " / " + Languages.TEAM_RED + ">";
    }

    @Override
    protected String description() {
        return LANG_DESCRIPTION;
    }

    @Override
    protected List<String> aliases() {
        return new ArrayList<>();
    }

    @Override
    public PermissionDefault getPermissionDefault() {
        return null;
    }
}
