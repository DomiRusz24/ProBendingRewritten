package com.probending.probending.command.arena.arenacontrol;

import com.probending.probending.ProBending;
import com.probending.probending.command.abstractclasses.ArenaSubCommand;
import com.probending.probending.command.abstractclasses.Command;
import com.probending.probending.core.arena.Arena;
import com.probending.probending.core.players.SpectatorPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class ArenaSpectateCommand extends ArenaSubCommand {

    public static String LANG_DESCRIPTION = "Allows you to spectate any arena that is in-game.";


    @Override
    public void execute(CommandSender sender, Arena arena, List<String> args) {
        if (isPlayer(sender)) {
            if (correctLength(sender, args.size(), 0, 0)) {
                if (!arena.inGame()) {
                    sender.sendMessage(Command.LANG_ARENA_NOT_IN_GAME);
                } else {
                    Player player = (Player) sender;
                    if (ProBending.playerM.getActivePlayer(player) != null) {
                        sender.sendMessage(Command.LANG_FAIL);
                        return;
                    }
                    new SpectatorPlayer(arena.getActiveArena(), player, player.getLocation(), player.getGameMode());
                    sender.sendMessage(Command.LANG_SUCCESS);
                }
            }
        }
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
}
