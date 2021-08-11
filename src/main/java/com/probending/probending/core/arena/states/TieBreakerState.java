package com.probending.probending.core.arena.states;

import com.probending.probending.api.enums.KnockOutCause;
import com.probending.probending.api.events.PBPlayerDamagePBPlayerEvent;
import com.probending.probending.api.events.PBPlayerKnockOutEvent;
import com.probending.probending.api.events.PBPlayerUpdateStageEvent;
import com.probending.probending.core.annotations.Language;
import com.probending.probending.core.arena.ActiveArena;
import com.probending.probending.core.enums.ArenaState;
import com.probending.probending.core.enums.Ring;
import com.probending.probending.core.enums.TeamTag;
import com.probending.probending.core.players.ActivePlayer;
import com.probending.probending.util.UtilMethods;

import java.util.ArrayList;
import java.util.List;

public class TieBreakerState extends AbstractArenaHandler {

    public TieBreakerState(ActiveArena arena) {
        super(arena);
    }

    private ActivePlayer getRandomPlayer(List<ActivePlayer> players) {
        return players.get((int) (Math.random() * players.size()));
    }

    private ArrayList<ActivePlayer> players = new ArrayList<>();

    @Override
    public void onStart() {
        players.add(getRandomPlayer(getArena().getTeam(TeamTag.BLUE).getPlayers(false)));
        players.add(getRandomPlayer(getArena().getTeam(TeamTag.RED).getPlayers(false)));
        for (TeamTag tag : TeamTag.values()) {
            for (ActivePlayer player : getArena().getTeam(tag).getPlayers(false)) {
                if (!players.contains(player)) {
                    player.setState(ActivePlayer.State.SPECTATING);
                }
            }
        }
        players.forEach(p -> {
            if (p.getTeamTag() == TeamTag.RED) {
                p.setRing(Ring.RED_TIEBREAKER);
            } else {
                p.setRing(Ring.BLUE_TIEBREAKER);
            }
            p.teleportTo(p.getRing());
            UtilMethods.freezePlayer(p.getPlayer(), true);
        });
        getArena().setState(ArenaState.TIE_BREAKER);
    }

    @Override
    public void onPlayerKnockOut(PBPlayerKnockOutEvent event) {
        if (event.getCause() != KnockOutCause.LEFT) {
            getArena().removePlayer(event.getPlayer(), true);
        }
    }

    @Override
    public void onPBPlayerDamagePBPlayer(PBPlayerDamagePBPlayerEvent event) {}

    @Override
    public void onPlayerUpdateStage(PBPlayerUpdateStageEvent event) {
        if (event.getRingOffset() < 0 || event.getRingOffset() > 1) {
            event.getPlayer().setState(ActivePlayer.State.DEAD);
        }
    }

    private int countdown = 10;

    @Language("ActiveArena.TieBreaker.Start")
    public static String LANG_START = "Player %player_1% and %player_2% have been chosen!";

    @Override
    public void onUpdate() {
        countdown--;
        if (countdown < 0) return;
        if (countdown == 9) {
            getArena().sendTitle("",
                    LANG_START
                            .replaceAll("%player_1%", players.get(0).getPlayer().getName())
                            .replaceAll("%player_2%", players.get(1).getPlayer().getName())
                    , 5, 50, 5, true);
        } else if (countdown < 6) {
            if (countdown == 0) {
                getArena().sendTitle(InRoundState.LANG_START, "", 5, 20, 5, true);
                players.forEach(p -> UtilMethods.freezePlayer(p.getPlayer(), false));
            } else {
                getArena().sendTitle(UtilMethods.getNumberPrefix(countdown) + countdown, "", 5, 10, 5, true);
            }
        }
    }

    @Override
    public boolean isTieBreaker() {
        return true;
    }
}
