package com.probending.probending.core.team;

import com.probending.probending.ProBending;
import me.domirusz24.plugincore.config.annotations.Language;
import com.probending.probending.core.arena.Arena;
import com.probending.probending.core.gui.TeamPlayGUI;
import com.probending.probending.core.players.PBMember;
import com.probending.probending.core.players.PBPlayer;
import com.probending.probending.core.players.PBPlayerWrapper;
import me.domirusz24.plugincore.core.team.AbstractTeam;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.probending.probending.ProBending.SqlM;

public class PBTeam extends AbstractTeam<PBMember> {

    private int wins;
    private int lost;

    private final TeamPlayGUI playGUI;
    

    public PBTeam(String name, int wins, int lost) {
        super(name, 3);
        this.wins = wins;
        this.lost = lost;
        playGUI = new TeamPlayGUI(this);
        ProBending.teamM.PBTEAM_BY_NAME.put(name, this);

    }

    public boolean contains(PBPlayer player) {
        for (PBMember member : getPlayers()) {
            if (player.getUuid().equals(member.getUuid().toString())) {
                return true;
            }
        }
        return false;
    }

    public boolean removePlayer(PBPlayer player) {
        for (PBMember member : getPlayers()) {
            if (player.getUuid().equals(member.getUuid().toString())) {
                removePlayer(member);
                return true;
            }
        }
        return false;
    }

    public PBMember getMember(PBPlayer player) {
        for (PBMember member : getPlayers()) {
            if (player.getUuid().equals(member.getUuid())) {
                return member;
            }
        }
        return null;
    }

    public PBMember getCaptain() {
        for (PBMember player : getPlayers()) {
            if (player.hasRole("captain")) {
                return player;
            }
        }
        return null;
    }

    private String getPlayersList() {
        StringBuilder builder = new StringBuilder();
        for (PBMember player : getPlayers()) {
            builder.append(player.toDBString()).append(";");
        }
        return builder.toString();
    }

