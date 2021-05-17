package com.probending.probending.command.abstractclasses;

import com.probending.probending.core.annotations.Language;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class Command {

    @Language("Command.ErrorPrefix")
    public static String LANG_ERROR_PREFIX = "&l&c[ERROR]&r&c ";
    @Language("Command.SuccessPrefix")
    public static String LANG_SUCCESS_PREFIX = "&l&a[SUCCESS]&r&a ";
    @Language("Command.PBPrefix")
    public static String LANG_PREFIX = "&l&b[ProBending]&r ";

    @Language("Command.Success")
    public static String LANG_SUCCESS = "&aSuccess!";
    @Language("Command.Fail")
    public static String LANG_FAIL = "&cCommand couldn't be executed!";
    @Language("Command.FailContactAdmins")
    public static String LANG_FAIL_ERROR = "&c&lAn internal error has occurred! Please contact server admins.";

    @Language("Command.InsufficientPermissions")
    public static String LANG_INSUFFICIENT_PERMS = "You don't have the permission to use this command!";

    @Language("Command.MustBePlayer")
    public static String LANG_MUST_BE_PLAYER = "You must be a player to execute this command!";

    @Language("Command.Usage")
    public static String LANG_USAGE = "Usage: ";

    @Language("Command.ArenaDoesNotExist")
    public static String LANG_ARENA_DOES_NOT_EXIST = "&c%arena% does not exist!";

    @Language("Command.Description")
    public static String LANG_COMMAND_DESCRIPTION = "Description: ";

    @Language("Command.Yes")
    public static String LANG_YES = "&aYes";

    @Language("Command.No")
    public static String LANG_NO = "&4No";

    @Language("Command.PlayerNotOnline")
    public static String LANG_PLAYER_NOT_ONLINE = "Player %player% is not online!";

    @Language("Command.LocationIsNotSet")
    public static String LANG_LOCATION_NOT_SET = "Location is not set!";

    @Language("Command.InvalidTeam")
    public static String LANG_INVALID_TEAM = "&4That is not a valid team!";


    @Language("Command.TeamBlue")
    public static String LANG_TEAM_BLUE = "blue";
    @Language("Command.TeamRed")
    public static String LANG_TEAM_RED = "red";

    @Language("Command.ArenaInGame")
    public static String LANG_ARENA_IN_GAME = "This arena is already in game!";

    @Language("Command.ArenaNotInGame")
    public static String LANG_ARENA_NOT_IN_GAME = "This arena hans't started yet!";

    @Language("Command.PlayerInGame")
    public static String LANG_PLAYER_IN_GAME = "You are already in a game!";

    protected abstract String name();
    protected abstract String usage();
    protected abstract String description();
    protected List<String> aliases() {
        return new ArrayList<>();
    }

    public String getName() {
        return this.name();
    }

    public String getUsage() {
        return this.usage();
    }

    public String getDescription() {
        return this.description();
    }

    public List<String> getAliases() {
        return this.aliases();
    }

    public void help(CommandSender sender, boolean description) {
        sender.sendMessage(LANG_USAGE + getUsage());
        if (description) {
            sender.sendMessage(LANG_COMMAND_DESCRIPTION + description());
        }
    }

    protected String getPermission() {
        return "probending.command." + this.name();
    }

    protected boolean hasPermission(CommandSender sender) {
        if (sender.hasPermission(getPermission())) {
            return true;
        } else {
            sender.sendMessage(LANG_ERROR_PREFIX + "" + LANG_INSUFFICIENT_PERMS);
            return false;
        }
    }

    protected boolean hasPermission(CommandSender sender, String extra) {
        if (sender.hasPermission(getPermission() + "." + extra)) {
            return true;
        } else {
            sender.sendMessage(LANG_ERROR_PREFIX + "" + LANG_INSUFFICIENT_PERMS);
            return false;
        }
    }

    protected boolean correctLength(CommandSender sender, int size, int min, int max) {
        if (size >= min && size <= max) {
            return true;
        } else {
            this.help(sender, false);
            return false;
        }
    }

    protected boolean isPlayer(CommandSender sender) {
        if (sender instanceof Player) {
            return true;
        } else {
            sender.sendMessage(LANG_ERROR_PREFIX + "" + LANG_MUST_BE_PLAYER);
            return false;
        }
    }

    protected Integer getNumber(String string) {
        Integer value;
        try {
            value = Integer.parseInt(string);
        } catch (NumberFormatException e) {
            value = null;
        }
        return value;
    }
}
