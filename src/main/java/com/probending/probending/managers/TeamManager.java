package com.probending.probending.managers;

import com.probending.probending.ProBending;
import com.probending.probending.core.enums.TeamTag;
import com.probending.probending.core.players.ActivePlayer;
import com.probending.probending.core.players.PBPlayer;
import com.probending.probending.core.team.ActiveTeam;
import com.probending.probending.core.team.ArenaTempTeam;
import com.probending.probending.core.team.PBTeam;
import com.probending.probending.core.team.PreArenaTeam;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.function.Consumer;

public class TeamManager extends PBManager {

    public final HashMap<ActivePlayer, ActiveTeam> TEAM_BY_PLAYER = new HashMap<>();

    public final HashMap<String, PBTeam> PBTEAM_BY_PLAYER = new HashMap<>();
    public final HashMap<String, PBTeam> PBTEAM_BY_NAME = new HashMap<>();

    public final HashMap<Player, ArenaTempTeam> TEMPTEAM_BY_PLAYER = new HashMap<>();

    public final HashMap<Player, PreArenaTeam> PREARENATEAM_BY_PLAYER = new HashMap<>();

    private final ItemStack[] BLUE_ARMOR;

    private final ItemStack[] RED_ARMOR;

    public TeamManager(ProBending plugin) {
        super(plugin);
        BLUE_ARMOR = getArmorByColor(Color.BLUE);
        RED_ARMOR = getArmorByColor(Color.RED);
    }

    public ItemStack[] getArmor(TeamTag team, boolean boots, boolean leggings, boolean chestplate, boolean helmet) {
        ItemStack[] armor = team == TeamTag.BLUE ? BLUE_ARMOR.clone() : RED_ARMOR.clone();
        if (!boots) armor[0] = new ItemStack(Material.AIR);
        if (!leggings) armor[1] = new ItemStack(Material.AIR);
        if (!chestplate) armor[2] = new ItemStack(Material.AIR);
        if (!helmet) armor[3] = new ItemStack(Material.AIR);
        return armor;
    }

    public ItemStack[] getArmorByColor(Color color, boolean unbreakable) {
        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
        setColor(helmet, color, unbreakable);
        ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
        setColor(chestplate, color, unbreakable);
        ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
        setColor(leggings, color, unbreakable);
        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
        setColor(boots, color, unbreakable);
        return new ItemStack[]{boots, leggings, chestplate, helmet};
    }

    public ItemStack[] getArmorByColor(Color color) {
        return getArmorByColor(color, true);
    }

    private void setColor(ItemStack armor, Color color, boolean unbreakable) {
        LeatherArmorMeta meta = (LeatherArmorMeta) armor.getItemMeta();
        meta.setColor(color);
        meta.addEnchant(Enchantment.BINDING_CURSE, 1, false);
        meta.addEnchant(Enchantment.VANISHING_CURSE, 1, false);
        meta.setUnbreakable(unbreakable);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE);
        armor.setItemMeta(meta);
    }

    // ------ TEAM ------

    public ActiveTeam getTeam(Player player) {
        ActivePlayer activePlayer = ProBending.playerM.getActivePlayer(player);
        return activePlayer == null ? null : getTeam(activePlayer);
    }

    public ActiveTeam getTeam(ActivePlayer player) {
        return TEAM_BY_PLAYER.getOrDefault(player, null);
    }

    // ------ TEMP TEAM ------

    public ArenaTempTeam getTempTeam(Player player) {
        return TEMPTEAM_BY_PLAYER.getOrDefault(player, null);
    }

    // ------ PRE ARENA TEAM ------

    public PreArenaTeam getArenaTeam(Player player) {
        return PREARENATEAM_BY_PLAYER.getOrDefault(player, null);
    }

    // ------ PB TEAM ------

    public PBTeam getPBTeam(PBPlayer player) {
        return PBTEAM_BY_PLAYER.getOrDefault(player.getName(), null);
    }

    public PBTeam getPBTeam(String name) {
        return PBTEAM_BY_PLAYER.getOrDefault(name, null);
    }

    public PBTeam getPBTeamByName(String name) {
        return PBTEAM_BY_NAME.getOrDefault(name, null);
    }

    public Collection<PBTeam> getPBTeams() {
        return PBTEAM_BY_NAME.values();
    }
}
