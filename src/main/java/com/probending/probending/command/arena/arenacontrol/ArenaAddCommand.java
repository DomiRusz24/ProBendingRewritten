package com.probending.probending.command.arena.arenacontrol;

import com.probending.probending.ProBending;
import com.probending.probending.command.ArenaSubCommand;
import com.probending.probending.command.Languages;
import me.domirusz24.plugincore.PluginCore;
import me.domirusz24.plugincore.config.annotations.Language;
import com.probending.probending.core.arena.Arena;
import com.probending.probending.core.enums.TeamTag;
import com.probending.probending.core.team.PBTeam;
import com.probending.probending.util.UtilMethods;
import me.domirusz24.plugincore.core.team.AbstractTeam;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

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
                    sender.sendMessage(Languages.ARENA_IN_GAME);
                    return;
                }
                String name = args.get(0);
                if (args.size() == 2) {
                    Player player = Bukkit.getPlayer(name);
                    if (player == null) {
                        sender.sendMessage(Languages.PLAYER_NOT_ONLINE.replaceAll("%player%", args.get(0)));
                        return;
                    }
                    if (ProBending.playerM.getActivePlayer(player) != null) {
                        sender.sendMessage(Languages.PLAYER_IN_GAME.replaceAll("%player%", name));
                        return;
                    }
                    TeamTag team = TeamTag.getFromName(args.get(1));
                    if (team == null) {
                        help(sender, false);
                        return;
                    }
                    if (arena.getTeam(team).addPlayer(player)) {
                        sender.sendMessage(Languages.SUCCESS);
                    } else {
                        sender.sendMessage(Languages.FAIL);
                    }
                } else {
                    PBTeam team = ProBending.teamM.getPBTeamByName(name);
                    if (team != null) {
                        if (team.throwIntoGame(arena)) {
                            sender.sendMessage(Languages.SUCCESS);
                        } else {
                            sender.sendMessage(Languages.FAIL);
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
            complete.add(Languages.TEAM_BLUE);
            complete.add(Languages.TEAM_RED);
        }
        return UtilMethods.getPossibleCompletions(args, complete);
    }

    @Override
    protected String name() {
        return "add";
    }

    @Override
    protected String usage() {
        return "add <player / team> <" + Languages.TEAM_BLUE + " / " + Languages.TEAM_RED + ">";
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

    @Override
    public PluginCore getCorePlugin() {
        return ProBending.plugin;
    }
}
