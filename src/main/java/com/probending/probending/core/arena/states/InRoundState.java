package com.probending.probending.core.arena.states;

import com.probending.probending.api.enums.KnockOutCause;
import com.probending.probending.api.events.PBPlayerDamagePBPlayerEvent;
import com.probending.probending.api.events.PBPlayerKnockOutEvent;
import com.probending.probending.api.events.PBPlayerLoseStageEvent;
import com.probending.probending.api.events.PBPlayerUpdateStageEvent;
import com.probending.probending.config.CommandConfig;
import com.probending.probending.core.arena.states.midroundstate.GameEnd;
import com.probending.probending.core.arena.states.midroundstate.RoundEnd;
import com.probending.probending.core.arena.states.midroundstate.TieBreakerStart;
import com.probending.probending.core.players.ActivePlayer;
import com.probending.probending.core.annotations.Language;
import com.probending.probending.core.arena.ActiveArena;
import com.probending.probending.core.enums.ArenaState;
import com.probending.probending.core.enums.TeamTag;
import com.probending.probending.util.UtilMethods;
import org.bukkit.Bukkit;

public class InRoundState extends AbstractArenaHandler {

    @Language("ActiveArena.Warning")
    public static String LANG_WARNING = "&c&l!";

    @Language("ActiveArena.IllegalStageGain")
    public static String LANG_ILLEGAL_STAGE_GAIN = "Your team didn't gain this stage yet!";

    @Language("ActiveArena.StageLose")
    public static String LANG_LOSE_STAGE = "You have lost a stage!";

    @Language("ActiveArena.Start")
    public static String LANG_START = "Start!";

    public InRoundState(ActiveArena arena) {
        super(arena);
    }

    public InRoundState(ActiveArena arena, int tickTime) {
        super(arena);
        this.sec = (int) ((float) tickTime / 20f);
    }

    @Override
    public int getTimeLeft() {
        return (duration * 20) - getTime();
    }

    @Override
    public int getTime() {
        return sec * 20;
    }

    @Override
    public void onStart() {
        for (ActivePlayer p : getArena().getPlayers(false)) {
            UtilMethods.freezePlayer(p.getPlayer(), false);
        }
        getArena().sendTitle((LANG_START), "", 5, 20, 5, true);
        duration = getArena().getArena().getArenaConfig().getRoundTime();
    }

    @Override
    public void onPlayerKnockOut(PBPlayerKnockOutEvent event) {
        if (event.getCause() != KnockOutCause.LEFT) {
            getArena().removePlayer(event.getPlayer(), true);
        }
    }

    @Override
    public void onPBPlayerDamagePBPlayer(PBPlayerDamagePBPlayerEvent event) {
    }

    @Override
    public void onPlayerUpdateStage(PBPlayerUpdateStageEvent event) {
        if (event.getRingOffset() > 0) {
            event.getPlayer().dragTo(event.getPlayer().getRing());
            event.getPlayer().getPlayer().sendTitle((LANG_WARNING), (LANG_ILLEGAL_STAGE_GAIN), 5, 20, 5);
        } else if (event.getRingOffset() < 0) {
            PBPlayerLoseStageEvent loseEvent = new PBPlayerLoseStageEvent(event.getArena(), event.getArenaState(), event.getPlayer(), event.getPlayer().getRing().getFromOffset(event.getPlayer().getTeamTag(), -1), -1);
            Bukkit.getPluginManager().callEvent(loseEvent);
            if (!loseEvent.isCancelled()) {
                event.getPlayer().setRing(loseEvent.getCurrentStage());
                event.getPlayer().getPlayer().sendTitle((LANG_WARNING), (LANG_LOSE_STAGE), 5, 20, 5);
                getArena().setTeamToCheck(event.getPlayer().getTeamTag());
                CommandConfig.Commands.RoundPlayerLoseStage.run(getArena().getArena(), event.getPlayer().getPlayer());
                if (getArena().getArena().getArenaConfig().getResetFatigueOnStageLose()) {
                    event.getPlayer().resetTiredness();
                }
            }
            event.getPlayer().dragTo(event.getPlayer().getRing());
        }
    }

    int sec = 0;

    int duration;

    @Override
    public void onUpdate() {
        sec++;
        if (sec > duration) {
            int redPoints = getArena().getTeam(TeamTag.RED).getPoints();
            int bluePoints = getArena().getTeam(TeamTag.BLUE).getPoints();
            int roundWin = getArena().getArena().getArenaConfig().getRoundWin();
            TeamTag winningTeam = getArena().getWinningTeamByRound();
            if (winningTeam == null) {
                for (TeamTag tag : TeamTag.values()) {
                    getArena().getTeam(tag).raisePoint();
                }
                getArena().setState(ArenaState.MID_ROUND);
                if ((bluePoints >= roundWin) && (redPoints >= roundWin)) {
                    getArena().setHandler(new MidRoundState(getArena(), new TieBreakerState(getArena()), ArenaState.TIE_BREAKER, null, new TieBreakerStart()));
                } else {
                    getArena().setHandler(new MidRoundState(getArena(), new InRoundState(getArena()), ArenaState.IN_ROUND, null, new RoundEnd()));
                }
            } else {
                getArena().getTeam(winningTeam).raisePoint();
                if ((bluePoints >= roundWin) && (redPoints >= roundWin)) {
                    getArena().setState(ArenaState.MID_ROUND);
                    getArena().setHandler(new MidRoundState(getArena(), new TieBreakerState(getArena()), ArenaState.TIE_BREAKER, null, new TieBreakerStart()));
                } else if ((bluePoints >= roundWin) || (redPoints >= roundWin)) {
                    int difference = Math.abs(bluePoints - redPoints);
                    if (difference == 1) {
                        getArena().setState(ArenaState.MID_ROUND);
                        getArena().setHandler(new MidRoundState(getArena(), new InRoundState(getArena()), ArenaState.IN_ROUND, winningTeam, new RoundEnd(true)));
                    } else if (difference > 1) {
                        getArena().setState(ArenaState.MID_ROUND);
                        getArena().setHandler(new MidRoundState(getArena(), new EndingState(getArena(), winningTeam), ArenaState.STOP, winningTeam, new GameEnd()));
                    }
                } else {
                    getArena().setState(ArenaState.MID_ROUND);
                    getArena().setHandler(new MidRoundState(getArena(), new InRoundState(getArena()), ArenaState.IN_ROUND, winningTeam, new RoundEnd()));
                }
            }
        }
    }
}
