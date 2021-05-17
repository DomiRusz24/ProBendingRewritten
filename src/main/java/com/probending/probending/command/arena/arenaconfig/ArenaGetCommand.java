package com.probending.probending.command.arena.arenaconfig;

import com.probending.probending.ProBending;
import com.probending.probending.command.abstractclasses.ArenaCommand;
import com.probending.probending.command.abstractclasses.ArenaSubCommand;
import com.probending.probending.command.abstractclasses.Command;
import com.probending.probending.core.annotations.Language;
import com.probending.probending.core.arena.Arena;
import com.probending.probending.core.enums.Ring;
import com.probending.probending.core.enums.TeamTag;
import com.sk89q.worldedit.WorldEditException;
import javafx.util.Pair;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ArenaGetCommand extends ArenaSubCommand {

    @Language("Command.ArenaConfig.Set.Description")
    public static String LANG_DESCRIPTION = "Allows you to get the contents of the arena, such as Ring Locations, Starting Locations, Rollbacks, and more.";

    @Override
    public void execute(CommandSender sender, Arena arena, List<String> args) {
        if (hasPermission(sender)) {
            if (correctLength(sender, args.size(), 1, 3)) {
                String setType = args.get(0).toLowerCase();
                if (setType.equals("rollback")) {
                    arena.getRollback(sender);
                } else if (setType.equals("center")) {
                    if (isPlayer(sender)) {
                        Player player = (Player) sender;
                        player.getLocation(arena.getCenter());
                    }
                } else if (args.size() == 2) {
                    if (setType.equals("ring")) {
                        if (isPlayer(sender)) {
                            Player player = (Player) sender;
                            Ring ringType = Ring.fromName(args.get(1));
                            if (ringType != null && ringType.isTeleportRing()) {
                                Location location = arena.getRingLocation(ringType);
                                if (location != null) {
                                    player.teleport(location);
                                    sender.sendMessage(Command.LANG_SUCCESS);
                                } else {
                                    sender.sendMessage(Command.LANG_LOCATION_NOT_SET);
                                }
                            } else {
                                help(sender, false);
                            }
                        }
                    }
                } else if (args.size() == 3) {
                    if (setType.equals("startlocation")) {
                        if (isPlayer(sender)) {
                            Player player = (Player) sender;
                            Integer number = getNumber(args.get(1));
                            TeamTag tag = TeamTag.getFromName(args.get(2));
                            if (tag != null && number != null && number > 0 && number < 4) {
                                Location location = arena.getStartingLocations(tag, number - 1);
                                if (location != null) {
                                    player.teleport(location);
                                    sender.sendMessage(Command.LANG_SUCCESS);
                                } else {
                                    sender.sendMessage(Command.LANG_LOCATION_NOT_SET);
                                }
                            } else {
                                help(sender, false);
                            }
                        }
                    }
                }
                help(sender, false);
            }
        }
    }

    @Override
    protected String name() {
        return "get";
    }

    @Override
    protected String usage() {
        return "get <rollback / startlocation / ring / center> <playernumber / ring> " + "<" + Command.LANG_TEAM_BLUE + " / " + Command.LANG_TEAM_RED + ">";
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
