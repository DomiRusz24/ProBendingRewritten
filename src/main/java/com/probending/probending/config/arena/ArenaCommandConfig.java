package com.probending.probending.config.arena;

import com.probending.probending.ProBending;
import com.probending.probending.config.CommandConfig;
import com.probending.probending.core.arena.Arena;

import java.io.File;

public class ArenaCommandConfig extends AbstractArenaConfig {


    public ArenaCommandConfig(Arena arena, String path, ProBending plugin) {
        super(arena, path, plugin);
        save();
    }

    public ArenaCommandConfig(Arena arena, File file, ProBending plugin) {
        super(arena, file, plugin);
        save();
    }

    @Override
    public boolean save() {
        for (CommandConfig.Commands value : CommandConfig.Commands.values()) {
            value.saveEmpty(this);
        }
        return super.save();
    }

    @Override
    public boolean reload() {
        boolean bool = super.reload();
        if (bool) {
            for (CommandConfig.Commands value : CommandConfig.Commands.values()) {
                value.reload(this);
            }
        }
        return bool;
    }
}
