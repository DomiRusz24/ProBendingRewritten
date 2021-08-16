package com.probending.probending.core.players;
import me.domirusz24.plugincore.core.players.AbstractPlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class PBPlayerWrapper extends AbstractPlayer {

    private final static HashMap<UUID, PBPlayerWrapper> WRAPPER_BY_PLAYER = new HashMap<>();
    protected PBPlayerWrapper(Player player) {
        super(player);
        WRAPPER_BY_PLAYER.put(player.getUniqueId(), this);
    }

    @Override
    protected void onUnregister() {
        WRAPPER_BY_PLAYER.remove(getPlayer().getUniqueId());
    }

    @Override
    public boolean resetInventory() {
        return false;
    }

    @Override
    public int hashCode() {
        return player.getUniqueId().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof PBPlayerWrapper) {
            return ((PBPlayerWrapper) obj).getPlayer().getUniqueId().equals(getPlayer().getUniqueId());
        }
        return false;
    }

    public static PBPlayerWrapper of(Player player) {
        return WRAPPER_BY_PLAYER.getOrDefault(player.getUniqueId(), new PBPlayerWrapper(player));
    }

    public static void unregister(Player player) {
        WRAPPER_BY_PLAYER.remove(player.getUniqueId());
    }

}
