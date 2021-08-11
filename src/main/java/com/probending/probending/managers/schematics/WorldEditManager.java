package com.probending.probending.managers.schematics;

import com.probending.probending.ProBending;
import com.probending.probending.command.abstractclasses.Command;
import com.probending.probending.util.Pair;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.BuiltInClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardWriter;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.Vector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.session.ClipboardHolder;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.*;

public class WorldEditManager extends SchematicManager {

    private final WorldEditPlugin plugin;


    public WorldEditManager(ProBending plugin, WorldEditPlugin worldEdit) {
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
        CuboidRegion region = new CuboidRegion(Vector3.toBlockPoint(min.getX(), min.getY(), min.getZ()), Vector3.toBlockPoint(max.getX(), max.getY(), max.getZ()));
        region.setWorld(BukkitAdapter.adapt(min.getWorld()));
        BlockArrayClipboard clipboard = new BlockArrayClipboard(region);

        try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(region.getWorld(), -1)) {
            ForwardExtentCopy forwardExtentCopy = new ForwardExtentCopy(
                    editSession, region, clipboard, region.getMinimumPoint()
            );
            try {
                Operations.complete(forwardExtentCopy);
            } catch (WorldEditException e) {
                e.printStackTrace();
            }
        }

        File file = new File(folder, name + ".schem");

        if (!file.getParentFile().exists()) {
            file.mkdirs();
        } else if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (ClipboardWriter writer = BuiltInClipboardFormat.SPONGE_SCHEMATIC.getWriter(new FileOutputStream(file))) {
            writer.write(clipboard);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean getSchematic(CommandSender sender, Location location, File folder, String name) {
        if (location == null) {
            if (sender != null) sender.sendMessage(Command.LANG_FAIL + " (Location null)");
            return false;
        }
        File file = new File(folder, name + ".schem");

        if (!file.exists()) {
            if (sender != null) sender.sendMessage(Command.LANG_FAIL + " (File doesn't exist)");
            return false;
        }
        ClipboardReader reader;
        try {
            reader = BuiltInClipboardFormat.SPONGE_SCHEMATIC.getReader(new FileInputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        Clipboard clipboard;
        try {
            clipboard = reader.read();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }


        try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(BukkitAdapter.adapt(location.getWorld()), -1)) {
            Operation operation = new ClipboardHolder(clipboard)
                    .createPaste(editSession)
                    .to(BlockVector3.at(location.getX(), location.getY(), location.getZ()))
                    .ignoreAirBlocks(false)
                    .build();
            try {
                Operations.complete(operation);
            } catch (WorldEditException e) {
                e.printStackTrace();
            }
        }
        if (sender != null) sender.sendMessage(Command.LANG_SUCCESS);
        return true;
    }
}
