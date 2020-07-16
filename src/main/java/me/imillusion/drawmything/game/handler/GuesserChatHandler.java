package me.imillusion.drawmything.game.handler;

import me.imillusion.drawmything.DrawPlugin;
import me.imillusion.drawmything.game.Game;
import me.imillusion.drawmything.game.data.Round;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class GuesserChatHandler implements Listener {

    private final DrawPlugin main;

    public GuesserChatHandler(DrawPlugin main) {
        this.main = main;
    }

    @EventHandler
    private void onChat(AsyncPlayerChatEvent e)
    {
        Game game = main.getGameManager().getPlayerGame(e.getPlayer().getUniqueId());

        e.getRecipients().removeIf(player -> !game.getArena().getUUIDs().contains(player.getUniqueId()));

        if (!game.isStarted())
            return;

        Round round = game.getArena().getRound();

        if (round.isIntermission())
            return;

        if (round.getDrawer() != null && round.getDrawer().getUuid().equals(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
            return;
        }

        if (round.getGuessedPlayers().contains(e.getPlayer().getUniqueId())) {
            e.setMessage("§a" + e.getMessage().replaceAll("§[0-9a-fA-F]]", ChatColor.GREEN.toString()));
            e.getRecipients().removeIf(player -> !round.getGuessedPlayers().contains(player.getUniqueId()));
            return;
        }

        if (e.getMessage().toLowerCase().contains(round.getWord())) {
            e.setCancelled(true);

            round.getGuessedPlayers().add(e.getPlayer().getUniqueId());
            round.addPoints(e.getPlayer().getUniqueId());

            round.getArena().getPlayers().forEach(player -> player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    main.getMessages().getMessage("word-guess")
                            .replace("%prefix%", main.getMessages().getPrefix())
                            .replace("%player%", e.getPlayer().getName()))));

        }

    }
}
