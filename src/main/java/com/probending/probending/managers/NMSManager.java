package com.probending.probending.managers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.probending.probending.ProBending;
import net.minecraft.server.v1_12_R1.Packet;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;

public class NMSManager extends PBManager {

    private final PacketContainer ADVENTURE_PACKET;


    public NMSManager(ProBending plugin) {
        super(plugin);

        //TODO: Spectator

        PacketContainer adventure = ProBending.protocol.createPacket(PacketType.Play.Server.GAME_STATE_CHANGE);
        adventure.getIntegers().write(0, 3);
        adventure.getFloat().write(0, 2f);
        ADVENTURE_PACKET = adventure;
    }

    public void setToSpectator(Player player) {
        player.setGameMode(GameMode.SPECTATOR);
        try {
            ProBending.protocol.sendServerPacket(player, ADVENTURE_PACKET);
        } catch (InvocationTargetException e) {
            ProBending.plugin.log(Level.WARNING, "An error has occurred while setting players GameMode to spectator!");
        }
        player.setAllowFlight(true);
        player.setFlying(true);
    }



}
