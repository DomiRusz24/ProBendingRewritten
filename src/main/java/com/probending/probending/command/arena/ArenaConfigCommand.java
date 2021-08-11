package com.probending.probending.command.arena;

import com.probending.probending.command.abstractclasses.ArenaCommand;
import me.domirusz24.plugincore.config.annotations.Language;
import com.probending.probending.core.arena.Arena;
import com.probending.probending.managers.PAPIManager;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ArenaConfigCommand extends ArenaCommand {

    @Language("Command.ArenaConfig.Arena")
    public static String LANG_ARENA = "--------- %arena_name% ------------||In game: %arena_in_game%||State: %arena_state%||PreArena State: %prearena_state%||Name: %arena_name%||Center: %arena_x% %arena_y% %arena_z%||Has rollback: %arena_has_rollback%||-----------------------------------";

    @Language("Command.ArenaConfig.Description")
    public static String LANG_DESCRIPTION = "Configuration of the Arena.";

    @Override
    public void selfExecute(CommandSender sender, Arena arena) {
        if (hasPermission(sender)) {
            sender.sendMessage(PAPIManager.setPlaceholders(arena, LANG_ARENA));
        }
    }

    @Override
    public void selfExecute(CommandSender sender) {
        if (hasPermission(sender)) {
            help(sender, false);
        }
    }

    @Override
    protected String name() {
        return "arenaconfig";
    }

    @Override
    protected String usage() {
        return "/arenaconfig <arena> ";
    }

    @Override
    protected String description() {
        return LANG_DESCRIPTION;
    }

    @Override
    protected List<String> aliases() {
        return Collections.singletonList("arenac");
    }
}
