package com.probending.probending.core.arena.states.midroundstate;

import me.domirusz24.plugincore.config.annotations.Language;
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
                    PAPIManager.setPlaceholder(state.getArena(), PAPIManager.setPlaceholder(state.getArena().getTeam(state.getWinningRound()), LANG_ROUND_GAME_END)),
                    5, 50, 5, true);
        else
            state.getArena().sendTitle(PAPIManager.setPlaceholder(state.getArena(), LANG_ROUND_GAME_END_TIE),
                    "",
                    5, 50, 5, true);
    }

    @Override
    public void runSecondStage(MidRoundState state) {
        if (state.getWinningRound() != null) {
            state.getArena().sendTitle(LANG_END,
                    PAPIManager.setPlaceholder(state.getArena(), PAPIManager.setPlaceholder(state.getArena().getTeam(state.getWinningRound()), LANG_ROUND_GAME_END)),
                    5, 50, 5, true);
        } else {
            state.getArena().sendTitle(PAPIManager.setPlaceholder(state.getArena(), LANG_ROUND_GAME_END_TIE),
                    "",
                    5, 50, 5, true);
        }
    }

    @Override
    public void runThirdStage(MidRoundState stage) {
        stage.getArena().sendTitle(PAPIManager.setPlaceholder(stage.getArena(), LANG_GAME_SHORTLY_END), "", 5, 50, 5, true);
    }

    @Override
    public void runFourthStage(MidRoundState stage) {

    }

    @Override
    public boolean countdown() {
        return false;
    }
}
