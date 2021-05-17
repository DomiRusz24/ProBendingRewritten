package com.probending.probending.core.players;

import com.probending.probending.core.displayable.CustomScoreboard;
import com.probending.probending.core.interfaces.PlaceholderPlayer;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Scoreboard;

public abstract class AbstractPlayer implements PlaceholderPlayer {

    protected final Player player;

    protected final BossBar bossBar;

    protected final CustomScoreboard scoreboard;

    private final ItemStack[] inventory;

    public AbstractPlayer(Player player) {
        this.player = player;
        bossBar = bossBar();
        scoreboard = scoreboard();
        if (bossBar != null) bossBar.addPlayer(player);
        if (scoreboard != null) scoreboard.addPlayer(player);
        if (resetInventory()) {
            inventory = player.getInventory().getContents();
            player.getInventory().clear();
        } else {
            inventory = null;
        }
    }

    public void unregister() {
        if (bossBar != null) bossBar.removeAll();
        if (scoreboard != null) scoreboard.removePlayer(player);
        if (resetInventory()) player.getInventory().setContents(inventory);
        onUnregister();
    }

    protected abstract void onUnregister();

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public int hashCode() {
        return player.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj instanceof AbstractPlayer) {
            return player.equals(((AbstractPlayer) obj).getPlayer());
        }
        return player.equals(obj);
    }

    public abstract boolean resetInventory();

    protected BossBar bossBar() {
        return null;
    }

    protected CustomScoreboard scoreboard() {
        return null;
    }

    public BossBar getBossBar() {
        return bossBar;
    }

    public CustomScoreboard getScoreBoard() {
        return scoreboard;
    }
}
