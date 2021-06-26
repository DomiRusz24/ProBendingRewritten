package com.probending.probending.managers.schematics;

import com.probending.probending.ProBending;
import com.probending.probending.command.abstractclasses.Command;
import com.probending.probending.util.Pair;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.*;

public class EasyRollBackManager extends SchematicManager {

    private final WorldEditPlugin plugin;

    public EasyRollBackManager(ProBending plugin, WorldEditPlugin worldEdit) {
        super(plugin);
        this.plugin = worldEdit;
    }

    @Override
    public Pair<Location, Location> getPlayerSelection(Player player) {
        try {
            Region region = plugin.getSession(player).getSelection(plugin.getSession(player).getSelectionWorld());
            BlockVector3 min = region.getMinimumPoint();
            BlockVector3 max = region.getMaximumPoint();
            return new Pair<>(new Location(BukkitAdapter.adapt(region.getWorld()), min.getBlockX(), min.getBlockY(), min.getBlockZ()), new Location(BukkitAdapter.adapt(region.getWorld()), max.getBlockX(), max.getBlockY(), max.getBlockZ()));
        } catch (IncompleteRegionException e) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void saveSchematic(CommandSender sender, Location min, Location max, File folder, String name) {
        //(new Copy(min.getBlockX(), min.getBlockY(), min.getBlockZ(), max.getBlockX(), max.getBlockY(), max.getBlockZ(), min.getWorld(), new File(folder, name + ".dat").toString(), sender)).run();
    }

    @Override
    public boolean getSchematic(CommandSender sender, Location location, File folder, String name) {
        //(new Paste(location, new File(folder, name + ".dat").toString(), sender, true, false, Command.LANG_SUCCESS_PREFIX + " ")).run();
        return true;
    }


}
