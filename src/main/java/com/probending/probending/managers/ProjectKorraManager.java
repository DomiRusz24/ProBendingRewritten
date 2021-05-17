package com.probending.probending.managers;

import com.probending.probending.ProBending;
import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.ability.CoreAbility;

import java.util.Arrays;
import java.util.List;

public class ProjectKorraManager extends PBManager {

    public static final List<String> LEGAL_ELEMENTS = Arrays.asList(Element.EARTH.getName(), Element.AIR.getName(), Element.FIRE.getName(), Element.WATER.getName());
    public ProjectKorraManager(ProBending plugin) {
        super(plugin);
    }

    public Element getBasicElement(Element element) {
        return element instanceof Element.SubElement ? ((Element.SubElement) element).getParentElement() : element;
    }

    public Element getFirstElement(BendingPlayer bendingPlayer) {
        for (String name : bendingPlayer.getAbilities().values()) {
            CoreAbility ability = CoreAbility.getAbility(name);
            if (ability != null) {
                return getBasicElement(ability.getElement());
            }
        }
        return null;
    }

    public BendingState getBendingState(BendingPlayer bendingPlayer) {

        if (bendingPlayer == null) return BendingState.NONE;

        Element element = null;

        for (String name : bendingPlayer.getAbilities().values()) {
            CoreAbility ability = CoreAbility.getAbility(name);
            if (ability != null) {
                Element ele = getBasicElement(ability.getElement());
                if (LEGAL_ELEMENTS.contains(ele.getName())) {
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



    public enum BendingState {
        NONE,
        CORRECT,
        MULTI,
        ILLEGAL;
    }
}
