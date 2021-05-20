package com.probending.probending.core.team;

import com.probending.probending.ProBending;
import com.probending.probending.core.annotations.Language;
import com.probending.probending.core.players.PBMember;
import com.probending.probending.core.players.PBPlayer;
import com.probending.probending.core.players.PBPlayerWrapper;
import com.probending.probending.managers.database.TeamDataTable;
import org.bukkit.Bukkit;

import java.util.UUID;

import static com.probending.probending.ProBending.SqlM;

public class PBTeam extends AbstractTeam<PBMember> {

    private int wins;
    private int lost;
    

    public PBTeam(String name, int wins, int lost) {
        super(name, 3);
        this.wins = wins;
        this.lost = lost;
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
            if (player.getUuid().equals(member.getUuid().toString())) {
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
        SqlM.teamTable.setStringField(getName(), SqlM.teamTable.PLAYERS, getPlayersList());
        return bool;
    }

    @Override
    public boolean removePlayer(PBMember player) {
        boolean remove = true;
        if (player.hasRole("captain")) {
            for (PBMember member : getPlayers()) {
                if (member.getName().equals(player.getName())) continue;
                member.addRoles("captain");
                return false;
            }
        } else {
            remove = false;
        }
        boolean bool = super.removePlayer(player);
        if (remove) {
            Bukkit.getScheduler().runTask(ProBending.plugin, this::removeTeam);
        } else {
            SqlM.teamTable.setStringField(getName(), SqlM.teamTable.PLAYERS, getPlayersList());
        }
        return bool;
    }

    public void removeTeam() {
        purgePlayers();
        SqlM.teamTable.removeIndex(getName());
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

    public int getWins() {
        return wins;
    }

    public int getLost() {
        return lost;
    }

    public void setWins(int wins) {
        this.wins = wins;
        SqlM.teamTable.setIntegerField(getName(), SqlM.teamTable.WINS, wins);
    }

    public void setLost(int lost) {
        this.lost = lost;
        SqlM.teamTable.setIntegerField(getName(), SqlM.teamTable.LOST, lost);
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
