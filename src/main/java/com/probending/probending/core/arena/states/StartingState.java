package com.probending.probending.core.arena.states;

import com.probending.probending.api.events.PBPlayerDamagePBPlayerEvent;
import com.probending.probending.api.events.PBPlayerKnockOutEvent;
import com.probending.probending.api.events.PBPlayerUpdateStageEvent;
import com.probending.probending.core.players.ActivePlayer;
import com.probending.probending.core.arena.ActiveArena;
import com.probending.probending.core.enums.ArenaState;
import com.probending.probending.core.enums.TeamTag;
import com.probending.probending.util.UtilMethods;

public class StartingState extends AbstractArenaHandler {
    public StartingState(ActiveArena arena) {
        super(arena);
    }

    @Override
    public void onStart() {
        getArena().teleportToStartingLocations();
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

    private int sec = 0;

    private int seconds = 10;

    @Override
    public void onUpdate() {
        sec++;
        if (seconds > 0) {
            seconds--;
        }
        if (seconds == 0) {
            getArena().teleportToStartingLocations();
            getArena().setState(ArenaState.IN_ROUND);
            getArena().setHandler(new InRoundState(getArena()));
        } else {
            if (seconds < 4) {
                if (seconds == 3) {
                    for (TeamTag team : TeamTag.values()) {
                        getArena().teleportToStartingLocations();
                        for (ActivePlayer p : getArena().getTeam(team).getPlayers(false)) {
                            UtilMethods.freezePlayer(p.getPlayer(), true);
                        }
                    }
                }
                getArena().sendTitle(UtilMethods.getNumberPrefix(seconds) + seconds, "", 5, 10, 5, true);
            } else {
                getArena().sendTitle("", UtilMethods.getNumberPrefix(seconds) + seconds, 5, 10, 5, true);
            }
        }
    }
}
