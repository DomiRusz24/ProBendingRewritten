package com.probending.probending.core.enums;

import com.probending.probending.core.annotations.Language;
import com.probending.probending.util.UtilMethods;

public enum ArenaState {
    HALT,
    STOP,
    NONE,
    IN_ROUND,
    MID_ROUND,
    STAGE_GAIN,
    TIE_BREAKER;

    @Language("ActiveArena.State.IN_ROUND")
    public static String LANG_IN_ROUND = "&cDuring round";

    @Language("ActiveArena.State.STOPPING")
    public static String LANG_STOPPING = "&cEnding the game";

    @Language("ActiveArena.State.MID_ROUND")
    public static String LANG_MID_ROUND = "&4Mid-Round";

    @Language("ActiveArena.State.STAGE_GAIN")
    public static String LANG_STAGE_GAIN = "&4Stage gain";

    @Language("ActiveArena.State.TIE_BREAKER")
    public static String LANG_TIE_BREAKER = "&2Tie Breaker";

    @Override
    public String toString() {
        switch (this) {
            case NONE:
                return "NONE";
            case IN_ROUND:
                return LANG_IN_ROUND;
            case MID_ROUND:
                return LANG_MID_ROUND;
            case TIE_BREAKER:
                return LANG_TIE_BREAKER;
            case STAGE_GAIN:
                return LANG_STAGE_GAIN;
            case STOP:
                return LANG_STOPPING;
            default:
                return "HALT";
        }
    }
}
