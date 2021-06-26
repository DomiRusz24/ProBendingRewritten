package com.probending.probending.managers.database;

import com.probending.probending.core.players.PBPlayer;
import com.probending.probending.managers.database.values.DataBaseValue;
import com.probending.probending.managers.database.values.IntegerValue;
import com.probending.probending.managers.database.values.StringValue;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.print.attribute.standard.MediaSize;
import java.util.UUID;
import java.util.function.Consumer;

import static com.probending.probending.ProBending.playerM;
import static com.probending.probending.ProBending.plugin;

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

    public void createTeamPlayer(Player p, Consumer<PBPlayer> consumer) {
        createTeamPlayer(p.getUniqueId(), p.getName(), consumer);
    }

    public void createTeamPlayer(final UUID uuid, final String playerName, Consumer<PBPlayer> consumer) {
        getIndex(uuid.toString(),
                (rs) -> {
            try {
                if (rs != null) {
                    setStringField(uuid.toString(), NAME, playerName);
                    PBPlayer player = new PBPlayer(
                            uuid.toString(),
                            playerName,
                            rs.getInt(WINS),
                            rs.getInt(LOST),
                            rs.getInt(KILLS),
                            rs.getInt(TIES),
                            rs.getString(TEAM));
                    Bukkit.getScheduler().runTask(plugin, () -> consumer.accept(player));
                } else {
                    putDefault(uuid.toString());
                    setStringField(uuid.toString(), NAME, playerName);
                    PBPlayer player = new PBPlayer(
                            uuid.toString(),
                            playerName,
                            0,
                            0,
                            0,
                            0,
                            "NULL");
                    Bukkit.getScheduler().runTask(plugin, () -> consumer.accept(player));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
                });
    }

    public void createTeamPlayer(final UUID uuid, final String playerName) {
        createTeamPlayer(uuid, playerName, (p) -> {});
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
