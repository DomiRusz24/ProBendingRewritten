package com.probending.probending.command.arena.arenacontrol;

import com.probending.probending.ProBending;

import com.probending.probending.command.ArenaSubCommand;
import com.probending.probending.command.Languages;
import me.domirusz24.plugincore.PluginCore;
import me.domirusz24.plugincore.config.annotations.Language;
import com.probending.probending.core.arena.Arena;
import com.probending.probending.core.enums.TeamTag;
import com.probending.probending.core.team.ArenaTempTeam;
import com.probending.probending.util.UtilMethods;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ArenaRemoveCommand extends ArenaSubCommand {

    @Language("Command.ArenaControl.Remove")
    public static String LANG_DESCRIPTION = "Allows you to remove players from Teams.";

    @Override
    public void execute(CommandSender sender, Arena arena, List<String> args) {
        if (hasPermission(sender)) {
            if (correctLength(sender, args.size(), 0, 1)) {
                if (args.size() == 0) {
                    if (isPlayer(sender)) {
                        Player player = (Player) sender;
                        arena.removePlayerFromTempTeam(player);
                        sender.sendMessage(Languages.SUCCESS);
                    }
                } else if (args.size() == 1) {
                    Player player = Bukkit.getPlayer(args.get(0));
                    if (player != null) {
                        if (ProBending.playerM.getActivePlayer(player) != null) {
                            sender.sendMessage(Languages.FAIL);
                            return;
                        }
                        arena.removePlayerFromTempTeam(player);
                        sender.sendMessage(Languages.SUCCESS);
                    } else {
                        sender.sendMessage(Languages.PLAYER_NOT_ONLINE.replace("%player%", args.get(0)));
                    }
                }
            }
        }
    }

    @Override
    public List<String> autoComplete(CommandSender sender, Arena arena, List<String> args) {
        List<String> complete = new ArrayList<>();
        if (args.size() == 1) {
            for (TeamTag tag : TeamTag.values()) {
                complete.addAll(arena.getTeam(tag).getPlayers().stream().map((p) -> p.getPlayer().getName()).collect(Collectors.toList()));
;            }
        }
        return UtilMethods.getPossibleCompletions(args, complete);
    }

    @Override
    protected String name() {
        return "remove";
    }

    @Override
    protected String usage() {
        return "remove <player>";
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
