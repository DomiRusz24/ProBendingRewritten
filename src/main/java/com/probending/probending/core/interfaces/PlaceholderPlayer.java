package com.probending.probending.core.interfaces;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

public interface PlaceholderPlayer extends PlaceholderObject {

    Player getPlayer();

    @Override
    default String onPlaceholderRequest(String param) {
        return PlaceholderAPI.setPlaceholders(getPlayer(), param);
    }

    @Override
    default String placeHolderPrefix() {
        return "probending";
    }
}
