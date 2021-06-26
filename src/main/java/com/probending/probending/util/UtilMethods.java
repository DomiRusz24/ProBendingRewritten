package com.probending.probending.util;

import com.probending.probending.ProBending;
import com.probending.probending.core.players.PBPlayer;
import com.probending.probending.core.team.PBTeam;
import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.event.AbilityLoadEvent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.java.JavaPlugin;
import sun.reflect.ReflectionFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
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
        if ((float) seconds / 10f < 1f) {
            return minutes + ":0" + seconds;
        } else {
            return minutes + ":" + seconds;
        }
    }


    public static List<String> getPossibleCompletions(List<String> args, List<String> possibilitiesOfCompletion) {
        String argumentToFindCompletionFor = args.get(args.size() - 1);
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
        BendingPlayer bp = BendingPlayer.getBendingPlayer(player);
        if (freeze) {
            if (bp != null) bp.blockChi();
        } else {
            if (bp != null) bp.unblockChi();
        }
    }

    public static Player[] toArray(List<Player> list) {
        Player[] array = new Player[list.size()];
        array = list.toArray(array);
        return array;
    }

    public static<T> T[] toArray(List<T> list, Class<T> clazz) {
        T[] array = (T[]) Array.newInstance(clazz, list.size());
        array = list.toArray(array);
        return array;
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

    public static PBTeam getSamePBTeam(PBPlayer... players) {
        return getSamePBTeam(Arrays.asList(players));
    }

    public static PBTeam getSamePBTeam(List<PBPlayer> players) {
        PBTeam team = null;
        for (PBPlayer player : players) {
            if (player == null) {
                System.out.println("PBPlayer is null somewhere");
                continue;
            }
            if (team == null) {
                team = player.getTeam();
            } else if (team != player.getTeam()) {
                return null;
            }
        }
        return team;
    }

    public static ItemStack createItem(Material type, byte data, String name, boolean glow, String... desc) {
        ItemStack is = new ItemStack(type, 1);
        ProBending.nmsM.setData(is, data);
        ItemMeta meta = is.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(desc));
        meta.setUnbreakable(true);
        if (glow) {
            meta.addEnchant(Enchantment.LUCK, 1, false);
        }
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE);
        is.setItemMeta(meta);
        return is;
    }


}
