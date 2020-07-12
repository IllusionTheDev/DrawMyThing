package me.imillusion.drawmything.files;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import me.imillusion.drawmything.utils.Pair;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

public class TitlesFile extends YMLBase {

    public TitlesFile(JavaPlugin plugin) {
        super(plugin, new File(plugin.getDataFolder(), "titles.yml"), true);
    }

    public void playTitle(String name, Pair<String, String>[] placeholders, Player... players)
    {
        if(!getConfiguration().contains("titles." + name))
            return;

        String title = StringEscapeUtils.unescapeJava(getConfiguration().getString("titles." + name + ".title"));
        String sub = StringEscapeUtils.unescapeJava(getConfiguration().getString("titles." + name + ".subtitle"));
        int fadeIn = getConfiguration().getInt("titles." + name + ".fadein");
        int stay = getConfiguration().getInt("titles." + name + ".stay");
        int fadeOut = getConfiguration().getInt("titles." + name + ".fadeout");

        for(Pair<String, String> pair : placeholders)
        {
            title = title.replace(pair.getKey(), pair.getValue());
            sub = sub.replace(pair.getKey(), pair.getValue());
        }

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
                WrappedChatComponent.fromText(ChatColor.translateAlternateColorCodes('&', sub)));

        try {
            for(Player player : players)
            {
                ProtocolLibrary.getProtocolManager().sendServerPacket(player, timesPacket);
                ProtocolLibrary.getProtocolManager().sendServerPacket(player, titlePacket);
                ProtocolLibrary.getProtocolManager().sendServerPacket(player, subtitlePacket);
            }
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    public void playTitle(String name, Player... players)
    {
        playTitle(name, new Pair[]{}, players);
    }
}
