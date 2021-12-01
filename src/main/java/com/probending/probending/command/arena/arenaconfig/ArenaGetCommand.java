package com.probending.probending.command.arena.arenaconfig;

import com.probending.probending.ProBending;
import com.probending.probending.command.ArenaSubCommand;
import com.probending.probending.command.Languages;
import me.domirusz24.plugincore.PluginCore;
import me.domirusz24.plugincore.config.annotations.Language;
import com.probending.probending.core.arena.Arena;
import com.probending.probending.core.enums.Ring;
import com.probending.probending.core.enums.TeamTag;
import com.probending.probending.util.UtilMethods;
import com.sk89q.worldedit.WorldEditException;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
                        player.teleport(arena.getCenter());
                        sender.sendMessage(Languages.SUCCESS);
                    }
                } else if (args.size() == 2) {
                    if (setType.equals("ring")) {
                        if (isPlayer(sender)) {
                            Player player = (Player) sender;
                            Ring ringType = Ring.fromName(args.get(1));
                            if (ringType != null) {
                                if (ringType.isTeleportRing()) {
                                    Location location = arena.getRingLocation(ringType);
                                    if (location != null) {
                                        player.teleport(location);
                                        sender.sendMessage(Languages.SUCCESS);
                                    } else {
                                        sender.sendMessage(Languages.LOCATION_NOT_SET);
                                    }
                                } else {
                                    sender.sendMessage(Languages.FAIL);
                                }
                            } else {
                                help(sender, false);
                            }
                        }
                    } else if (setType.equals("gettercenter")) {
                        if (isPlayer(sender)) {
                            TeamTag tag = TeamTag.getFromName(args.get(1));
                            if (tag != null) {
                                Player player = (Player) sender;
                                player.teleport(arena.getPreArena().getRegion(tag).getCenter());
                                sender.sendMessage(Languages.SUCCESS);
                            } else {
                                sender.sendMessage(Languages.INVALID_TEAM);
                            }
                        }
                    } else if (setType.equals("getterregioncenter")) {
                        if (isPlayer(sender)) {
                            TeamTag tag = TeamTag.getFromName(args.get(1));
                            if (tag != null) {
                                Player player = (Player) sender;
                                player.teleport(arena.getPreArena().getRegion(tag).getRegionCenter());
                                sender.sendMessage(Languages.SUCCESS);
                            } else {
                                sender.sendMessage(Languages.INVALID_TEAM);
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
                                    sender.sendMessage(Languages.SUCCESS);
                                } else {
                                    sender.sendMessage(Languages.LOCATION_NOT_SET);
                                }
                            } else {
                                help(sender, false);
                            }
                        }
                    }
                } else {
                    help(sender, false);
                }
            }
        }
    }

    @Override
    public List<String> autoComplete(CommandSender sender, Arena arena, List<String> args) {
        List<String> complete = new ArrayList<>();
        if (args.size() == 1) {
            complete.addAll(Arrays.asList("rollback", "startlocation", "ring", "center", "gettercenter", "getterregioncenter"));
        } else if (args.size() == 2) {
            String s = args.get(0);
            if (s.equalsIgnoreCase("ring")) {
                complete.addAll(Arrays.stream(Ring.values()).map(Enum::name).collect(Collectors.toList()));
            } else if (s.equalsIgnoreCase("startlocation")) {
                for (int i = 1; i <= 3; i++) {
                    complete.add(String.valueOf(i));
                }
            }
        } else if (args.size() == 3) {
            complete.add(Languages.TEAM_BLUE);
            complete.add(Languages.TEAM_RED);
        }
        return UtilMethods.getPossibleCompletions(args, complete);
    }

    @Override
    protected String name() {
        return "get";
    }

    @Override
    protected String usage() {
        return "get <rollback / startlocation / ring / center> <playernumber / ring> " + "<" + Languages.TEAM_BLUE + " / " + Languages.TEAM_RED + ">";
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
