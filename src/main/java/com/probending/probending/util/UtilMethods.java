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

public class UtilMethods extends me.domirusz24.plugincore.util.UtilMethods {

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


}
