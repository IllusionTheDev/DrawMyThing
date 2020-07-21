package me.imillusion.drawmything.files;

import lombok.Getter;
import me.imillusion.drawmything.gui.configuration.Path;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class MessagesFile extends YMLBase {

    @Path(path = "messages.prefix")
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
