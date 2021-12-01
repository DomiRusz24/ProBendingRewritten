package com.probending.probending.core.arena.states;

import com.probending.probending.api.events.PBPlayerDamagePBPlayerEvent;
import com.probending.probending.api.events.PBPlayerKnockOutEvent;
import com.probending.probending.api.events.PBPlayerUpdateStageEvent;
import com.probending.probending.config.CommandConfig;
import com.probending.probending.core.arena.states.midroundstate.MidRoundStateType;
import com.probending.probending.core.players.ActivePlayer;
import me.domirusz24.plugincore.config.annotations.Language;
import com.probending.probending.core.arena.ActiveArena;
import com.probending.probending.core.enums.ArenaState;
import com.probending.probending.core.enums.TeamTag;
import com.probending.probending.managers.PAPIManager;
import com.probending.probending.util.UtilMethods;
import org.bukkit.entity.Player;

public class MidRoundState extends AbstractArenaHandler {

    @Language("ActiveArena.MidRound.RoundEnd")
    public static String LANG_ROUND_END = "Round %arena_round% has ended!";

    // ---

    @Language("ActiveArena.MidRound.TeamResult")
    public static String LANG_ROUND_TEAM_RESULT = "Team %team_name% has won!";
    @Language("ActiveArena.MidRound.MatchPoint")
    public static String LANG_ROUND_MATCH_POINT = "If this happens again, team %team_name% will win!";

    @Language("ActiveArena.MidRound.TieResult")
    public static String LANG_ROUND_TIE_RESULT = "This round has been ended in a tie!";

    @Language("ActiveArena.MidRound.TieBreakerResult")
    public static String LANG_ROUND_TIEBREAKER_RESULT = "Due to a close game, a TieBreaker will commence!";

    @Language("ActiveArena.MidRound.GameEnd")
    public static String LANG_ROUND_GAME_END = "Team %team_name% has won!";

    @Language("ActiveArena.MidRound.GameEndTie")
    public static String LANG_ROUND_GAME_END_TIE = "Game has been ended in a tie!";

    // ---

    @Language("ActiveArena.MidRound.RoundShortlyStart")
    public static String LANG_ROUND_SHORTLY_START = "Round %arena_round% will shortly start!";

    @Language("ActiveArena.MidRound.GameShortlyEnd")
    public static String LANG_GAME_SHORTLY_END = "Game will shortly end!";

    private final AbstractArenaHandler nextHandler;
    private final ArenaState nextState;
    private final TeamTag winningRound;

    private final MidRoundStateType type;

    public MidRoundState(ActiveArena arena, AbstractArenaHandler nextHandler, ArenaState nextState, TeamTag winningRound, MidRoundStateType type) {
        super(arena);
        this.nextHandler = nextHandler;
        this.nextState = nextState;
        this.winningRound = winningRound;
        this.type = type;
    }

    @Override
    public void onStart() {
        getArena().sendTitle(PAPIManager.setPlaceholder(getArena(), LANG_ROUND_END), "", 5, 50, 5, true);
        for (ActivePlayer p : getArena().getPlayers(false)) {
            UtilMethods.freezePlayer(p.getPlayer(), true);
        }
        if (winningRound != null) {
            for (ActivePlayer player : getArena().getTeam(winningRound).getPlayers(true)) {
                CommandConfig.Commands.ArenaRoundWinPlayer.run(getArena().getArena(), player.getPlayer());
            }
            for (ActivePlayer player : getArena().getTeam(winningRound.getOther()).getPlayers(true)) {
                CommandConfig.Commands.ArenaRoundLosePlayer.run(getArena().getArena(), player.getPlayer());
            }
        }
        type.runFirstStage(this);
    }

    @Override
    public void onPlayerKnockOut(PBPlayerKnockOutEvent event) {
        getArena().getCancelHandler().onPlayerKnockOut(event);
    }

    @Override
    public void onPBPlayerDamagePBPlayer(PBPlayerDamagePBPlayerEvent event) {
        getArena().getCancelHandler().onPBPlayerDamagePBPlayer(event);
    }

    @Override
    public void onPlayerUpdateStage(PBPlayerUpdateStageEvent event) {
        getArena().getCancelHandler().onPlayerUpdateStage(event);
    }

    public TeamTag getWinningRound() {
        return winningRound;
    }

    private int sec = 0;

    private int countdown = 10;

    @Override
    public void onUpdate() {
        sec++;
        if (sec == 3) {
            type.runSecondStage(this);
        } else if (sec == 6) {
            type.runThirdStage(this);
        } else if (sec >= 9) {
            if (!type.countdown()) {
                endMidRoundState();
            } else {
                countdown--;
                if (countdown == 0) {
                    endMidRoundState();
                } else {
                    getArena().sendTitle(UtilMethods.getNumberPrefix(countdown) + countdown, "", 0, 20, 0, true);
                }
            }
        }
    }

    private void endMidRoundState() {
        for (ActivePlayer p : getArena().getPlayers(false)) {
            UtilMethods.freezePlayer(p.getPlayer(), false);
            p.getPlayer().setHealth(20);
        }
        type.runFourthStage(this);
        getArena().setState(nextState);
        getArena().setHandler(nextHandler);
    }

    public MidRoundStateType getType() {
        return type;
    }
}
