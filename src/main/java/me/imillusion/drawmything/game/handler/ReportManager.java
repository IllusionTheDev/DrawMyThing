package me.imillusion.drawmything.game.handler;

import me.imillusion.drawmything.DrawPlugin;
import me.imillusion.drawmything.data.DrawPlayer;
import me.imillusion.drawmything.events.GameStartEvent;
import me.imillusion.drawmything.game.Game;
import me.imillusion.drawmything.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ReportManager implements Listener {

    private final ItemStack item;
    private final DrawPlugin main;

    public ReportManager(DrawPlugin main) {
        this.main = main;
        this.item = new ItemBuilder(Material.BARRIER)
                .name("&c&lReport this player.")
                .build();
    }

    @EventHandler
    private void onStart(GameStartEvent e) {
        e.getArena().getPlayers().forEach(player -> player.getInventory().setItem(7, item));
    }

    @EventHandler
    private void onClick(PlayerInteractEvent e) {
        if (e.getItem() == null || !e.getItem().isSimilar(item))
            return;

        DrawPlayer player = main.getPlayerManager().get(e.getPlayer());
        Game game = player.getCurrentGame();

        if (game == null)
            return;

        System.out.println(main.getImageSaver().serializeCanvas(game.getArena().getCanvas()));

    }
}
