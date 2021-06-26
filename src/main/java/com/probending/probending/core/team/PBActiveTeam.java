package com.probending.probending.core.team;

import com.probending.probending.core.arena.ActiveArena;
import com.probending.probending.core.enums.TeamTag;

public class PBActiveTeam extends ActiveTeam {

    private final PBTeam team;

    public PBActiveTeam(ActiveArena arena, TeamTag tag, PBTeam team) {
        super(arena, tag);
        this.team = team;
    }

    @Override
    public String getName() {
        return "" + getTag().getColor() + "" + team.getName() + "";
    }

    @Override
    public void onWin() {
        super.onWin();
        team.setWins(team.getWins() + 1);
    }

    @Override
    public void onLose() {
        super.onLose();
        team.setLost(team.getLost() + 1);
    }
}
