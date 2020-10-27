package me.imillusion.drawmything.pregame;

import me.imillusion.drawmything.DrawPlugin;
import me.imillusion.drawmything.game.Game;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatHandler implements Listener {

    private DrawPlugin main;

    @EventHandler
    private void onChat(AsyncPlayerChatEvent e) {
        Game game = main.getGameManager().getPlayerGame(e.getPlayer().getUniqueId());

        e.getRecipients().removeIf(player -> !game.getArena().getUUIDs().contains(e.getPlayer().getUniqueId()));
    }
}
