package com.probending.probending.command.arena.arenaconfig;

import com.probending.probending.ProBending;
import com.probending.probending.command.abstractclasses.ArenaSubCommand;
import com.probending.probending.command.abstractclasses.Command;
import com.probending.probending.core.annotations.Language;
import com.probending.probending.core.arena.Arena;
import com.probending.probending.core.enums.Ring;
import com.probending.probending.core.enums.TeamTag;
import com.probending.probending.util.Pair;
import com.probending.probending.util.UtilMethods;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ArenaSetCommand extends ArenaSubCommand {

    @Language("Command.ArenaConfig.Set.Description")
    public static String LANG_DESCRIPTION = "Allows you to modify the contents of the arena, such as Ring Locations, Starting Locations, Rollbacks, and more.";

    @Language("Command.ArenaConfig.Set.IncorrectSelection")
    public static String LANG_INCORRECT_SELECTION = "Incorrect world edit selection!";

    @Override
    public void execute(CommandSender sender, Arena arena, List<String> args) {
        if (isPlayer(sender)) {
            if (hasPermission(sender)) {
                if (correctLength(sender, args.size(), 1, 3)) {
                    String setType = args.get(0).toLowerCase();
                    Player player = (Player) sender;
                    if (setType.equals("rollback")) {
                        Pair<Location, Location> selection = ProBending.schematicM.getPlayerSelection(player);
                        if (selection != null) {
                            arena.setRollback(sender, selection.getKey(), selection.getValue());
                            sender.sendMessage(Command.LANG_SUCCESS);
                        } else {
                            sender.sendMessage(LANG_INCORRECT_SELECTION);
                        }
                    } else if (setType.equals("center")) {
                        arena.setCenter(player.getLocation());
                        sender.sendMessage(Command.LANG_SUCCESS);
                    } else if (setType.equals("sign")) {
                        Block block = player.getTargetBlock(null, 8);
                        if (block.getType().equals(Material.WALL_SIGN) || block.getType().equals(Material.SIGN)) {
                            arena.setSignLocation((Sign) block.getState());
                            sender.sendMessage(Command.LANG_SUCCESS + " (x, y, z)"
                                    .replaceAll("x", String.valueOf(arena.getJoinSign().getLocation().getBlockX()))
                                    .replaceAll("y", String.valueOf(arena.getJoinSign().getLocation().getBlockY()))
                                    .replaceAll("z", String.valueOf(arena.getJoinSign().getLocation().getBlockZ()))
                            );
                        } else {
                            sender.sendMessage(LANG_INCORRECT_SELECTION);
                        }
                    } else if (args.size() == 2) {
                        if (setType.equals("ring")) {
                            Ring ringType = Ring.fromName(args.get(1));
                            if (ringType != null && ringType.isTeleportRing()) {
                                arena.setRingLocation(ringType, player.getLocation());
                                sender.sendMessage(Command.LANG_SUCCESS);
                            } else {
                                help(sender, false);
                            }
                        } else if (setType.equals("getter")) {
                            TeamTag tag = TeamTag.getFromName(args.get(1));
                            if (tag != null) {
                                Pair<Location, Location> selection = ProBending.schematicM.getPlayerSelection(player);
                                if (selection != null) {
                                    arena.setRegionSelection(selection.getKey(), selection.getValue(), tag);
                                    sender.sendMessage(Command.LANG_SUCCESS);
                                } else {
                                    sender.sendMessage(LANG_INCORRECT_SELECTION);
                                }
                            } else {
                                sender.sendMessage(Command.LANG_INVALID_TEAM);
                            }
                        } else if (setType.equals("gettercenter")) {
                            TeamTag tag = TeamTag.getFromName(args.get(1));
                            if (tag != null) {
                                Location center = player.getLocation();
                                arena.setRegionCenter(center, tag);
                                sender.sendMessage(Command.LANG_SUCCESS);
                            } else {
                                sender.sendMessage(Command.LANG_INVALID_TEAM);
                            }
                        } else if (setType.equals("getterregioncenter")) {
                            TeamTag tag = TeamTag.getFromName(args.get(1));
                            if (tag != null) {
                                Location center = player.getLocation();
                                arena.setRegionRegionCenter(center, tag);
                                sender.sendMessage(Command.LANG_SUCCESS);
                            } else {
                                sender.sendMessage(Command.LANG_INVALID_TEAM);
                            }
                        }
                    } else if (args.size() == 3) {
                        if (setType.equals("startlocation")) {
                            Integer number = getNumber(args.get(1));
                            TeamTag tag = TeamTag.getFromName(args.get(2));
                            if (tag != null && number != null && number > 0 && number < 4) {
                                arena.setStartingLocations(tag, (number - 1), player.getLocation());
                                sender.sendMessage(Command.LANG_SUCCESS);
                            } else {
                                help(sender, false);
                            }
                        }
                    } else {
                        help(sender, false);
                    }
                }
            }
        }
    }

    @Override
    public List<String> autoComplete(CommandSender sender, Arena arena, List<String> args) {
        List<String> complete = new ArrayList<>();
        if (args.size() == 1) {
            complete.addAll(Arrays.asList("rollback", "startlocation", "ring", "center", "sign", "gettercenter", "getterregioncenter", "getter"));
        } else if (args.size() == 2) {
            String s = args.get(0);
            if (s.equalsIgnoreCase("ring")) {
                complete.addAll(Arrays.stream(Ring.values()).filter(Ring::isTeleportRing).map(Enum::name).collect(Collectors.toList()));
            } else if (s.equalsIgnoreCase("startlocation")) {
                for (int i = 1; i <= 3; i++) {
                    complete.add(String.valueOf(i));
                }
            } else if (s.equalsIgnoreCase("gettercenter") || s.equalsIgnoreCase("getterregioncenter") || s.equalsIgnoreCase("getter")) {
                complete.add(Command.LANG_TEAM_BLUE);
                complete.add(Command.LANG_TEAM_RED);
            }
        } else if (args.size() == 3) {
            complete.add(Command.LANG_TEAM_BLUE);
            complete.add(Command.LANG_TEAM_RED);
        }
        return UtilMethods.getPossibleCompletions(args, complete);
    }

    @Override
    protected String name() {
        return "set";
    }

    @Override
    protected String usage() {
        return "set <rollback / startlocation / ring / center / sign / getter / gettercenter / getterregioncenter> <playernumber / ring / " + Command.LANG_TEAM_BLUE + " / " + Command.LANG_TEAM_RED + " > " + "<" + Command.LANG_TEAM_BLUE + " / " + Command.LANG_TEAM_RED + ">";
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
