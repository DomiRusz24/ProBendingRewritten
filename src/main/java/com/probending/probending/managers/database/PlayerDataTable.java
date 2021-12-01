package com.probending.probending.managers.database;

import com.probending.probending.core.players.PBPlayer;
import me.domirusz24.plugincore.managers.database.DataBaseManager;
import me.domirusz24.plugincore.managers.database.DataBaseTable;
import me.domirusz24.plugincore.managers.database.PlayerDataBaseTable;
import me.domirusz24.plugincore.managers.database.values.DataBaseValue;
import me.domirusz24.plugincore.managers.database.values.IntegerValue;
import me.domirusz24.plugincore.managers.database.values.StringValue;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.function.Consumer;

import static com.probending.probending.ProBending.playerM;
import static com.probending.probending.ProBending.plugin;

public class PlayerDataTable extends PlayerDataBaseTable {

    public final String WINS = "wins";
    public final String LOST = "lost";
    public final String KILLS = "kills";
    public final String TIES = "ties";
    public final String TEAM = "team";

    public PlayerDataTable(DataBaseManager manager) {
        super(manager);
    }


    @Override
    public String getName() {
        return "PBBenders";
    }

    @Override
    public DataBaseValue<?>[] _getValues() {
        return new DataBaseValue[] {
                new IntegerValue(WINS),
                new IntegerValue(LOST),
                new IntegerValue(KILLS),
                new IntegerValue(TIES),
                new StringValue(TEAM, 100, "NULL"),
        };
    }
}
