package com.probending.probending.managers.database;

import com.probending.probending.ProBending;
import com.probending.probending.core.players.PBMember;
import com.probending.probending.core.players.PBPlayer;
import com.probending.probending.core.team.PBTeam;
import me.domirusz24.plugincore.managers.database.DataBaseManager;
import me.domirusz24.plugincore.managers.database.DataBaseTable;
import me.domirusz24.plugincore.managers.database.values.DataBaseValue;
import me.domirusz24.plugincore.managers.database.values.IntegerValue;
import me.domirusz24.plugincore.managers.database.values.StringValue;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

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

    public void createTeam(Player player, String name) {
        createTeam(player.getUniqueId().toString(), player.getName(), name);
    }

    public void createTeam(String uuid, String playerName, String name, Consumer<PBTeam> teamConsumer) {
        PBPlayer player = (PBPlayer) ProBending.playerDataM.getPlayer(playerName, UUID.fromString(uuid));
        if (ProBending.teamM.getPBTeamByName(name) != null) return;
        if (player != null && name.length() <= 20 && !name.equalsIgnoreCase("null")) {
            getIndex(name, (rs) -> {
                try {
                    if (rs != null) {
                        List<PBMember> players = DataBaseTable.stringToList(rs.getString(PLAYERS)).stream().map(PBMember::getFromString).collect(Collectors.toList());
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
                        putDefault(name);
                        Bukkit.getScheduler().runTask(manager.getPlugin(), () -> {
                            PBTeam team = new PBTeam(name, 0, 0);
                            team.addPlayer(new PBMember(playerName, uuid, Collections.singletonList(ProBending.playerM.getRole("captain"))));
                            teamConsumer.accept(team);
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public void createTeam(String uuid, String playerName, String name) {
        createTeam(uuid, playerName, name, (t) -> {});
    }

    @Override
    public String getIndex() {
        return "name";
    }
}
