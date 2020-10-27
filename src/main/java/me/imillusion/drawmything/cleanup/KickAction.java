package me.imillusion.drawmything.cleanup;

import me.imillusion.drawmything.DrawPlugin;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.function.BiConsumer;

public class KickAction implements BiConsumer<DrawPlugin, Player> {

    @Override
    public void accept(DrawPlugin main, Player player) {
        String message = String.join("\\n", main.getSettings().getConfiguration().getStringList("kick-message"));

        player.kickPlayer(ChatColor.translateAlternateColorCodes('&', message));
    }
}
