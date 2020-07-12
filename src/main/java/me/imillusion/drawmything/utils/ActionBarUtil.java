package me.imillusion.drawmything.utils;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

public class ActionBarUtil {

    public static void sendActionbarMessage(String message, Player... players)
    {
        PacketContainer barPacket = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.CHAT);

        barPacket.getChatComponents().write(0,
            WrappedChatComponent.fromText(ChatColor.translateAlternateColorCodes('&', message)));

        barPacket.getBytes().write(0, (byte) 2);

        try {
            for(Player p : players)
                ProtocolLibrary.getProtocolManager().sendServerPacket(p, barPacket);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
