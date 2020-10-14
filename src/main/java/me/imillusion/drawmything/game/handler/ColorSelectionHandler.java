package me.imillusion.drawmything.game.handler;

import me.imillusion.drawmything.DrawPlugin;
import me.imillusion.drawmything.game.arena.Arena;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Set;
import java.util.UUID;

public class ColorSelectionHandler implements Listener {

    private final DrawPlugin main;

    public ColorSelectionHandler(DrawPlugin main) {
        this.main = main;
    }

    @EventHandler
    private void onClick(PlayerInteractEvent e)
    {
        if (e.getAction() != Action.LEFT_CLICK_AIR)
            return;

        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        Block clickedBlock = player.getTargetBlock((Set<Material>) null, 100);
        Arena arena = main.getGameManager().getPlayerGame(player.getUniqueId()).getArena();

        if (arena.getRound().getDrawer() == null || !arena.getRound().getDrawer().getUuid().equals(uuid))
            return;

        arena.getMap().getColorSelectionAreas().forEach(area -> {
            if (area.isWithin(clickedBlock.getLocation())) {
                arena.getRound().getDrawer().setSelectedColor(area.getColor());
                main.getSounds().playSound(player, "color-selection");
                player.sendMessage(main.getMessages().getMessage("color-selection." + area.getColor().name().toLowerCase()));
            }
        });
    }
}
