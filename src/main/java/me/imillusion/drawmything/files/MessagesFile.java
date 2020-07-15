package me.imillusion.drawmything.files;

import lombok.Getter;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class MessagesFile extends YMLBase {

    @Getter
    private String prefix;

    public MessagesFile(JavaPlugin plugin) {
        super(plugin, new File(plugin.getDataFolder(), "messages.yml"), true);

        prefix = StringEscapeUtils.unescapeJava(getConfiguration().getString("messages.prefix"));
    }

    public void sendMessage(Player player, String name)
    {
        if (!getConfiguration().contains("messages." + name))
            return;

        player.sendMessage(
                ChatColor.translateAlternateColorCodes('&',
                        StringEscapeUtils.unescapeJava(getMessage(name).replace("%prefix%", prefix))));
    }

    public String getMessage(String name)
    {
        return getConfiguration().getString("messages." + name);
    }
}
