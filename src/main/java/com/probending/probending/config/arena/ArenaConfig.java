package com.probending.probending.config.arena;

import com.probending.probending.ProBending;
import com.probending.probending.config.PluginConfig;
import com.probending.probending.core.arena.Arena;
import me.domirusz24.plugincore.config.configvalue.ConfigValue;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArenaConfig extends AbstractArenaConfig {

    private final ConfigValue<Integer> roundTime = new ConfigValue<>("RoundTime", 180, this, true);
    private final ConfigValue<Integer> roundWin = new ConfigValue<>("RoundWin", 3, this, true);
    private final ConfigValue<Integer> minimalPlayerAmount = new ConfigValue<>("MinimalPlayerAmount", 2, this, true);

    private final ConfigValue<Integer> autoStart = new ConfigValue<>("AutoStart", -1, this, true);
    private ConfigValue<Boolean> damage = new ConfigValue<>("Damage", true, this);
    private ConfigValue<Boolean> publicArena = new ConfigValue<>("Public", true, this);
    private ConfigValue<List<String>> bannedAbilities = new ConfigValue<>("BannedAbilities", Arrays.asList(""), this);
    private ConfigValue<Double> dragValue = new ConfigValue<>("DragForce", 1.0, this, true);
    private ConfigValue<Integer> tirednessMax = new ConfigValue<>("FatigueMax", 200, this, true);
    private ConfigValue<Integer> hpToTirednessRatio = new ConfigValue<>("HPToFatigueRatio", 4, this, true);
    private ConfigValue<Boolean> resetFatigueOnStageLose = new ConfigValue<>("ResetFatigueOnStageLose", false, this, true);
    private ConfigValue<Integer> woolLayerLevel = new ConfigValue<>("WoolLayerLevel", ProBending.pluginConfig.getYLevel(), this, true);

    private ConfigValue<Boolean> hat = new ConfigValue<>("Armor.Hat", true, this);
    private ConfigValue<Boolean> body = new ConfigValue<>("Armor.Body", true, this);

    private ConfigValue<Boolean> multibending = new ConfigValue<>("AllowedElements.MultiBending", false, this);
    private ConfigValue<Boolean> otherelements = new ConfigValue<>("AllowedElements.OtherElement", false, this);

    private ConfigValue<Boolean> runLeaveCommandInMenu = new ConfigValue<>("Lobby.RunLeaveCommand", true, this);


    public ArenaConfig(Arena arena, String path, ProBending plugin) {
        super(arena, path, plugin);
        save();
    }

    public ArenaConfig(Arena arena, File file, ProBending plugin) {
        super(arena, file, plugin);
        save();
    }

    public List<String> getBannedAbilities() {
        List<String> s = new ArrayList<>(bannedAbilities.getValue());
        s.addAll(ProBending.pluginConfig.getBannedAbilities());
        return s;
    }

    public Integer getRoundTime() {
        return Math.max(roundTime.getValue(), 60);
    }

    public Integer getRoundWin() {
        return Math.max(roundWin.getValue(), 2);
    }

    public Integer getMinimalPlayerAmount() {
        return Math.max(Math.min(minimalPlayerAmount.getValue(), 6), 2);
    }

    public int getTirednessMax() {
        return tirednessMax.getValue();
    }

    public int getHpToTirednessRatio() {
        return hpToTirednessRatio.getValue();
    }

    public boolean getDamage() {
        return damage.getValue();
    }

    public double getDragValue() {
        return dragValue.getValue();
    }

    public boolean getResetFatigueOnStageLose() {
        return resetFatigueOnStageLose.getValue();
    }

    public boolean isPublic() {
        return publicArena.getValue();
    }

    public Boolean getHat() {
        return hat.getValue();
    }

    public Boolean getBody() {
        return body.getValue();
    }

    public Integer getYLevel() {
        return woolLayerLevel.getValue();
    }

    public boolean getMultiBending() {
        return multibending.getValue();
    }

    public boolean getOtherElements() {
        return otherelements.getValue();
    }

    public boolean getRunLeaveCommandInMenu() {
        return runLeaveCommandInMenu.getValue();
    }

    public int getAutoStart() {
        return autoStart.getValue();
    }
}
