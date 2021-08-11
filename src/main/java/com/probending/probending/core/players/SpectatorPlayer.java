package com.probending.probending.core.players;

import com.probending.probending.ProBending;
import me.domirusz24.plugincore.config.annotations.Language;
import com.probending.probending.core.arena.ActiveArena;
import com.probending.probending.util.UtilMethods;
import me.domirusz24.plugincore.core.displayable.CustomScoreboard;
import me.domirusz24.plugincore.core.players.AbstractPlayer;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class SpectatorPlayer extends AbstractPlayer {

    private final GameMode startingGameMode;

    private final Location startingLocation;

    private final boolean isFlying;

    private final ActiveArena arena;

    public SpectatorPlayer(ActiveArena arena, Player player, Location startingLocation, GameMode startingGameMode) {
        super(player);
        if (ProBending.playerM.getSpectator(player) != null) ProBending.playerM.removeSpectator(ProBending.playerM.getSpectator(player));
        this.startingLocation = startingLocation;
        this.startingGameMode = startingGameMode;
        this.isFlying = player.getAllowFlight();
        player.setGameMode(GameMode.SPECTATOR);
        this.arena = arena;
        arena.addSpectator(this);
        player.teleport(arena.getArena().getCenter());
        ProBending.playerM.addSpectator(this);
    }

    public SpectatorPlayer(ActiveArena arena, Player player) {
        super(player);
        if (ProBending.playerM.getSpectator(player) != null) ProBending.playerM.removeSpectator(ProBending.playerM.getSpectator(player));
        this.startingLocation = null;
        this.startingGameMode = null;
        this.isFlying = false;
        this.arena = arena;
        arena.addSpectator(this);
        ProBending.playerM.addSpectator(this);
    }

    @Override
    protected void onUnregister() {
        if (startingGameMode != null) {
            player.teleport(startingLocation);
            player.setGameMode(startingGameMode);
            player.setAllowFlight(isFlying);
        }
        arena.removeSpectator(this);
        ProBending.playerM.removeSpectator(this);
    }

    @Override
    public boolean resetInventory() {
        return false;
    }

    public ActiveArena getArena() {
        return arena;
    }

    @Language("Spectator.Scoreboard")
    public static String LANG_SCOREBOARD = "%arena_name%||--------------- ||Round: %arena_round%||Time left: %arena_r_time% min||---------------";

    @Override
    protected CustomScoreboard scoreboard() {
        String[] scoreboard = UtilMethods.stringToList(LANG_SCOREBOARD);
        CustomScoreboard board = new CustomScoreboard("spec_" + getPlayer().getName(), scoreboard[0], this, getArena());
        for (String s : Arrays.asList(scoreboard).subList(1, scoreboard.length)) {
            board.addValue(s);
        }
        return board;
    }
}
