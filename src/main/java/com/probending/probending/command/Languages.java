package com.probending.probending.command;

import me.domirusz24.plugincore.config.annotations.Language;

public class Languages extends me.domirusz24.plugincore.command.Languages {

    @Language("Command.ArenaDoesNotExist")
    public static String ARENA_DOES_NOT_EXIST = "&c%arena% does not exist!";

    @Language("Command.LocationIsNotSet")
    public static String LOCATION_NOT_SET = "Location is not set!";

    @Language("Command.InvalidTeam")
    public static String INVALID_TEAM = "&4That is not a valid team!";


    @Language("Command.TeamBlue")
    public static String TEAM_BLUE = "blue";
    @Language("Command.TeamRed")
    public static String TEAM_RED = "red";

    @Language("Command.ArenaInGame")
    public static String ARENA_IN_GAME = "This arena is already in game!";

    @Language("Command.ArenaNotInGame")
    public static String ARENA_NOT_IN_GAME = "This arena hans't started yet!";

    @Language("Command.PlayerInGame")
    public static String PLAYER_IN_GAME = "You are already in a game!";


}
