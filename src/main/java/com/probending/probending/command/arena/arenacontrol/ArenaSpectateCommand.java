package com.probending.probending.command.arena.arenacontrol;

import com.probending.probending.ProBending;

import com.probending.probending.command.ArenaSubCommand;
import com.probending.probending.core.arena.Arena;
import com.probending.probending.core.players.SpectatorPlayer;
import com.probending.probending.command.Languages;
import me.domirusz24.plugincore.PluginCore;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

import java.util.List;

public class ArenaSpectateCommand extends ArenaSubCommand {

    public static String LANG_DESCRIPTION = "Allows you to spectate any arena that is in-game.";


    @Override
    public void execute(CommandSender sender, Arena arena, List<String> args) {
        if (isPlayer(sender) && hasPermission(sender)) {
            if (correctLength(sender, args.size(), 0, 0)) {
                if (!arena.inGame()) {
                    sender.sendMessage(Languages.ARENA_NOT_IN_GAME);
                } else {
                    Player player = (Player) sender;
                    if (ProBending.playerM.getActivePlayer(player) != null) {
                        sender.sendMessage(Languages.FAIL);
                        return;
                    }
                    new SpectatorPlayer(arena.getActiveArena(), player, player.getLocation(), player.getGameMode());
                    sender.sendMessage(Languages.SUCCESS);
                }
            }
        }
    }

    @Override
    public List<String> autoComplete(CommandSender sender, Arena arena, List<String> args) {
        return null;
    }

    @Override
    protected String name() {
        return "spectate";
    }

    @Override
    protected String usage() {
        return "spectate";
    }

    @Override
    protected String description() {
        return LANG_DESCRIPTION;
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
