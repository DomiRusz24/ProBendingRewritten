package com.probending.probending.core.arena;

import com.probending.probending.ProBending;
import com.probending.probending.core.players.SpectatorPlayer;
import me.domirusz24.plugincore.core.region.CustomRegion;
import org.bukkit.entity.Player;

public class SpectateGetterRegion extends CustomRegion {

    private final Arena arena;

    public SpectateGetterRegion(String ID, Arena arena) {
        super(ProBending.plugin, ID);
        this.arena = arena;
    }

    @Override
    protected void onPlayerEnter(Player player) {
        if (!arena.inGame()) return;
        if (ProBending.playerM.getActivePlayer(player) == null && ProBending.playerM.getMenuPlayer(player) == null && ProBending.playerM.getSpectator(player) == null) {
            new SpectatorPlayer(arena.getActiveArena(), player);
        }
    }

    @Override
    protected void onPlayerLeave(Player player) {
        SpectatorPlayer spectatorPlayer = ProBending.playerM.getSpectator(player);
        if (spectatorPlayer != null) spectatorPlayer.unregister();
    }

    @Override
    protected void onPlayerForceLeave(Player player) {
        SpectatorPlayer spectatorPlayer = ProBending.playerM.getSpectator(player);
        if (spectatorPlayer != null) spectatorPlayer.unregister();
    }

    @Override
    protected void _onLeave(Player player) {
        onPlayerLeave(player);
    }
}