    public List<PBPlayer> getPBPlayers() {
        return getPlayers().stream().map((t) -> {
            if (ProBending.playerDataM.getPlayer(t.getPlayer().getPlayer()) != null) {
                return (PBPlayer) ProBending.playerDataM.getPlayer(t.getPlayer().getPlayer());
            } else {
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public List<Player> getBukkitPlayers() {
        return getPlayers().stream().map((t) -> {
            if (t.getPlayer() != null) {
                return t.getPlayer().getPlayer();
            } else {
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    @Override
    public boolean onAddPlayer(PBMember player) {
        ProBending.teamM.PBTEAM_BY_PLAYER.put(player.getName(), this);
        return true;
    }

    @Override
    public boolean onRemovePlayer(PBMember player) {
        ProBending.teamM.PBTEAM_BY_PLAYER.remove(player.getName());
        return true;
    }

    @Override
    public boolean addPlayer(PBMember player) {
        boolean bool = super.addPlayer(player);
        if (bool) {
            PBPlayer pbPlayer = PBPlayer.of(player.getPlayer().getPlayer());
            if (pbPlayer != null) {
                pbPlayer.setTeam(getName());
            } else {
                ProBending.playerTable.setStringField(player.getUuid().toString(), ProBending.playerTable.TEAM, getName());
            }
            ProBending.teamTable.setStringField(getName(), ProBending.teamTable.PLAYERS, getPlayersList());
            ProBending.teamM.PBTEAM_BY_PLAYER.put(player.getName(), this);
        }
        return bool;
    }

    @Override
    public boolean removePlayer(PBMember player) {
        boolean remove = true;
        if (player.hasRole("captain")) {
            for (PBMember member : getPlayers()) {
                if (member.getName().equals(player.getName())) continue;
                member.addRoles("captain");
                remove = false;
            }
        } else {
            remove = false;
        }
        PBPlayer pbPlayer = PBPlayer.of(player.getPlayer().getPlayer());
        if (pbPlayer != null) {
            pbPlayer.setTeam("NULL");
        } else {
            ProBending.playerTable.setStringField(player.getUuid().toString(), ProBending.playerTable.TEAM, "NULL");
        }
        boolean bool = super.removePlayer(player);
        if (remove) {
            Bukkit.getScheduler().runTask(ProBending.plugin, this::removeTeam);
        } else {
            ProBending.teamTable.setStringField(getName(), ProBending.teamTable.PLAYERS, getPlayersList());
            ProBending.teamM.PBTEAM_BY_PLAYER.remove(player.getName());
        }
        return bool;
    }

    public void removeTeam() {
        purgePlayers();
        ProBending.teamTable.removeIndex(getName());
        ProBending.teamM.PBTEAM_BY_NAME.remove(this.getName());
    }

    @Override
    public boolean onPurgePlayers() {
        return true;
    }

    @Override
    public void sendMessage(String message) {
        for (PBMember player : getPlayers()) {
            PBPlayerWrapper pbPlayer = player.getPlayer();
            if (pbPlayer != null) {
                pbPlayer.getPlayer().sendMessage(message);
            }
        }
    }

    @Override
    public void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        for (PBMember player : getPlayers()) {
            PBPlayerWrapper pbPlayer =  player.getPlayer();
            if (pbPlayer != null) {
                pbPlayer.getPlayer().sendTitle(title, subtitle, fadeIn, stay, fadeOut);
            }
        }
    }

    public boolean throwIntoGame(List<Player> players) {
        return ProBending.arenaM.throwIntoGame(players);
    }

    public boolean throwIntoGame(Arena arena, List<Player> players) {
        return arena.throwIntoGame(players);
    }

    public boolean throwIntoGame() {
        return ProBending.arenaM.throwIntoGame((Player[])
                getBukkitPlayers().stream()
                        .filter((p) -> ProBending.teamM.getTempTeam(p) == null && ProBending.playerM.getActivePlayer(p) == null)
                        .toArray());
    }

    public boolean throwIntoGame(Arena arena) {
        return arena.throwIntoGame((Player[])
                getBukkitPlayers().stream()
                        .filter((p) -> ProBending.teamM.getTempTeam(p) == null && ProBending.playerM.getActivePlayer(p) == null)
                        .toArray());
    }

    public boolean allOnline() {
        for (PBMember player : getPlayers()) {
            if (player.getPlayer() == null) {
                return false;
            }
        }
        return true;
    }

    public int getWins() {
        return wins;
    }

    public int getLost() {
        return lost;
    }

    public void setWins(int wins) {
        this.wins = wins;
        ProBending.teamTable.setIntegerField(getName(), ProBending.teamTable.WINS, wins);
    }

    public void setLost(int lost) {
        this.lost = lost;
        ProBending.teamTable.setIntegerField(getName(), ProBending.teamTable.LOST, lost);
    }

    public TeamPlayGUI getPlayGUI() {
        return playGUI;
    }

    @Language("Team.PB.TEAM_INFO")
    public static String LANG_TEAM_INFO = "--- %team_name% ---||%team_players%||-------------------";

    @Language("Team.PB.EMPTY_SLOT")
    public static String LANG_EMPTY_SLOT = "&7 - NONE||";

    @Language("Team.PB.PLAYER_SLOT")
    public static String LANG_PLAYER_SLOT = " - %member_captain_prefix% %member_name%||";

    @Override
    public String getPlayerInfoSyntax() {
        return LANG_PLAYER_SLOT;
    }

    @Override
    public String getNullPlayerInfoSyntax() {
        return LANG_EMPTY_SLOT;
    }

    @Override
    public String getSyntax() {
        return LANG_TEAM_INFO;
    }

    @Override
    protected String _onPlaceholderRequest(String message) {
        switch (message.toLowerCase()) {
            case "wins":
                return String.valueOf(wins);
            case "lost":
                return String.valueOf(lost);
        }
        return null;
    }
}
