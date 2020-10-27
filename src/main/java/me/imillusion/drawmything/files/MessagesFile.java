package me.imillusion.drawmything.files;

import lombok.Getter;
import me.clip.placeholderapi.PlaceholderAPI;
import me.imillusion.drawmything.DrawPlugin;
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

    public void sendMessage(Player player, String name) {
        if (!getConfiguration().contains("messages." + name))
            return;

        String msg = StringEscapeUtils.unescapeJava(getMessage(name).replace("%prefix%", prefix));

        if (DrawPlugin.hookPlaceholders())
            msg = PlaceholderAPI.setPlaceholders(player, msg);

        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
    }

    public String getMessage(String name) {
        String msg = getConfiguration().getString("messages." + name);

        return getConfiguration().getString("messages." + name);
    }
}
