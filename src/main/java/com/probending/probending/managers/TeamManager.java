package com.probending.probending.managers;

import com.probending.probending.ProBending;
import com.probending.probending.core.players.ActivePlayer;
import com.probending.probending.core.team.ActiveTeam;
import com.probending.probending.core.team.ArenaTempTeam;
import com.probending.probending.core.team.PreArenaTeam;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.HashMap;

public class TeamManager extends PBManager {

    public final HashMap<ActivePlayer, ActiveTeam> TEAM_BY_PLAYER = new HashMap<>();

    public final HashMap<Player, ArenaTempTeam> TEMPTEAM_BY_PLAYER = new HashMap<>();

    public final HashMap<Player, PreArenaTeam> PREARENATEAM_BY_PLAYER = new HashMap<>();

    public final ItemStack[] BLUE_ARMOR;

    public final ItemStack[] RED_ARMOR;

    public TeamManager(ProBending plugin) {
        super(plugin);
        BLUE_ARMOR = getArmorByColor(Color.BLUE);
        RED_ARMOR = getArmorByColor(Color.RED);
    }

    private ItemStack[] getArmorByColor(Color color) {
        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
        setColor(helmet, color);
        ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
        setColor(chestplate, color);
        ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
        setColor(leggings, color);
        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
        setColor(boots, color);
        return new ItemStack[]{boots, leggings, chestplate, helmet};
    }

    private void setColor(ItemStack armor, Color color) {
        LeatherArmorMeta meta = (LeatherArmorMeta) armor.getItemMeta();
        meta.setColor(color);
        meta.addEnchant(Enchantment.BINDING_CURSE, 1, false);
        meta.addEnchant(Enchantment.VANISHING_CURSE, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
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

}
