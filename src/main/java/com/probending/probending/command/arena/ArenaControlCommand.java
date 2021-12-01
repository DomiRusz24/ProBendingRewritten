package com.probending.probending.command.arena;

import com.probending.probending.ProBending;
import com.probending.probending.command.ArenaCommand;
import me.domirusz24.plugincore.PluginCore;
import me.domirusz24.plugincore.config.annotations.Language;
import com.probending.probending.core.arena.Arena;
import com.probending.probending.core.enums.TeamTag;
import com.probending.probending.managers.PAPIManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;

import java.util.ArrayList;
import java.util.List;

public class ArenaControlCommand extends ArenaCommand {

    @Language("Command.ArenaControl.Description")
    public static String LANG_DESCRIPTION = "Main command used for managing Arenas, such as spectating or managing teams.";


    @Override
    protected String name() {
        return "arena";
    }

    @Override
    protected String usage() {
        return "/arena <arena> ";
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

    @Language("Command.ArenaControl.ArenaNotInGame")
    public static String LANG_ARENA_NOT_IN_GAME = "--------- %arena_name% ------------||State: %arena_state%||%team_blue%||%team_red%||-----------------------------------";

    @Language("Command.ArenaControl.ArenaInGame")
    public static String LANG_ARENA_IN_GAME = "--------- %arena_name% ------------||State: %arena_state%||Spectators: %arena_spectators%||Length: %arena_game_time% minutes.||Round time: %arena_round_time% minutes.||%team_blue%||%team_red%||-----------------------------------";

    @Override
    public void selfExecute(CommandSender sender, Arena arena) {
        if (hasPermission(sender)) {
            String value;
            if (arena.inGame()) {
                value = LANG_ARENA_IN_GAME;
                value = value.replaceAll("%team_blue%", arena.getActiveArena().getTeam(TeamTag.BLUE).getInfo());
                value = value.replaceAll("%team_red%", arena.getActiveArena().getTeam(TeamTag.RED).getInfo());
            } else {
                value = LANG_ARENA_NOT_IN_GAME;
                value = value.replaceAll("%team_blue%", arena.getTeam(TeamTag.BLUE).getInfo());
                value = value.replaceAll("%team_red%", arena.getTeam(TeamTag.RED).getInfo());
            }
            sender.sendMessage(PAPIManager.setPlaceholder(arena, value));
        }
    }

    @Language("Command.ArenaControl.ArenaList.List")
    public static String LANG_ARENA_LIST = "<------->||Arenas:||%arena_list%||<------->";

    @Language("Command.ArenaControl.ArenaList.SingleArena")
    public static String LANG_SINGLE_ARENA = "&4 -%arena_in_game% %arena_name%: %arena_state%||";

    @Override
    public void selfExecute(CommandSender sender) {
        if (hasPermission(sender)) {
            StringBuilder arenaList = new StringBuilder();
            for (Arena arena : ProBending.arenaM.getArenas()) {
                arenaList.append(PAPIManager.setPlaceholder(arena, LANG_SINGLE_ARENA));
            }
            sender.sendMessage(LANG_ARENA_LIST.replaceAll("%arena_list%", arenaList.toString()));
        }
    }

    @Override
    public PluginCore getCorePlugin() {
        return ProBending.plugin;
    }
}
