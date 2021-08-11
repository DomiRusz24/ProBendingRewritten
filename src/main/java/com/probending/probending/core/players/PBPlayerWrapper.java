package com.probending.probending.core.players;
import me.domirusz24.plugincore.core.players.AbstractPlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class PBPlayerWrapper extends AbstractPlayer {

    private final static HashMap<Player, PBPlayerWrapper> WRAPPER_BY_PLAYER = new HashMap<>();
    protected PBPlayerWrapper(Player player) {
        super(player);
        WRAPPER_BY_PLAYER.put(player, this);
    }

    @Override
    protected void onUnregister() {
        WRAPPER_BY_PLAYER.remove(getPlayer());
    }

    @Override
    public boolean resetInventory() {
        return false;
    }

    public static PBPlayerWrapper of(Player player) {
        return WRAPPER_BY_PLAYER.getOrDefault(player, new PBPlayerWrapper(player));
    }

    public static void unregister(Player player) {
        WRAPPER_BY_PLAYER.remove(player);
    }

}
