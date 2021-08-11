package com.probending.probending.core.gui;

import com.probending.probending.ProBending;
import com.probending.probending.command.pbteam.pbteam.TeamPlayCommand;
import com.probending.probending.core.team.PBTeam;
import com.probending.probending.util.UtilMethods;
import me.domirusz24.plugincore.config.annotations.Language;
import me.domirusz24.plugincore.core.gui.CustomGUI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public class TeamPlayGUI extends CustomGUI {

    @Language("GUIS.TeamPlay.Title")
    public static String LANG_TITLE = "&cTeam play";

    @Language("GUIS.TeamPlay.Notify")
    public static String LANG_NOTIFY = "&cYour team wants to play ProBending! Type in \"/team play\" to join!";

    private final PBTeam team;


    public TeamPlayGUI(PBTeam team) {
        super(LANG_TITLE, 9);
        this.team = team;
        clearItems();
    }

    private boolean isReady = false;

    @Override
    public void refresh() {
        super.refresh();
        if (isReady) {
            List<Player> players = team.getBukkitPlayers().stream()
                    .filter((player) ->
                            ProBending.playerM.getActivePlayer(player) == null
                                    && ProBending.teamM.getTempTeam(player) == null)
                    .collect(Collectors.toList());
            update = false;
            for (Player player : players) {
                this.leave(player);
            }
            update = true;
            if (!team.throwIntoGame(players)) {
                players.forEach((p) -> p.sendMessage(TeamPlayLanguages.NO_ARENAS));
            }
        }
    }

    @Override
    public void onUpdate() {
        List<Player> players = team.getBukkitPlayers().stream()
                .filter((player) ->
                        ProBending.playerM.getActivePlayer(player) == null
                        && ProBending.teamM.getTempTeam(player) == null)
                .collect(Collectors.toList());

        if (players.size() == 0) {
            isReady = false;
            return;
        }

        boolean ready = true;

        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            if (getViewers().contains(player)) {
                getItem((i + 1) * 2).setMaterial(Material.GREEN_STAINED_GLASS_PANE).setName(ChatColor.GREEN + player.getName()).setGlow(true);
            } else {
                player.sendMessage(LANG_NOTIFY);
                getItem((i + 1) * 2).setMaterial(Material.RED_STAINED_GLASS_PANE).setName(ChatColor.RED + player.getName()).setGlow(false);
                ready = false;
            }
        }

        isReady = ready;
    }

    @Override
    protected void onClear() {
        for (int i = 0; i < 3; i++) {
            getItem((i + 1) * 2).makeEmpty().setMaterial(Material.GRAY_STAINED_GLASS_PANE);
        }
    }

    public boolean isReady() {
        return isReady;
    }

    @Override
    protected void firstOpen(Player player) {
        refresh();
    }

    @Override
    protected void pageOpen(Player player) {
        firstOpen(player);
    }

    private boolean update = true;

    @Override
    protected void close(Player player) {
        if (update) refresh();
    }

    @Override
    protected void changeGUI(Player player) {
        close(player);
    }

    @Override
    protected ItemStack emptySlot() {
        return UtilMethods.createItem(Material.GRAY_STAINED_GLASS_PANE, (byte) 0, "",  false, "");
    }
}
