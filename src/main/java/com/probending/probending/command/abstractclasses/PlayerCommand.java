package com.probending.probending.command.abstractclasses;

import com.probending.probending.ProBending;
import com.probending.probending.core.arena.Arena;
import com.probending.probending.util.UtilMethods;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public List<String> autoComplete(CommandSender sender, List<String> args) {
        List<String> complete = new ArrayList<>();
        if (args.size() == 1) {
            complete.addAll(Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList()));
        } else if (args.size() == 2) {
            complete.addAll(getSubCommands().stream().map(Command::getName).collect(Collectors.toList()));
        } else {
            Player player = Bukkit.getPlayer(args.get(0));
            if (player != null) {
                for (PlayerSubCommand command : getSubCommands()) {
                    if (command.getName().equalsIgnoreCase(args.get(1)) || command.getAliases().contains(args.get(1).toLowerCase())) {
                        return command.autoComplete(sender, player, args.subList(2, args.size()));
                    }
                }
            }
        }
        return UtilMethods.getPossibleCompletions(args, complete);
    }



    public abstract void selfExecute(CommandSender sender, Player player);

    public abstract void selfExecute(CommandSender sender, String name);
}
