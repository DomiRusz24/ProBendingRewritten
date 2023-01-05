package com.probending.probending.managers;

import com.probending.probending.ProBending;
import com.probending.probending.core.arena.states.StartingState;
import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.OfflineBendingPlayer;
import com.projectkorra.projectkorra.ability.CoreAbility;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

public class ProjectKorraManager extends PBManager {

    private final HashMap<UUID, BendingPlayer> bendingPlayerByUUID = new HashMap<>();

    public static final List<String> LEGAL_ELEMENTS = Arrays.asList(Element.EARTH.getName(), Element.AIR.getName(), Element.FIRE.getName(), Element.WATER.getName());
    private final Function<Element, String> getColorPrefix;

    public ProjectKorraManager(ProBending plugin) {
        super(plugin);

        getColorPrefix = (e) -> e.getColor().toString();
    }

    public Element getBasicElement(Element element) {
        return element instanceof Element.SubElement ? ((Element.SubElement) element).getParentElement() : element;
    }

    public HashMap<UUID, BendingPlayer> getBendingPlayerByUUID() {
        return bendingPlayerByUUID;
    }

    public BendingPlayer getBendingPlayer(Player player) {
        return BendingPlayer.getBendingPlayer(player);
    }

    public String getColor(Element element) {
        return getColorPrefix.apply(element);
    }

    public Element getFirstElement(OfflineBendingPlayer bendingPlayer) {
        for (String name : bendingPlayer.getAbilities().values()) {
            CoreAbility ability = CoreAbility.getAbility(name);
            if (ability != null) {
                return getBasicElement(ability.getElement());
            }
        }
        return null;
    }

    public BendingState getBendingState(OfflineBendingPlayer bendingPlayer, List<String> illegalAbilities) {

        if (bendingPlayer == null) return BendingState.NULL;

        Element element = null;

        for (String name : bendingPlayer.getAbilities().values()) {
            if (name == null) continue;
            CoreAbility ability = CoreAbility.getAbility(name);
            if (ability != null) {
                Element ele = getBasicElement(ability.getElement());
                if (LEGAL_ELEMENTS.contains(ele.getName()) && !illegalAbilities.contains(ability.getName())) {
                    if (element == null) {
                        element = ele;
                    } else {
                        if (element != ele) {
                            return BendingState.MULTI;
                        }
                    }
                } else {
                    return BendingState.ILLEGAL;
                }
            }
        }

        return element == null ? BendingState.NONE : BendingState.CORRECT;
    }

    public boolean hasBoardEnabled(Player player) {
        for (Objective objective : player.getScoreboard().getObjectives()) {
            if (objective.getDisplayName().equals(StartingState.LANG_PB_BOARD_TITLE)) {
                return true;
            }
        }
        return false;
    }



    public enum BendingState {
        NONE,
        CORRECT,
        MULTI,

        NULL,
        ILLEGAL;
    }
}
