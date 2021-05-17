package com.probending.probending.core.players;

import com.probending.probending.ProBending;
import com.probending.probending.core.arena.ActiveArena;
import com.probending.probending.core.interfaces.PlaceholderPlayer;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

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
        ProBending.nmsM.setToSpectator(player);
        this.arena = arena;
        arena.addSpectator(this);
        ProBending.playerM.addSpectator(this);
    }

    @Override
    protected void onUnregister() {
        player.teleport(startingLocation);
        player.setGameMode(startingGameMode);
        player.setAllowFlight(isFlying);
        arena.removeSpectator(this);
        ProBending.playerM.removeSpectator(this);
    }

    @Override
    public boolean resetInventory() {
        return true;
    }

    public ActiveArena getArena() {
        return arena;
    }
}
