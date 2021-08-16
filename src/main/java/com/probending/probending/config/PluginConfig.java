package com.probending.probending.config;

import com.probending.probending.ProBending;
import com.probending.probending.util.UtilMethods;
import me.domirusz24.plugincore.config.configvalue.ConfigValue;
import net.lingala.zip4j.core.ZipFile;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

public class PluginConfig {

    private ConfigValue<Boolean> autoLanguage;

    private ConfigValue<Integer> levelY;

    private ConfigValue<List<String>> bannedAbilities;

    public PluginConfig() {
        levelY = new ConfigValue<>("Arena.levelY", 14, ProBending.configM.getConfig());
        bannedAbilities = new ConfigValue<>("BannedAbilities", Collections.singletonList(""), ProBending.configM.getConfig());
        autoLanguage = new ConfigValue<>("AutoDownloadLanguage", false, ProBending.configM.getConfig());
        ProBending.configM.getConfig().save();

        if (getAutoLanguage()) {
            File folder = new File(ProBending.plugin.getDataFolder(), "Languages/Languages.zip");
            folder.mkdirs();
            try {
                UtilMethods.copyURLToFile(
                        new URL("https://download-directory.github.io/?url=https%3A%2F%2Fgithub.com%2FDomiRusz24%2FProBendingLanguages%2Ftree%2Fmaster%2FLanguages"), folder);
            } catch (Exception e) {
                ProBending.plugin.log(Level.WARNING, "Couldn't download Language file from GitHub!");
                e.printStackTrace();
                return;
            }
            try {
                ZipFile zipFile = new ZipFile(folder);
                zipFile.extractAll(folder.getParent());
            } catch (Exception e) {
                ProBending.plugin.log(Level.WARNING, "Couldn't extract Language.zip file!");
                e.printStackTrace();
                return;
            }

        }
    }

    public int getYLevel() {
        return levelY.getValue();
    }

    public Boolean getAutoLanguage() {
        return autoLanguage.getValue();
    }

    public List<String> getBannedAbilities() {
        return bannedAbilities.getValue();
    }
}
