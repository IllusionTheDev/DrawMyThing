package me.imillusion.drawmything.utils;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

public final class TitleUtil {

    private TitleUtil() {
        //Avoid initializing in utility class
    }

    public static void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut, Player... players) {
        if (players.length == 0)
            return;

        PacketContainer titlePacket = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.TITLE);
        PacketContainer subtitlePacket = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.TITLE);
        PacketContainer timesPacket = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.TITLE);

        titlePacket.getTitleActions().write(0, EnumWrappers.TitleAction.TITLE);
        subtitlePacket.getTitleActions().write(0, EnumWrappers.TitleAction.SUBTITLE);
        timesPacket.getTitleActions().write(0, EnumWrappers.TitleAction.TIMES);

        timesPacket.getIntegers().write(0, fadeIn);
        timesPacket.getIntegers().write(1, stay);
        timesPacket.getIntegers().write(2, fadeOut);

        titlePacket.getChatComponents().write(0,
                WrappedChatComponent.fromText(ChatColor.translateAlternateColorCodes('&', title)));
        subtitlePacket.getChatComponents().write(0,
                WrappedChatComponent.fromText(ChatColor.translateAlternateColorCodes('&', subtitle)));

        try {
            for (Player player : players) {
                ProtocolLibrary.getProtocolManager().sendServerPacket(player, timesPacket);
                ProtocolLibrary.getProtocolManager().sendServerPacket(player, titlePacket);
                ProtocolLibrary.getProtocolManager().sendServerPacket(player, subtitlePacket);
            }
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
