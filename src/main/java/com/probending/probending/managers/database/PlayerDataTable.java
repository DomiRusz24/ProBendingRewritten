package com.probending.probending.managers.database;

import com.probending.probending.core.players.PBPlayer;
import com.probending.probending.managers.database.values.DataBaseValue;
import com.probending.probending.managers.database.values.IntegerValue;
import com.probending.probending.managers.database.values.StringValue;
import org.bukkit.entity.Player;

import javax.print.attribute.standard.MediaSize;
import java.util.UUID;

import static com.probending.probending.ProBending.playerM;

public class PlayerDataTable extends DataBaseTable {

    public final String NAME = "username";
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
    public DataBaseValue<?>[] getValues() {
        return new DataBaseValue[] {
                new StringValue(NAME, 100, "NULL"),
                new IntegerValue(WINS),
                new IntegerValue(LOST),
                new IntegerValue(KILLS),
                new IntegerValue(TIES),
                new StringValue(TEAM, 100, "NULL"),
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
                            rs.getInt(TIES),
                            rs.getString(TEAM)));
                } else {
                    putDefault(uuid.toString());
                    setStringField(uuid.toString(), NAME, playerName);
                    playerM.addPBPlayer(new PBPlayer(
                            uuid.toString(),
                            playerName,
                            0,
                            0,
                            0,
                            0,
                            "NULL"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
                });
    }

    public void createTeamPlayer(final UUID uuid) {
        getIndex(uuid.toString(),
                (rs) -> {
                    try {
                        if (rs != null) {
                            playerM.addPBPlayer(new PBPlayer(
                                    uuid.toString(),
                                    rs.getString(NAME),
                                    rs.getInt(WINS),
                                    rs.getInt(LOST),
                                    rs.getInt(KILLS),
                                    rs.getInt(TIES),
                                    rs.getString(TEAM)));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }
}
