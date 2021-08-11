package com.probending.probending.core.players;

import com.probending.probending.ProBending;
import me.domirusz24.plugincore.config.annotations.Language;
import com.probending.probending.core.arena.ActiveArena;
import com.probending.probending.core.enums.Ring;
import com.probending.probending.core.enums.TeamTag;
import com.probending.probending.core.team.ActiveTeam;
import com.probending.probending.util.UtilMethods;
import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.Element;
import me.domirusz24.plugincore.core.displayable.CustomScoreboard;
import me.domirusz24.plugincore.core.players.AbstractPlayer;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.material.Wool;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.Arrays;

public class ActivePlayer extends AbstractPlayer {

    // Player used for in-game.

    // ---------

    private final BendingPlayer bPlayer;
    private final PBPlayer playerData;
    private final Element element;

    // ----------

    private State state;
    private int tiredness;
    private boolean hasBeenHit;

    private final GameMode startingGameMode;

    private final String prefix;

    // ----------

    public ActivePlayer(Player player, TeamTag teamTag, ActiveArena arena, Element element) {
        super(player);
        this.teamTag = teamTag;
        this.arena = arena;
        this.element = element;
        this.state = State.PLAYING;
        tiredness = 0;
        this.startingGameMode = player.getGameMode();
        this.ring = teamTag == TeamTag.BLUE ? Ring.BLUE_FIRST : Ring.RED_FIRST;
        bPlayer = BendingPlayer.getBendingPlayer(player);
        playerData = (PBPlayer) ProBending.playerDataM.getPlayer(player);
        hasBeenHit = false;
        if (element == null) {
            prefix = ChatColor.GRAY + "N";
        } else {
            String c = element.getColor().toString();
            prefix = c + ChatColor.BOLD + "[" + ChatColor.BOLD + c +  String.valueOf(element.getName().toUpperCase().charAt(0)) + ChatColor.BOLD + "]";
        }
        bossBar.setColor(teamTag.getBarColor());
        scoreboard.addPlaceholder(getArena().getArena());
        scoreboard.addPlaceholder(getArena().getTeam(teamTag));
        if (getArena().getArena().getArenaConfig().getDamage()) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 999999, 10));
            player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 999999, 10));
        }
        if (teamTag == TeamTag.BLUE) {
            player.getInventory().setArmorContents(ProBending.teamM.BLUE_ARMOR);
        } else {
            player.getInventory().setArmorContents(ProBending.teamM.RED_ARMOR);
        }
        ProBending.playerM.addActivePlayer(this);
    }

    @Override
    protected void onUnregister() {
        player.removePotionEffect(PotionEffectType.REGENERATION);
        player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
        player.teleport(ProBending.locationConfig.getSpawn());
        ProBending.nmsM.setGameMode(player, startingGameMode);
        ProBending.playerM.removeActivePlayer(this);
    }

    @Override
    public boolean resetInventory() {
        return true;
    }

    public void update() {
        getBossBar().setTitle(UtilMethods.getPercentPrefix(getTiredness()) + getTiredness() + "%");
        double percent = (double) getTiredness() / (double) getArena().getArena().getArenaConfig().getTirednessMax();
        getBossBar().setProgress(percent);
        getBossBar().setColor(UtilMethods.getBarColorFromPercent((int) ((1 - percent) * 100)));
        getScoreBoard().update();
    }

    // ----------

    public PBPlayer getPlayerData() {
        return playerData;
    }

    public BendingPlayer getBendingPlayer() {
        return bPlayer;
    }

    // ----------

    private final ActiveArena arena;
    private final TeamTag teamTag;

    public ActiveArena getArena() {
        return arena;
    }

    public TeamTag getTeamTag() {
        return teamTag;
    }

    public ActiveTeam getTeam() {
        return arena.getTeam(teamTag);
    }

    public String getElementPrefix() {
        return prefix;
    }

    // ----------

    private Ring ring;

    public void setRing(Ring ring) {
        this.ring = ring;
    }

    public Ring getRing() {
        return ring;
    }

    public Ring getCurrentRing() {
        Block block = player.getWorld().getBlockAt(player.getLocation().getBlockX(), ProBending.pluginConfig.getYLevel(), player.getLocation().getBlockZ());
        if (block.getState().getData() instanceof Wool) {
            Wool wool = (Wool) block.getState().getData();
            Ring ring = Ring.fromID(wool.getColor().getWoolData());
            if (ring != null) {
                return ring;
            } else {
                return Ring.OFF_WOOL;
            }
        }
        return Ring.OFF_WOOL;
    }

    public int ringOffset(Ring ring) {
        return ring.offset(teamTag, ring);
    }

    // ----------

    public void dragTo(Ring ring) {
        Location l = arena.getArena().getRingLocation(ring);
        l.clone().add(0, 1, 0);
        if (l.getWorld().equals(player.getLocation().getWorld())) {
            Vector vector = l.subtract(player.getPlayer().getLocation()).toVector().normalize();
            vector.multiply(getArena().getArena().getArenaConfig().getDragValue());
            try {
                getPlayer().setVelocity(vector);
            } catch (IllegalArgumentException e) {
                getPlayer().teleport(l);
            }
        }
    }

    public void teleportTo(Ring ring) {
        Location l = arena.getArena().getRingLocation(ring).clone();
        if (ring.getTeam() != getTeamTag()) {
            l.setDirection(l.getDirection().multiply(-1));
        }
        getPlayer().teleport(l);
    }

    // ---------- //

    private boolean spectating = false;

    public State getState() {
        return state;
    }

    public void setState(State state) {
        if (state != this.state) {
            this.state = state;
            if (state == State.SPECTATING) {
                ProBending.nmsM.setToSpectator(player);
                spectating = true;
            }
        }
    }

    public boolean hasBeenHit() {
        return hasBeenHit;
    }

    public void setHasBeenHit(boolean bool) {
        hasBeenHit = bool;
    }

    public boolean isSpectating() {
        return spectating;
    }

    // ---

    public GameMode getStartingGameMode() {
        return startingGameMode;
    }

    public Element getElement() {
        return element;
    }

    public int getTiredness() {
        return tiredness;
    }

    public void raiseTiredness(int raise) {
        tiredness = Math.min(tiredness + raise, getArena().getArena().getArenaConfig().getTirednessMax());
    }

    public void resetTiredness() {
        tiredness = 0;
        update();
    }

    // ---------- //

    @Language("ActivePlayer.Scoreboard")
    public static String LANG_SCOREBOARD = "%arena_name%||--------------- ||%team_color%%player_name%||Round: %arena_round%||Points: %team_color%%team_points%||Tiredness: %probending_tired%||Time left: %arena_r_time% min||---------------";


    @Override
    protected BossBar bossBar() {
        return Bukkit.getServer().createBossBar("In mid round!", BarColor.WHITE, BarStyle.SOLID);
    }

    @Override
    protected CustomScoreboard scoreboard() {
        String[] scoreboard = UtilMethods.stringToList(LANG_SCOREBOARD);
        CustomScoreboard board = new CustomScoreboard("pb_" + getPlayer().getName(), scoreboard[0], this);
        for (String s : Arrays.asList(scoreboard).subList(1, scoreboard.length)) {
            board.addValue(s);
        }
        return board;
    }

    // --------- //


    @Language("ActivePlayer.State.PLAYING")
    public static String LANG_PLAYING = "In game!";

    @Language("ActivePlayer.State.KNOCKED_OUT")
    public static String LANG_DEAD = "Knocked out!";

    @Language("ActivePlayer.State.SPECTATING")
    public static String LANG_SPECTATING = "Spectating!";


    public enum State {
        PLAYING,
        DEAD,
        LEFT,
        SPECTATING;

        public String toString() {
            switch (this) {
                case PLAYING:
                    return LANG_PLAYING;
                case DEAD:
                    return LANG_DEAD;
                default:
                    return LANG_SPECTATING;
            }
        }
    }
}
