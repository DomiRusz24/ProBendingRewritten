package com.probending.probending.core.displayable;

import com.probending.probending.ProBending;
import com.probending.probending.core.interfaces.PlaceholderObject;
import com.probending.probending.managers.PAPIManager;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.Hash;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.*;
import java.util.function.Supplier;

public class PBScoreboard extends PlayerDisplayable {

    private String title;

    private final String name;

    private Objective obj;

    private final Scoreboard scoreboard;

    public PBScoreboard(String name, String title) {
        this.scoreboard = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
        this.name = name;
        this.title = title;
        obj = scoreboard.registerNewObjective(this.name, "dummy");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        obj.setDisplayName(this.title);
    }

    public PBScoreboard(String name, String title, PlaceholderObject... objects) {
        super(objects);
        this.scoreboard = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
        this.name = name.substring(0, Math.min(name.length(), 16));
        this.title = title;
        obj = scoreboard.registerNewObjective(this.name, "dummy");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        obj.setDisplayName(this.title);
    }

    @Override
    protected void onUpdate(ArrayList<String> values) {
        /*
        for (Player player : getPlayers()) {
            player.setScoreboard(Bukkit.getServer().getScoreboardManager().getNewScoreboard());
        }
         */
        obj.unregister();
        obj = scoreboard.registerNewObjective(name, "dummy");
        String title = this.title;
        for (PlaceholderObject placeholder : getPlaceholders()) {
            title = PAPIManager.setPlaceholders(placeholder, title);
        }
        obj.setDisplayName(title);
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        for (int i = 0; i < values.size(); i++) {
            obj.getScore(values.get(i).substring(0, Math.min(32, values.get(i).length()))).setScore(i * -1);
        }
        for (Player player : getPlayers()) {
            player.setScoreboard(scoreboard);
        }
    }

    @Override
    protected boolean onPlayerAdd(Player player) {
        player.setScoreboard(scoreboard);
        return true;
    }

    @Override
    protected boolean onPlayerRemove(Player player) {
        Bukkit.getScheduler().runTaskLater(ProBending.plugin, () -> player.setScoreboard(Bukkit.getServer().getScoreboardManager().getNewScoreboard()), 1);
        return true;
    }
}
