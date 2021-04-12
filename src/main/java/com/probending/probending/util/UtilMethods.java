package com.probending.probending.util;

import com.probending.probending.ProBending;
import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.event.AbilityLoadEvent;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import sun.reflect.ReflectionFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class UtilMethods {


    public static String translateColor(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }


    // Very useful for AutoComplete, in args just place args and in List place all of the autocomplete strings, and it will neatly output a list with alphabetical order.
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
        JavaPlugin plugin = ProBending.plugin;
        ClassLoader loader = plugin.getClass().getClassLoader();
        String path = "com.probending".replace('.', '/');
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
}
