package com.probending.probending.core.enums;

import com.probending.probending.ProBending;
import com.probending.probending.core.annotations.Language;
import com.probending.probending.util.UtilMethods;
import org.bukkit.configuration.file.YamlConfiguration;

public enum GameType {


    DEFAULT(180, 3),
    RANKED(180, 3);

    @Language("Arena.GameType.DEFAULT")
    public static String LANG_DEFAULT = "Default";

    @Language("Arena.GameType.RANKED")
    public static String LANG_RANKED = "Ranked";

    public static GameType getFromName(String string) {
        for (GameType type : GameType.values()) {
            if (type.name().equalsIgnoreCase(string)) return type;
        }
        return null;
    }

    public static void saveDefaults(YamlConfiguration config) {
        for (GameType type : GameType.values()) {
            type.saveDefault(config);
        }
    }

    private final int defaultRoundTime;
    private final int defaultRoundWin;

    GameType(int roundTime, int roundWin) {
        this.defaultRoundTime = roundTime;
        this.defaultRoundWin = roundWin;
    }

    private void saveDefault(YamlConfiguration config) {
        config.addDefault("Arena.GameType." + name() + ".RoundTime", defaultRoundTime);
        config.addDefault("Arena.GameType." + name() + ".RoundWin", defaultRoundWin);
    }

    public int getRoundTime() {
        return ProBending.configM.getConfig().getInt("Arena.GameType." + name() + ".RoundTime");
    }

    public int getRoundWin() {
        return ProBending.configM.getConfig().getInt("Arena.GameType." + name() + ".RoundWin");
    }

    @Override
    public String toString() {
        switch (this) {
            case RANKED:
                return LANG_RANKED;
            case DEFAULT:
                return LANG_DEFAULT;
        }
        return null;
    }
}
