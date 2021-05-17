package com.probending.probending.command.abstractclasses;

import com.probending.probending.ProBending;
import com.probending.probending.core.arena.Arena;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class PlayerCommand extends AbstractCommand<PlayerSubCommand> {

    @Override
    public boolean canExecuteSubCommands(CommandSender sender, List<String> args) {
        boolean canExecute = false;
        if (args.size() > 1) {
            String name = args.get(0);
            Player player = Bukkit.getPlayer(name);
            canExecute = player != null;
            if (!canExecute) {
                sender.sendMessage(Command.LANG_PLAYER_NOT_ONLINE.replaceAll("%player%", name));
            }
        }
        return canExecute;
    }

    @Override
    public boolean canSelfExecute(CommandSender sender, List<String> args) {
        return args.size() < 2;
    }

    @Override
    public void selfExecute(CommandSender sender, List<String> args) {
        if (args.isEmpty()) {
            selfExecute(sender);
            return;
        }
        String name = args.get(0);
        Player player = Bukkit.getPlayer(name);
        if (player != null) {
            selfExecute(sender, player);
        } else {
            selfExecute(sender, name);
        }
    }

    public abstract void selfExecute(CommandSender sender, Player player);

    public abstract void selfExecute(CommandSender sender, String name);
}
