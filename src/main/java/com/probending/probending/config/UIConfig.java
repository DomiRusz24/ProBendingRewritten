package com.probending.probending.config;

import com.sun.org.apache.xpath.internal.operations.Bool;
import me.domirusz24.plugincore.PluginCore;
import me.domirusz24.plugincore.config.AbstractConfig;
import me.domirusz24.plugincore.config.configvalue.ConfigValue;

import java.io.File;

public class UIConfig extends AbstractConfig {
    private final ConfigValue<String> bossBarTitle = new ConfigValue<>("bossbar.Title", "%fatigue%", this);
    private final ConfigValue<Boolean> actionBar = new ConfigValue<>("actionbar.Enabled", false, this);
    private final ConfigValue<String> actionBarValue = new ConfigValue<>("actionbar.Value", "%fatigue%", this);

    private final ConfigValue<String> playerIconDead = new ConfigValue<>("playericon.Dead", "Dead", this);
    private final ConfigValue<String> playerIconAlive = new ConfigValue<>("playericon.Alive", "Alive", this);
    public UIConfig(PluginCore plugin) {
        super("ui.yml", plugin);
        save();
    }

    public String getBossBarTitle() {
        return bossBarTitle.getValue();
    }

    public boolean isActionBarEnabled() {
        return actionBar.getValue();
    }

    public String getActionBarValue() {
        return actionBarValue.getValue();
    }

    public String getPlayerIconDead() {
        return playerIconDead.getValue();
    }

    public String getPlayerIconAlive() {
        return playerIconAlive.getValue();
    }


    @Override
    protected boolean autoGenerate() {
        return true;
    }
}
