package me.imillusion.drawmything.pregame;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public class ItemEventHandler implements Listener {

    @EventHandler
    private void onDrop(PlayerDropItemEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    private void onClick(InventoryClickEvent e) {
        e.setCancelled(true);
    }
}
