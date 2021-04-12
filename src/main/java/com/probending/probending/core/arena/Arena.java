package com.probending.probending.core.arena;

import com.probending.probending.core.annotations.Language;
import org.bukkit.ChatColor;
import org.bukkit.Location;

public class Arena {

    // --- LANGUAGE ---

    @Language("Arena.State.DISABLED")
    public static String LANG_DISABLED = "&7Disabled!";

    @Language("Arena.State.NOT_COMPLETE")
    public static String LANG_NOT_COMPLETE = "&cNot complete!";

    @Language("Arena.State.ERROR")
    public static String LANG_ERROR = "&4This arena is with a malfunction.";

    @Language("Arena.State.EMPTY")
    public static String LANG_EMPTY = "&2This arena is empty.";

    @Language("Arena.State.TAKEN")
    public static String LANG_TAKEN = "&dThis arena is taken!";

    // ----------------

    private State state;

    private String name;

    private Location center;

    public Arena(String name, Location center) {
        if (name.length() > 12 || name.contains(" ") || name.contains(".")) {
            state = State.ERROR;
            throw new IllegalArgumentException();
        }
        this.name = name;
        this.center = center;
        state = State.NOT_COMPLETE;
    }

    public String getName() {
        return name;
    }

    public Location getCenter() {
        return center;
    }

    public State getState() {
        return state;
    }

    enum State {
        DISABLED,
        NOT_COMPLETE,
        ERROR,
        EMPTY,
        TAKEN;

        public String toString() {
            switch (this) {
                case DISABLED:
                    return LANG_DISABLED;
                case NOT_COMPLETE:
                    return LANG_NOT_COMPLETE;
                case ERROR:
                    return LANG_ERROR;
                case EMPTY:
                    return LANG_EMPTY;
                case TAKEN:
                    return LANG_TAKEN;
            }
            return null;
        }
    }
}
