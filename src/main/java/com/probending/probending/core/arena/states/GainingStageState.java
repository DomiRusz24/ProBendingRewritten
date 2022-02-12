package com.probending.probending.core.arena.states;

import com.probending.probending.PBListener;
import com.probending.probending.api.enums.KnockOutCause;
import com.probending.probending.api.events.PBPlayerDamagePBPlayerEvent;
import com.probending.probending.api.events.PBPlayerGainStageEvent;
import com.probending.probending.api.events.PBPlayerKnockOutEvent;
import com.probending.probending.api.events.PBPlayerUpdateStageEvent;
import com.probending.probending.config.CommandConfig;
import com.probending.probending.core.enums.Ring;
import com.probending.probending.core.players.ActivePlayer;
import com.probending.probending.core.players.PBPlayer;
import me.domirusz24.plugincore.config.annotations.Language;
import com.probending.probending.core.arena.ActiveArena;
import com.probending.probending.core.enums.ArenaState;
import com.probending.probending.core.enums.TeamTag;
import com.probending.probending.util.UtilMethods;
import org.bukkit.Bukkit;

import java.util.HashMap;

public class GainingStageState extends AbstractArenaHandler {

    @Language("ActiveArena.StageGainStart.Resume")
    public static String LANG_RESUME = "&4Game will resume in 3 seconds!";

    @Language("ActiveArena.StageGainStart.WinningTeam.Start.Main")
    public static String LANG_WIN_START = "&4You have gained a stage!";

    @Language("ActiveArena.StageGainStart.WinningTeam.Start.Desc")
    public static String LANG_WIN_START_DESC = "&4You are immune from damage, go forward!";

    @Language("ActiveArena.StageGainStart.LosingTeam.Start")
    public static String LANG_LOSE_START = "&4You have lost a stage!";

    @Language("ActiveArena.StageGainStart.StageBackward")
    public static String LANG_STAGE_BACKWARD = "&4Game has been halted cause a team is gaining a stage!";

    private final TeamTag team;
    private final int gap;
    private Ring furthest;
    private int tickTime;
    private final HashMap<ActivePlayer, Integer> needToGainStage = new HashMap<>();

    public GainingStageState(ActiveArena arena, TeamTag tag, int tickTime, int gap, Ring furthest) {
        super(arena);
        this.team = tag;
        this.gap = gap;
        this.tickTime = tickTime;
        this.furthest = furthest;
        this.sec = -((gap - 1) * 3);
    }

    @Override
    public int getTimeLeft() {
        return (getArena().getArena().getArenaConfig().getRoundTime() * 20) - tickTime;
    }

    @Override
    public int getTime() {
        return tickTime;
    }

    @Override
    public void onStart() {
        for (ActivePlayer p : getArena().getTeam(team).getPlayers(false)) {
            CommandConfig.Commands.RoundPlayerGainStage.run(getArena().getArena(), p.getPlayer());
        }
        raiseGap(gap);
        getArena().getTeam(team).sendTitle((LANG_WIN_START), (LANG_WIN_START_DESC), 5, 30, 5, false);
        getArena().getTeam(team.getOther()).sendTitle((LANG_LOSE_START), "", 5, 30, 5, false);
    }

    private void raiseGap(int gap) {
        for (ActivePlayer p : getArena().getTeam(team).getPlayers(false)) {
            int amount = needToGainStage.getOrDefault(p, 0);
            Ring newRing = p.getRing().getFromOffset(team, gap + amount);
            if (newRing.offset(team, furthest) < 1) {
                needToGainStage.put(p, p.getRing().offset(team, furthest) - 1);
            } else {
                needToGainStage.put(p, amount + gap);
            }
        }
    }

    @Override
    public void onPlayerKnockOut(PBPlayerKnockOutEvent event) {
        if (event.getCause().equals(KnockOutCause.LEFT)) {
            if (event.getPlayer().getTeam().getTag() == team) {
                needToGainStage.remove(event.getPlayer());
            } else {
                int gap = event.getPlayer().getTeam().getGap();
                if (gap != 0) {
                    furthest = getArena().getTeam(team.getOther()).getFurthestRing();
                    raiseGap(getArena().getTeam(team).getGap());
                    sec = 0;
                    countDown = 2;
                }
            }
        }
        getArena().getCancelHandler().onPlayerKnockOut(event);
    }

