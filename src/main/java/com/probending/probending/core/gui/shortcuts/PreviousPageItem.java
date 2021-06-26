package com.probending.probending.core.gui.shortcuts;

import com.probending.probending.ProBending;
import com.probending.probending.core.gui.GUIItem;
import com.probending.probending.core.gui.PBGUI;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

public class PreviousPageItem extends GUIItem {
    public PreviousPageItem(ItemStack item, int slot) {
        super(
                (p) -> {
                    ConcurrentLinkedQueue<PBGUI> pages = ProBending.playerM.GUIS_BY_PLAYER.get(p);
                    if (pages != null && !pages.isEmpty()) {
                        if (pages.size() > 1) {
                            pages.poll();
                            PBGUI gui = pages.peek();
                            gui.addPlayer(p);
                        } else {
                            pages.poll().removePlayer(p);
                        }
                    }
                },
                (p) -> {
                    ConcurrentLinkedQueue<PBGUI> pages = ProBending.playerM.GUIS_BY_PLAYER.get(p);
                    if (pages != null && !pages.isEmpty()) {
                        if (pages.size() > 1) {
                            pages.poll();
                            PBGUI gui = pages.peek();
                            gui.addPlayer(p);
                        } else {
                            pages.poll().removePlayer(p);
                        }
                    }
                },
                item, slot);
    }
}
