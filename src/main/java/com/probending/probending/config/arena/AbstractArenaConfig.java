package com.probending.probending.config.arena;

import com.probending.probending.ProBending;
import com.probending.probending.core.arena.Arena;
import me.domirusz24.plugincore.config.AbstractConfig;
import me.domirusz24.plugincore.managers.ConfigManager;

import java.io.File;

public class AbstractArenaConfig extends AbstractConfig {

    private final Arena arena;

    public AbstractArenaConfig(Arena arena, String path, ProBending plugin) {
        super("Arenas/" + arena.getName() + "/" + path, plugin);
        this.arena = arena;
    }

    public AbstractArenaConfig(Arena arena, String path, ProBending plugin, ConfigManager manager) {
        super("Arenas/" + arena.getName() + "/" + path, plugin, manager);
        this.arena = arena;
    }

    public AbstractArenaConfig(Arena arena, File file, ProBending plugin) {
        super(file, plugin);
        this.arena = arena;
    }

    public Arena getArena() {
        return arena;
    }
}
