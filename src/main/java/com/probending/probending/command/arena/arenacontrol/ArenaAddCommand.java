package com.probending.probending.command.arena.arenacontrol;

import com.probending.probending.ProBending;
import com.probending.probending.command.abstractclasses.ArenaSubCommand;
import com.probending.probending.command.abstractclasses.Command;
import me.domirusz24.plugincore.config.annotations.Language;
import com.probending.probending.core.arena.Arena;
import com.probending.probending.core.enums.TeamTag;
import com.probending.probending.core.team.PBTeam;
import com.probending.probending.util.UtilMethods;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ArenaAddCommand extends ArenaSubCommand {

    @Language("Command.ArenaControl.Add.Description")
    public static String LANG_DESCRIPTION = "Allows you to add players to teams.";

    @Language("Command.ArenaControl.Add.TeamNotReady")
    public static String LANG_TEAM_NOT_READY = "This team isnt ready!";


    @Override
    public void execute(CommandSender sender, Arena arena, List<String> args) {
        if (hasPermission(sender)) {
            if (correctLength(sender, args.size(), 1, 2)) {
                if (arena.inGame()) {
                    sender.sendMessage(Command.LANG_ARENA_IN_GAME);
                    return;
                }
                String name = args.get(0);
                if (args.size() == 2) {
                    Player player = Bukkit.getPlayer(name);
                    if (player == null) {
                        sender.sendMessage(Command.LANG_PLAYER_NOT_ONLINE.replaceAll("%player%", args.get(0)));
                        return;
                    }
                    if (ProBending.playerM.getActivePlayer(player) != null) {
                        sender.sendMessage(Command.LANG_PLAYER_IN_GAME.replaceAll("%player%", name));
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
                } else {
                    PBTeam team = ProBending.teamM.getPBTeamByName(name);
                    if (team != null) {
                        if (team.throwIntoGame(arena)) {
                            sender.sendMessage(LANG_SUCCESS);
                        } else {
                            sender.sendMessage(LANG_FAIL);
                        }
                    } else {
                        sender.sendMessage(LANG_TEAM_NOT_READY);
                    }
                }
            }
        }
    }

    @Override
    public List<String> autoComplete(CommandSender sender, Arena arena, List<String> args) {
        List<String> complete = new ArrayList<>();
        if (args.size() == 1) {
            complete.addAll(Bukkit.getOnlinePlayers().stream()
                    .filter((p) -> ProBending.playerM.getActivePlayer(p) == null && ProBending.playerM.getMenuPlayer(p) == null)
                    .map(HumanEntity::getName).collect(Collectors.toList()));
            complete.addAll(ProBending.teamM.getPBTeams().stream().map(AbstractTeam::getName).collect(Collectors.toList()));
        } else if (args.size() == 2) {
            complete.add(Command.LANG_TEAM_BLUE);
            complete.add(Command.LANG_TEAM_RED);
        }
        return UtilMethods.getPossibleCompletions(args, complete);
    }

    @Override
    protected String name() {
        return "add";
    }

    @Override
    protected String usage() {
        return "add <player / team> <" + Command.LANG_TEAM_BLUE + " / " + Command.LANG_TEAM_RED + ">";
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
