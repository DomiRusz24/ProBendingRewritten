package com.probending.probending.managers.database;

import com.probending.probending.core.players.PBPlayer;
import com.probending.probending.managers.database.values.DataBaseValue;
import com.probending.probending.managers.database.values.IntegerValue;
import com.probending.probending.managers.database.values.StringValue;
import org.bukkit.entity.Player;

import java.util.UUID;

import static com.probending.probending.ProBending.playerM;

public class PlayerDataTable extends DataBaseTable {

    public final String NAME = "username";
    public final String WINS = "wins";
    public final String LOST = "lost";
    public final String KILLS = "kills";
    public final String TIES = "ties";


    public PlayerDataTable(DataBaseManager manager) {
        super(manager);
    }

    @Override
    public String getName() {
        return "PBBenders";
    }

    @Override
    public DataBaseValue<?>[] getValues() {
        return new DataBaseValue[] {
                new StringValue("username", 100, "NULL"),
                new IntegerValue("wins"),
                new IntegerValue("lost"),
                new IntegerValue("kills"),
                new IntegerValue("ties")
        };
    }

    @Override
    public String getIndex() {
        return "UUID";
    }

    public void createTeamPlayer(Player p) {
        createTeamPlayer(p.getUniqueId(), p.getName());
    }

    public void createTeamPlayer(final UUID uuid, final String playerName) {
        getIndex(uuid.toString(),
                (rs) -> {
            try {
                if (rs != null) {
                    setStringField(uuid.toString(), NAME, playerName);
                    playerM.addPBPlayer(new PBPlayer(
                            uuid.toString(),
                            playerName,
                            rs.getInt(WINS),
                            rs.getInt(LOST),
                            rs.getInt(KILLS),
                            rs.getInt(TIES)));
                } else {
                    putDefault(uuid.toString());
                    setStringField(uuid.toString(), NAME, playerName);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
                });
    }
}
