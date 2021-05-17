package com.probending.probending.core.arena.states.midroundstate;

import com.probending.probending.core.arena.states.MidRoundState;
import com.probending.probending.managers.PAPIManager;

import static com.probending.probending.core.arena.states.MidRoundState.LANG_ROUND_TIEBREAKER_RESULT;

public class TieBreakerStart extends MidRoundStateType {
    @Override
    public void runFirstStage(MidRoundState stage) {

    }

    @Override
    public void runSecondStage(MidRoundState stage) {
        stage.getArena().sendTitle(PAPIManager.setPlaceholders(stage.getArena(), LANG_ROUND_TIEBREAKER_RESULT),
                "",
                5, 50, 5, true);
    }

    @Override
    public void runThirdStage(MidRoundState stage) {
        stage.getArena().sendTitle(PAPIManager.setPlaceholders(stage.getArena(), MidRoundState.LANG_ROUND_SHORTLY_START.replace("%arena_round%", String.valueOf(stage.getArena().getRound() + 1))),
                "",
                5, 50, 5, true);
    }

    @Override
    public void runFourthStage(MidRoundState stage) {

    }

    @Override
    public boolean countdown() {
        return true;
    }
}