    @Override
    public void onPBPlayerDamagePBPlayer(PBPlayerDamagePBPlayerEvent event) {
        getArena().getCancelHandler().onPBPlayerDamagePBPlayer(event);
    }

    @Override
    public void onPlayerUpdateStage(PBPlayerUpdateStageEvent event) {
        if (event.getRingOffset() > 0) {
            if (needToGainStage.containsKey(event.getPlayer())) {
                int stagesLeft = needToGainStage.get(event.getPlayer());
                if (stagesLeft > 0) {
                    advancePlayer(event.getPlayer(), true);
                    needToGainStage.put(event.getPlayer(), stagesLeft - 1);
                } else {
                    denyMoving(event.getPlayer());
                }
            } else {
                denyMoving(event.getPlayer());
            }

        } else if (event.getRingOffset() < 0) {
            event.getPlayer().getPlayer().sendTitle((LANG_STAGE_BACKWARD), "", 10, 40, 10);
            if (needToGainStage.containsKey(event.getPlayer())) {
                int stagesLeft = needToGainStage.get(event.getPlayer());
                for (int i = 0; i < stagesLeft; i++) {
                    advancePlayer(event.getPlayer(), false);
                }
                needToGainStage.put(event.getPlayer(), 0);
            }
            denyMoving(event.getPlayer());
        }
    }

    private void advancePlayer(ActivePlayer player, boolean drag) {
        PBPlayerGainStageEvent gainEvent = new PBPlayerGainStageEvent(getArena(), getArena().getState(), player, player.getRing().getFromOffset(team, 1), 1);
        Bukkit.getPluginManager().callEvent(gainEvent);
        if (!gainEvent.isCancelled()) {
            player.setRing(player.getRing().getFromOffset(team, 1));
            if (drag) player.dragTo(player.getRing());
        }
    }

    private void denyMoving(ActivePlayer player) {
        player.teleportTo(player.getRing());
        UtilMethods.freezePlayer(player.getPlayer(), true);
    }

    @Override
    public void onGap(TeamTag tag, int gap, Ring ring) {}

    private int sec = 0;

    private int countDown = 2;

    private int timeTillStart = 2;

    @Override
    public void onUpdate() {
        sec++;
        if (sec > 6 || needToGainStage.keySet().stream().filter((p) -> needToGainStage.get(p) > 0).toArray().length == 0) {
            if (sec <= 7) {
                needToGainStage.keySet().stream()
                        .filter((p) -> needToGainStage.get(p) > 0)
                        .forEach((p) -> {
                            PBPlayerGainStageEvent gainEvent = new PBPlayerGainStageEvent(getArena(), getArena().getState(), p, p.getRing().getFromOffset(team, needToGainStage.get(p)), needToGainStage.get(p));
                            Bukkit.getPluginManager().callEvent(gainEvent);
                            if (!gainEvent.isCancelled()) {
                                p.setRing(p.getRing().getFromOffset(team, needToGainStage.get(p)));
                                p.teleportTo(p.getRing());
                            }
                        });
                sec = 8;
                getArena().sendTitle((LANG_RESUME), "", 5, 10, 5, true);
            } else if (sec > 8) {
                if (countDown == 0) {
                    getArena().setState(ArenaState.IN_ROUND);
                    getArena().setHandler(new InRoundState(getArena(), tickTime));
                } else {
                    getArena().sendTitle(UtilMethods.getNumberPrefix(countDown) + countDown, "", 0, 20, 0, true);
                }
                countDown--;
            }
        } else if (timeTillStart == 0) {
            needToGainStage.keySet().stream()
                    .filter((p) -> needToGainStage.get(p) > 0)
                    .forEach((p) -> p.getPlayer().sendTitle("", (LANG_WIN_START_DESC), 5, 10, 5));
        } else {
            timeTillStart--;
        }
    }
}
