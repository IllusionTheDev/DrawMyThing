package me.imillusion.drawmything.pregame;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class WorldActionsHandler implements Listener {

    @EventHandler
    private void onBreak(BlockBreakEvent e)
    {
        e.setCancelled(true);
    }

    @EventHandler
    private void onDamage(EntityDamageEvent e)
    {
        e.setCancelled(true);
    }
}
