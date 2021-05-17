package com.probending.probending.managers.schematics;

import com.probending.probending.ProBending;
import com.probending.probending.command.abstractclasses.Command;
import com.probending.probending.managers.PBManager;
import com.sk89q.worldedit.*;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardWriter;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.internal.LocalWorldAdapter;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.session.ClipboardHolder;
import javafx.util.Pair;
import net.shadowxcraft.rollbackcore.Copy;
import net.shadowxcraft.rollbackcore.Paste;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class EasyRollBackManager extends SchematicManager {

    public EasyRollBackManager(ProBending plugin) {
        super(plugin);
    }

    @Override
    public void saveSchematic(CommandSender sender, Location min, Location max, File folder, String name) {
        (new Copy(min.getBlockX(), min.getBlockY(), min.getBlockZ(), max.getBlockX(), max.getBlockY(), max.getBlockZ(), min.getWorld(), new File(folder, name + ".dat").toString(), sender)).run();
    }

    @Override
    public boolean getSchematic(CommandSender sender, Location location, File folder, String name) {
        (new Paste(location, new File(folder, name + ".dat").toString(), sender, true, false, Command.LANG_SUCCESS_PREFIX + " ")).run();
        return true;
    }


}
