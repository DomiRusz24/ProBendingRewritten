package com.probending.probending.core.arena.states;

import com.probending.probending.api.events.PBPlayerDamagePBPlayerEvent;
import com.probending.probending.api.events.PBPlayerKnockOutEvent;
import com.probending.probending.api.events.PBPlayerUpdateStageEvent;
import com.probending.probending.command.abstractclasses.Command;
import com.probending.probending.config.CommandConfig;
import me.domirusz24.plugincore.config.annotations.Language;
import com.probending.probending.core.arena.ActiveArena;
import com.probending.probending.core.enums.TeamTag;
import com.probending.probending.managers.PAPIManager;
import com.probending.probending.util.UtilMethods;

public class EndingState extends AbstractArenaHandler {

    @Language("ActiveArena.End.Message")
    public static String LANG_END_MESSAGE = "You will be soon transported to lobby!";

    @Language("ActiveArena.End.MessageDesc")
    public static String LANG_END_MESSAGE_DESC = "Don't forget to say your GG's!";

    private final TeamTag winningTeam;

    public EndingState(ActiveArena arena, TeamTag winningTeam) {
        super(arena);
        this.winningTeam = winningTeam;
    }

    @Override
    public void onStart() {
        getArena().getPlayers(false).forEach((player) -> {UtilMethods.freezePlayer(player.getPlayer(), false);});
        getArena().sendTitle(PAPIManager.setPlaceholders(getArena(), LANG_END_MESSAGE), PAPIManager.setPlaceholders(getArena(), LANG_END_MESSAGE_DESC), 5, 50, 5, true);
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
    }

    int sec = 0;

    int seconds = 5;

    @Override
    public void onUpdate() {
        sec++;
        if (sec > 3) {
            seconds--;
            if (seconds == 0) {
                if (winningTeam != null) {
                    getArena().getTeam(winningTeam).onWin();
                    getArena().getTeam(winningTeam.getOther()).onLose();
                }
                getArena().forceUnstableStop(winningTeam);
            } else {
                getArena().sendTitle("", UtilMethods.getNumberPrefix(seconds) + seconds, 0, 20, 0, true);
            }
        }
    }
}
