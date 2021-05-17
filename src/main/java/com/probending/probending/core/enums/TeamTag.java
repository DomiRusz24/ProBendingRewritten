package com.probending.probending.core.enums;

import com.probending.probending.command.abstractclasses.Command;
import com.probending.probending.core.annotations.Language;
import org.bukkit.boss.BarColor;

public enum TeamTag {

    RED(BarColor.RED),
    BLUE(BarColor.BLUE);

    private final BarColor color;

    TeamTag(BarColor color) {
        this.color = color;
    }

    @Language("TeamTag.BLUE")
    public static String LANG_BLUE = "Blue";
    @Language("TeamTag.RED")
    public static String LANG_RED = "Red";

    @Language("TeamTag.ColorRed")
    public static String LANG_RED_COLOR = "&c";
    @Language("TeamTag.ColorBlue")
    public static String LANG_BLUE_COLOR = "&1";


    public static TeamTag getFromName(String name) {
        String blue = Command.LANG_TEAM_BLUE;
        String red = Command.LANG_TEAM_RED;
        if (name.equalsIgnoreCase(blue)) {
            return BLUE;
        } else if (name.equalsIgnoreCase(red)) {
            return RED;
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        if (this == TeamTag.RED) {
            return LANG_RED;
        }
        return LANG_BLUE;
    }

    public String getColor() {
        if (this == RED) {
            return LANG_RED_COLOR;
        } else {
            return LANG_BLUE_COLOR;
        }
    }

    public BarColor getBarColor() {
        return color;
    }

    public TeamTag getOther() {
        return this == RED ? BLUE : RED;
    }
}
