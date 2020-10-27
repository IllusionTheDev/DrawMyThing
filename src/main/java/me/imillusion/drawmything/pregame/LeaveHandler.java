package me.imillusion.drawmything.pregame;

import me.imillusion.drawmything.DrawPlugin;
import me.imillusion.drawmything.game.Game;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class LeaveHandler implements Listener {

    private final DrawPlugin main;

    public LeaveHandler(DrawPlugin main) {
        this.main = main;
    }

    @EventHandler
    private void onLeave(PlayerQuitEvent e) {
        main.getPlayerManager().unregister(e.getPlayer());
        Game game = main.getGameManager().getPlayerGame(e.getPlayer().getUniqueId());
        e.setQuitMessage("");

        if (game == null)
            return;

        game.getArena().getPlayers().forEach(player -> player.sendMessage(
                ChatColor.translateAlternateColorCodes('&',
                        main.getMessages().getMessage("player-leave")
                                .replace("%prefix%", main.getMessages().getPrefix())
                                .replace("%player%", e.getPlayer().getName())
                                .replace("%count%", String.valueOf(game.getArena().getUUIDs().size() - 1)))));

        game.getArena().removePlayer(e.getPlayer().getUniqueId());

        for (ItemStack item : e.getPlayer().getInventory().getContents())
            if (item != null && !item.isSimilar(main.getHidingHandler().getInactiveItem()) && !item.isSimilar(main.getHidingHandler().getActiveItem()))
                e.getPlayer().getInventory().remove(item);
    }
}
