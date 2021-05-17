package com.probending.probending.managers.schematics;

import com.probending.probending.ProBending;
import com.probending.probending.command.abstractclasses.Command;
import com.probending.probending.managers.PBManager;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.bukkit.selections.Selection;
import javafx.util.Pair;
import net.shadowxcraft.rollbackcore.Copy;
import net.shadowxcraft.rollbackcore.Paste;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;

public abstract class SchematicManager extends PBManager {
    public SchematicManager(ProBending plugin) {
        super(plugin);
    }

    public Pair<Location, Location> getPlayerSelection(Player player) {
        Selection selection = ProBending.worldEdit.getSelection(player);
        if (selection instanceof CuboidSelection) {
            return new Pair<>(new Location(player.getWorld(), selection.getMinimumPoint().getX(), selection.getMinimumPoint().getY(), selection.getMinimumPoint().getZ()),
                    new Location(player.getWorld(), selection.getMaximumPoint().getX(), selection.getMaximumPoint().getY(), selection.getMaximumPoint().getZ()));
        } else {
            return null;
        }
    }

    public abstract void saveSchematic(CommandSender sender, Location min, Location max, File folder, String name);

    public abstract boolean getSchematic(CommandSender sender, Location location, File folder, String name);


}
