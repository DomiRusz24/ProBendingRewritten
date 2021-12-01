package com.probending.probending.core.arena.states;

import com.probending.probending.ProBending;
import com.probending.probending.api.events.PBPlayerDamagePBPlayerEvent;
import com.probending.probending.api.events.PBPlayerKnockOutEvent;
import com.probending.probending.api.events.PBPlayerUpdateStageEvent;
import com.probending.probending.core.players.ActivePlayer;
import com.probending.probending.core.arena.ActiveArena;
import com.probending.probending.core.enums.ArenaState;
import com.probending.probending.core.enums.TeamTag;
import com.probending.probending.util.UtilMethods;
import com.projectkorra.projectkorra.BendingPlayer;
import me.domirusz24.plugincore.PluginCore;
import me.domirusz24.plugincore.config.annotations.Language;
import me.domirusz24.plugincore.core.gui.CustomGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class StartingState extends AbstractArenaHandler {
    public StartingState(ActiveArena arena) {
        super(arena);
    }

    @Override
    public void onStart() {
        getArena().teleportToStartingLocations();
        for (ActivePlayer player : getArena().getPlayers(false)) {
            player.getPlayer().setHealth(20);
            if (!UtilMethods.hasScoreboard(player.getPlayer())) {
                new BoardChooseGUI(player).addPlayer(player.getPlayer());
            } else {
                if (ProBending.projectKorraM.hasBoardEnabled(player.getPlayer())) {
                    new BoardChooseGUI(player).addPlayer(player.getPlayer());
                }
            }
        }
    }

    @Override
    public void onPlayerKnockOut(PBPlayerKnockOutEvent event) {
        getArena().getCancelHandler().onPlayerKnockOut(event);
    }

    @Override
    public void onPBPlayerDamagePBPlayer(PBPlayerDamagePBPlayerEvent event) {
        getArena().getCancelHandler().onPBPlayerDamagePBPlayer(event);
    }

    @Override
    public void onPlayerUpdateStage(PBPlayerUpdateStageEvent event) {
    }

    private int sec = 0;

    private int seconds = 10;

    @Override
    public void onUpdate() {
        sec++;
        if (seconds > 0) {
            seconds--;
        }
        if (seconds == 0) {
            getArena().teleportToStartingLocations();
            getArena().setState(ArenaState.IN_ROUND);
            getArena().setHandler(new InRoundState(getArena()));
        } else {
            if (seconds < 4) {
                if (seconds == 3) {
                    for (TeamTag team : TeamTag.values()) {
                        getArena().teleportToStartingLocations();
                        for (ActivePlayer p : getArena().getTeam(team).getPlayers(false)) {
                            UtilMethods.freezePlayer(p.getPlayer(), true);
                            if (ProBending.guiM.get(p.getPlayer()).getCurrent() != null) {
                                ProBending.guiM.get(p.getPlayer()).getCurrent().leave(p.getPlayer());
                            }
                        }
                    }
                }
                getArena().sendTitle(UtilMethods.getNumberPrefix(seconds) + seconds, "", 5, 10, 5, true);
            } else {
                getArena().sendTitle("", UtilMethods.getNumberPrefix(seconds) + seconds, 5, 10, 5, true);
            }
        }
    }

    @Language("BoardChoose.GUI.Name")
    public static String LANG_NAME = "&d&lBending board &r&7| &3&lProBending board";

    @Language("BoardChoose.GUI.Item.Selected")
    public static String LANG_SELECTED = "&a[SELECTED] ";

    @Language("BoardChoose.GUI.Item.PKBoard.Display")
    public static String LANG_PK_NAME = "&d&lBending board";

    @Language("BoardChoose.GUI.Item.PKBoard.Description")
    public static String LANG_PK_DESCRIPTION = "&7If you select this, your normal bending board will||&7be shown during the game.";

    @Language("BoardChoose.GUI.Item.PBBoard.Display")
    public static String LANG_PB_NAME = "&3&lProBending board";

    @Language("BoardChoose.GUI.Item.PBBoard.Description")
    public static String LANG_PB_DESCRIPTION = "&7If you select this, the ProBending board will||&7be shown during the game.||&7&l(SUGGESTED)";

    @Language("BoardChoose.GUI.Command.ToggleBoard")
    public static String LANG_PB_TOGGLE_BOARD = "bending board";

    @Language("BoardChoose.GUI.Command.BoardTitle")
    public static String LANG_PB_BOARD_TITLE = "&lAbilities";

    public static class BoardChooseGUI extends CustomGUI {

        private final ActivePlayer player;

        public BoardChooseGUI(ActivePlayer player) {
            super(ProBending.plugin, LANG_NAME, 9);
            this.player = player;
            clearItems();
        }

        @Override
        protected void onUpdate() {

        }

        @Override
        protected void onClear() {
            if (ProBending.projectKorraM.hasBoardEnabled(player.getPlayer())) {
                player.toggleBoard(false);
                getItem(2).setMaterial(Material.PURPLE_STAINED_GLASS_PANE).setName(LANG_SELECTED + LANG_PK_NAME).setDescription(UtilMethods.stringToList(LANG_PK_DESCRIPTION)).setGlow(true)
                        .setLeftClick((p) -> {
                            leave(player.getPlayer());
                        });
                getItem(6).setMaterial(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName(LANG_PB_NAME).setDescription(UtilMethods.stringToList(LANG_PB_DESCRIPTION))
                        .setLeftClick((p) -> {
                            player.toggleBoard(true);
                            player.getPlayer().performCommand(LANG_PB_TOGGLE_BOARD);
                            leave(player.getPlayer());
                        });
            } else {
                player.toggleBoard(true);
                getItem(2).setMaterial(Material.PURPLE_STAINED_GLASS_PANE).setName(LANG_PK_NAME).setDescription(UtilMethods.stringToList(LANG_PK_DESCRIPTION))
                        .setLeftClick((p) -> {
                            player.toggleBoard(false);
                            player.getPlayer().performCommand(LANG_PB_TOGGLE_BOARD);
                            leave(player.getPlayer());

                        });
                getItem(6).setMaterial(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName(LANG_SELECTED + LANG_PB_NAME).setDescription(UtilMethods.stringToList(LANG_PB_DESCRIPTION)).setGlow(true)
                        .setLeftClick((p) -> {
                            leave(player.getPlayer());
                        });
            }
            //2, 6
        }

        @Override
        protected void firstOpen(Player player) {

        }

        @Override
        protected void pageOpen(Player player) {

        }

        @Override
        protected void close(Player player) {

        }

        @Override
        protected void changeGUI(Player player) {

        }

        @Override
        protected ItemStack emptySlot() {
            return UtilMethods.createItem(Material.GRAY_STAINED_GLASS_PANE, (byte) 0, "", false, "");
        }

        public ActivePlayer getPlayer() {
            return player;
        }

        @Override
        public PluginCore getCorePlugin() {
            return ProBending.plugin;
        }
    }
}
