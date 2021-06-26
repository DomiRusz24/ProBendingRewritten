package com.probending.probending.managers.schematics;

import com.probending.probending.ProBending;
import com.probending.probending.managers.PBManager;
import com.probending.probending.util.Pair;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;

public abstract class SchematicManager extends PBManager {
    public SchematicManager(ProBending plugin) {
        super(plugin);
    }

    public abstract Pair<Location, Location> getPlayerSelection(Player player);

    public abstract void saveSchematic(CommandSender sender, Location min, Location max, File folder, String name);

    public abstract boolean getSchematic(CommandSender sender, Location location, File folder, String name);


}
