package com.probending.probending.core.displayable;

import com.probending.probending.PBListener;
import com.probending.probending.ProBending;
import com.probending.probending.core.displayable.interfaces.ClickableItem;
import com.probending.probending.core.displayable.interfaces.LeftClickable;
import com.probending.probending.core.displayable.interfaces.RightClickable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class PBItemStack implements ClickableItem {

    protected Consumer<Player> leftClick, rightClick;

    protected ItemStack item;

    public PBItemStack(Consumer<Player> leftClick, Consumer<Player> rightClick, ItemStack item) {
        this.leftClick = leftClick;
        this.rightClick = rightClick;
        this.item = item;
        PBListener.hookInListener((LeftClickable) this);
        PBListener.hookInListener((RightClickable) this);
    }

    public void giveToPlayer(Player player, int amount) {
        ItemStack clone = item.clone();
        clone.setAmount(amount);
        player.getInventory().addItem(clone);
    }

    public void giveToPlayer(Player player, int amount, int slot) {
        ItemStack clone = item.clone();
        clone.setAmount(amount);
        player.getInventory().setItem(slot, clone);
    }

    public void unregister() {
        PBListener.removeListener((LeftClickable) this);
        PBListener.removeListener((RightClickable) this);
    }

    @Override
    public ItemStack getItemStack() {
        return item;
    }

    @Override
    public void onLeftClick(Player player) {
        leftClick.accept(player);
    }

    @Override
    public void onRightClick(Player player) {
        rightClick.accept(player);
    }
}
