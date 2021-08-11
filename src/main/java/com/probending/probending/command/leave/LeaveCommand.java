package com.probending.probending.command.leave;

import com.probending.probending.ProBending;

import me.domirusz24.plugincore.command.abstractclasses.BaseCommand;
import me.domirusz24.plugincore.config.annotations.Language;
import com.probending.probending.core.enums.TeamTag;
import com.probending.probending.core.players.ActivePlayer;
import com.probending.probending.core.players.MenuPlayer;
import com.probending.probending.core.players.SpectatorPlayer;
import com.probending.probending.core.team.ArenaTempTeam;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

import java.util.Arrays;
import java.util.List;

public class LeaveCommand extends BaseCommand {

    @Language("Command.Leave.Description")
    public static String LANG_DESCRIPTION = "Allows you to leave the arena.";


    @Override
    public void selfExecute(CommandSender sender) {
        if (isPlayer(sender) && hasPermission(sender)) {
            Player player = (Player) sender;
            ActivePlayer activePlayer = ProBending.playerM.getActivePlayer(player);
            if (activePlayer != null) activePlayer.setState(ActivePlayer.State.LEFT);
            ArenaTempTeam team = ProBending.teamM.getTempTeam(player);
            if (team != null) {
                team.removePlayer(player);
                for (TeamTag tag : TeamTag.values()) {
                    if (team.getArena().getPreArena().getRegion(tag).getPlayers().contains(player)) {
                        team.getArena().getPreArena().getRegion(tag).forceRemove(player);
                    }
                }
            }
            SpectatorPlayer specPlayer = ProBending.playerM.getSpectator(player);
            if (specPlayer != null) specPlayer.unregister();
        }
    }

    @Override
    protected String name() {
        return "leave";
    }

    @Override
    protected String usage() {
        return "/leave ";
    }

    @Override
    protected String description() {
        return LANG_DESCRIPTION;
    }

    @Override
    protected List<String> aliases() {
        return Arrays.asList("quit");
    }

    @Override
    public PermissionDefault getPermissionDefault() {
        return PermissionDefault.TRUE;
    }
}
