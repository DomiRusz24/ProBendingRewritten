package com.probending.probending.core.arena.states.midroundstate;

import com.probending.probending.core.arena.states.MidRoundState;

public abstract class MidRoundStateType {

    public abstract void runFirstStage(MidRoundState stage);
    public abstract void runSecondStage(MidRoundState stage);
    public abstract void runThirdStage(MidRoundState stage);
    public abstract void runFourthStage(MidRoundState stage);

    public abstract boolean countdown();


}
