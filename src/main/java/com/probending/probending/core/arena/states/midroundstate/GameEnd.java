package com.probending.probending.core.arena.states.midroundstate;

import com.probending.probending.core.annotations.Language;
import com.probending.probending.core.arena.states.MidRoundState;
import com.probending.probending.managers.PAPIManager;

import static com.probending.probending.core.arena.states.MidRoundState.*;

public class GameEnd extends MidRoundStateType {

    @Language("ActiveArena.MidRound.GameEndMessage")
    public static String LANG_END = "&cGame end!";

    @Override
    public void runFirstStage(MidRoundState state) {
        if (state.getWinningRound() != null)
            state.getArena().sendTitle(LANG_END,
                    PAPIManager.setPlaceholders(state.getArena(), PAPIManager.setPlaceholders(state.getArena().getTeam(state.getWinningRound()), LANG_ROUND_GAME_END)),
                    5, 50, 5, true);
        else
            state.getArena().sendTitle(PAPIManager.setPlaceholders(state.getArena(), LANG_ROUND_GAME_END_TIE),
                    "",
                    5, 50, 5, true);
    }

    @Override
    public void runSecondStage(MidRoundState state) {
        if (state.getWinningRound() != null) {
            state.getArena().sendTitle(LANG_END,
                    PAPIManager.setPlaceholders(state.getArena(), PAPIManager.setPlaceholders(state.getArena().getTeam(state.getWinningRound()), LANG_ROUND_GAME_END)),
                    5, 50, 5, true);
        } else {
            state.getArena().sendTitle(PAPIManager.setPlaceholders(state.getArena(), LANG_ROUND_GAME_END_TIE),
                    "",
                    5, 50, 5, true);
        }
    }

    @Override
    public void runThirdStage(MidRoundState stage) {
        stage.getArena().sendTitle(PAPIManager.setPlaceholders(stage.getArena(), LANG_GAME_SHORTLY_END), "", 5, 50, 5, true);
    }

    @Override
    public void runFourthStage(MidRoundState stage) {

    }

    @Override
    public boolean countdown() {
        return false;
    }
}
