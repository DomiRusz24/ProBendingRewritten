package com.probending.probending.core.players;

import com.probending.probending.ProBending;
import com.probending.probending.config.arena.ArenaConfig;
import com.probending.probending.managers.CustomItemManager;
import me.domirusz24.plugincore.config.annotations.Language;
import com.probending.probending.core.arena.prearena.ArenaGetterRegion;
import com.probending.probending.core.enums.TeamTag;
import com.probending.probending.util.UtilMethods;
import me.domirusz24.plugincore.core.displayable.CustomScoreboard;
import me.domirusz24.plugincore.core.players.AbstractPlayer;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class MenuPlayer extends AbstractPlayer {

    private final TeamTag tag;

    private final ArenaGetterRegion region;

    private boolean voteSkip = false;

    public MenuPlayer(Player player, ArenaGetterRegion region, TeamTag tag) {
        super(ProBending.plugin, player);
        this.tag = tag;
        this.region = region;
        player.getInventory().setArmorContents(ProBending.teamM.getArmor(tag,false, false, false, true));
        CustomItemManager.LEAVE_COMMAND().giveToPlayer(player, 1, 8);
        scoreboard.addPlaceholder(region.getArena());
        scoreboard.addPlaceholder(region.getTeam());
        ProBending.playerM.addMenuPlayer(this);
    }

    public void setVoteSkip(boolean voteSkip) {
        this.voteSkip = voteSkip;
        if (voteSkip) {
            region.getArena().onVoteStart(this);
        }
    }

    public boolean getVoteSkip() {
        return voteSkip;
    }

    public TeamTag getTag() {
        return tag;
    }

    @Language("PreGamePlayer.Scoreboard")
    public static String LANG_SCOREBOARD = "%team_name%||--------------- ||%player_name%|| ||Use /fs to force the game!||---------------";

    @Override
    protected CustomScoreboard scoreboard() {
        String[] scoreboard = UtilMethods.stringToList(LANG_SCOREBOARD);
        CustomScoreboard board = new CustomScoreboard(ProBending.plugin, "pA_" + getPlayer().getName(), scoreboard[0], this);
        for (String s : Arrays.asList(scoreboard).subList(1, scoreboard.length)) {
            board.addValue(s);
        }
        return board;
    }

    @Override
    protected boolean overrideScoreBoard() {
        return false;
    }

    @Override
    protected void onUnregister() {
        ProBending.playerM.removeMenuPlayer(this);
    }

    @Override
    public boolean resetInventory() {
        return true;
    }
}
