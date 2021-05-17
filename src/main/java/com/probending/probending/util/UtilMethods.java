package com.probending.probending.util;

import com.probending.probending.ProBending;
import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.event.AbilityLoadEvent;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import sun.reflect.ReflectionFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

public class UtilMethods {


    public static String translateColor(String string) {
        return ChatColor.translateAlternateColorCodes('&', string.replaceAll("\\Q|\\E\\Q|\\E", "\n&r"));
    }

    public static String[] stringToList(String string) {
        return string.split("\\n");
    }

    public static String secondsToMinutes(int seconds) {
        int minutes = (int) ((double) seconds / 60);
        seconds = seconds - (minutes * 60);
        return minutes + ":" + seconds;
    }


    public static List<String> getPossibleCompletions(String[] args, List<String> possibilitiesOfCompletion) {
        String argumentToFindCompletionFor = args[args.length - 1];
        ArrayList<String> listOfPossibleCompletions = new ArrayList<>();

        for (String foundString : possibilitiesOfCompletion) {
            if (foundString.regionMatches(true, 0, argumentToFindCompletionFor, 0, argumentToFindCompletionFor.length())) {
                listOfPossibleCompletions.add(foundString);
            }
        }
        return listOfPossibleCompletions;
    }


    // Outputs all classes in com.probending, really useful for annotations!
    public static List<Class<?>> findClasses() {
        return findClasses("com.probending.probending");
    }

    public static List<Class<?>> findClasses(String path) {
        JavaPlugin plugin = ProBending.plugin;
        ClassLoader loader = plugin.getClass().getClassLoader();
        path = path.replace('.', '/');
        JarFile jar = null;
        if (loader != null) {
            try {
                Enumeration<URL> resources = loader.getResources(path);
                String jarloc = resources.nextElement().getPath();
                jarloc = jarloc.substring(5, jarloc.length() - path.length() - 2);
                String s = URLDecoder.decode(jarloc, "UTF-8");
                jar = new JarFile(new File(s));
            } catch (IOException error) {
                error.printStackTrace();
                return null;
            }
        }
        if (jar == null) return null;
        ArrayList<Class<?>> classes = new ArrayList<>();
        Enumeration<?> entries = jar.entries();

        while (entries.hasMoreElements()) {
            JarEntry entry = (JarEntry) entries.nextElement();
            if (entry.getName().endsWith(".class") && !entry.getName().contains("$")) {
                String className = entry.getName().replace('/', '.').substring(0, entry.getName().length() - 6);
                if (className.startsWith(path.replace('/', '.'))) {
                    Class<?> clazz;
                    try {
                        clazz = Class.forName(className, true, loader);
                        if (clazz != null) classes.add(clazz);
                    } catch (Error | Exception error) {
                        error.printStackTrace();
                        return null;
                    }
                }
            }
        }
        return classes;
    }

    // Outputs all files in a directory
    public static List<File> getFiles(File directory) {
        List<File> files = new ArrayList<>();
        if (directory == null) {
            return files;
        }
        if (directory.isFile()) {
            directory = directory.getParentFile();
        }
        File[] folderFiles = directory.listFiles();
        assert folderFiles != null;
        for (File file : folderFiles) {
            if (!file.isDirectory()) {
                files.add(file);
            }
        }
        return files;
    }

    public static List<File> getDirectories(File directory) {
        List<File> files = new ArrayList<>();
        if (directory == null) {
            return files;
        }
        if (directory.isFile()) {
            directory = directory.getParentFile();
        }
        File[] folderFiles = directory.listFiles();
        assert folderFiles != null;
        for (File file : folderFiles) {
            if (file.isDirectory()) {
                files.add(file);
            }
        }
        return files;
    }

    public static void freezePlayer(Player player, boolean freeze) {
        ProBending.playerM.freezePlayer(player, freeze);
        if (freeze) {
            BendingPlayer bp = BendingPlayer.getBendingPlayer(player);
            if (bp != null) bp.blockChi();
            player.setGravity(false);
        } else {
            BendingPlayer bp = BendingPlayer.getBendingPlayer(player);
            if (bp != null) bp.unblockChi();
            player.setGravity(true);
        }
    }

    private static int getStage(int num, int stage1, int stage2) {
        return num > stage1 ? 1 : num > stage2 ? 2 : 3;
    }

    public static String getNumberPrefix(int num) {
        int stage = getStage(num, 5, 3);
        switch (stage) {
            case 1: return ChatColor.GREEN + "";
            case 2: return ChatColor.YELLOW + "";
            default: return ChatColor.RED + "" + ChatColor.BOLD + "";
        }
    }

    public static String getPercentPrefix(int num) {
        int stage = getStage(num, 66, 33);
        switch (stage) {
            case 1: return ChatColor.GREEN + "";
            case 2: return ChatColor.YELLOW + "";
            default: return ChatColor.RED + "" + ChatColor.BOLD + "";
        }
    }

    public static BarColor getBarColorFromPercent(int num) {
        int stage = getStage(num, 66, 33);
        switch (stage) {
            case 1: return BarColor.GREEN;
            case 2: return BarColor.YELLOW;
            default: return BarColor.RED;
        }
    }


}
