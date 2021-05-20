package com.probending.probending.core.arena.states.midroundstate;

import com.probending.probending.core.arena.states.MidRoundState;
import com.probending.probending.core.players.ActivePlayer;
import com.probending.probending.managers.PAPIManager;

import static com.probending.probending.core.arena.states.MidRoundState.*;

public class RoundEnd extends MidRoundStateType {


    private final boolean roundPoint;


    public RoundEnd(boolean roundPoint) {
        this.roundPoint = roundPoint;
    }

    public RoundEnd() {
        this.roundPoint = false;
    }



    @Override
    public void runFirstStage(MidRoundState stage) {}

    @Override
    public void runSecondStage(MidRoundState state) {
        if (state.getWinningRound() != null) {
            if (roundPoint) {
                state.getArena().sendTitle(PAPIManager.setPlaceholders(state.getArena(), PAPIManager.setPlaceholders(state.getArena().getTeam(state.getWinningRound()), LANG_ROUND_TEAM_RESULT)),
                        PAPIManager.setPlaceholders(state.getArena(), LANG_ROUND_MATCH_POINT.replaceAll("%team%", state.getWinningRound().toString())),
                        5, 50, 5, true);
            } else {
                state.getArena().sendTitle(PAPIManager.setPlaceholders(state.getArena(), PAPIManager.setPlaceholders(state.getArena().getTeam(state.getWinningRound()), LANG_ROUND_TEAM_RESULT)),
                        "",
                        5, 50, 5, true);
            }
        } else {
            state.getArena().sendTitle(PAPIManager.setPlaceholders(state.getArena(), LANG_ROUND_TIE_RESULT),
                    "",
                    5, 50, 5, true);
        }
    }

    @Override
    public void runThirdStage(MidRoundState state) {
        state.getArena().teleportToStartingLocations();
        state.getArena().getArena().getRollback(null);
        state.getArena().sendTitle(PAPIManager.setPlaceholders(state.getArena(), MidRoundState.LANG_ROUND_SHORTLY_START.replace("%arena_round%", String.valueOf(state.getArena().getRound() + 1))), "", 5, 50, 5, true);
    }

    @Override
    public void runFourthStage(MidRoundState state) {
        state.getArena().raiseRound();
        state.getArena().getPlayers(false).forEach(ActivePlayer::resetTiredness);
    }

    @Override
    public boolean countdown() {
        return true;
    }
}
