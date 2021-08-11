package com.probending.probending.command.arena.arenacontrol;

import com.probending.probending.ProBending;
import com.probending.probending.command.abstractclasses.ArenaSubCommand;
import me.domirusz24.plugincore.config.annotations.Language;
import com.probending.probending.core.arena.Arena;
import com.probending.probending.core.arena.prearena.ArenaGetterRegion;
import com.probending.probending.core.arena.prearena.PreArena;
import com.probending.probending.core.enums.Ring;
import com.probending.probending.core.enums.TeamTag;
import com.probending.probending.core.team.Team;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class ArenaRefreshCommand extends ArenaSubCommand {

    @Language("Command.ArenaControl.Refresh.Description")
    public static String LANG_DESCRIPTION = "Allows you refresh an Arena in case it bugs out.";


    @Override
    public void execute(CommandSender sender, Arena arena, List<String> args) {
        if (ProBending.configM.getLocationsConfig().getSpawn() == null) {
            sender.sendMessage(ChatColor.RED + "Spawn... Not set!");
        } else {
            sender.sendMessage(ChatColor.GREEN + "Spawn... OK!");
        }
        if (arena.getRollbackLocation() == null) {
            sender.sendMessage(ChatColor.RED + "RollBack... Not set!");
        } else {
            sender.sendMessage(ChatColor.GREEN + "RollBack... OK!");
        }
        if (arena.getCenter() == null) {
            sender.sendMessage(ChatColor.RED + "Center... Not set!");
        } else {
            sender.sendMessage(ChatColor.GREEN + "Center... OK!");
        }
        if (arena.getJoinSign().getLocation() == null) {
            sender.sendMessage(ChatColor.RED + "Sign... Not set!");
        } else {
            sender.sendMessage(ChatColor.GREEN + "Sign... OK!");
        }

        for (Ring ring : Ring.values()) {
            if (ring.isTeleportRing()) {
                if (arena.getRingLocation(ring) == null) {
                    sender.sendMessage(ChatColor.RED + "Ring " + ring.name() + "... Not set!");
                }
            }
        }

        for (TeamTag tag : TeamTag.values()) {
            for (int i = 0; i < 3; i++) {
                if (arena.getStartingLocations(tag, i) == null) {
                    sender.sendMessage(ChatColor.RED + "Starting " + i +  " location for " + tag.toString() + "... Not set!");
                }
            }
        }

        PreArena preArena = arena.getPreArena();

        for (TeamTag tag : TeamTag.values()) {
            ArenaGetterRegion region = preArena.getRegion(tag);
            if (region.getRegionCenter() == null) {
                sender.sendMessage(ChatColor.RED + "Getter region center for " + tag.toString() + "... Not set!");
            }
            if (region.getCenter() == null) {
                sender.sendMessage(ChatColor.RED + "Getter center for " + tag.toString() + "... Not set!");
            }
        }

        preArena.isReady();
        arena.isReadyToPlay();
    }

    @Override
    public List<String> autoComplete(CommandSender sender, Arena arena, List<String> args) {
        return new ArrayList<>();
    }

    @Override
    protected String name() {
        return "refresh";
    }

    @Override
    protected String usage() {
        return "refresh";
    }

    @Override
    protected String description() {
        return LANG_DESCRIPTION;
    }
}
