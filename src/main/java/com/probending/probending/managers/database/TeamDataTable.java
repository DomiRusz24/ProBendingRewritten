package com.probending.probending.managers.database;

import com.probending.probending.ProBending;
import com.probending.probending.core.players.PBMember;
import com.probending.probending.core.players.PBPlayer;
import com.probending.probending.core.team.PBTeam;
import com.probending.probending.managers.database.values.DataBaseValue;
import com.probending.probending.managers.database.values.IntegerValue;
import com.probending.probending.managers.database.values.StringValue;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class TeamDataTable extends DataBaseTable {

    public final String WINS = "wins";
    public final String LOST = "lost";

    public final String PLAYERS = "players";


    public TeamDataTable(DataBaseManager manager) {
        super(manager);
    }

    @Override
    public String getName() {
        return "PBTeams";
    }

    @Override
    public DataBaseValue<?>[] getValues() {
        return new DataBaseValue[]{
                new StringValue(PLAYERS, 200, "NULL"),
                new IntegerValue(WINS),
                new IntegerValue(LOST)
        };
    }

    public void createTeam(Player player, String name, Consumer<PBTeam> team) {
        createTeam(player.getUniqueId().toString(), player.getName(), name, team);
    }

    public void createTeam(String uuid, String playerName, String name, Consumer<PBTeam> teamConsumer) {
        PBPlayer player = ProBending.playerM.getPlayer(UUID.fromString(uuid));
        if (player != null && name.length() <= 20 && !name.equalsIgnoreCase("null")) {
            getIndex(name, (rs) -> {
                try {
                    if (rs.next()) {
                        List<PBMember> players = stringToList(rs.getString(PLAYERS)).stream().map(PBMember::getFromString).collect(Collectors.toList());
                        int wins = rs.getInt(WINS);
                        int lost = rs.getInt(LOST);
                        if (players.stream().filter(p -> uuid.equals(p.getUuid().toString())).toArray().length == 1) {
                            Bukkit.getScheduler().runTask(manager.getPlugin(), () -> {
                                PBTeam team = new PBTeam(name, wins, lost);
                                for (PBMember pbMember : players) {
                                    team.addPlayer(pbMember);
                                }
                                teamConsumer.accept(team);
                            });
                        }
                    } else {
                        putDefault(index);
                        Bukkit.getScheduler().runTask(manager.getPlugin(), () -> {
                            PBTeam team = new PBTeam(name, 0, 0);
                            team.addPlayer(new PBMember(uuid, playerName, Collections.singletonList(ProBending.playerM.getRole("captain"))));
                            teamConsumer.accept(team);
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private PBPlayer getPlayer(String uuid) {
        if (uuid.equals("NULL")) return null;
        return ProBending.playerM.getPlayer(UUID.fromString(uuid));
    }

    @Override
    public String getIndex() {
        return "name";
    }
}
