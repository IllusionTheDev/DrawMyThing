package me.imillusion.drawmything.game.handler;

import me.imillusion.drawmything.DrawPlugin;
import me.imillusion.drawmything.data.DrawPlayer;
import me.imillusion.drawmything.game.arena.Arena;
import me.imillusion.drawmything.utils.PointConverter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Set;

public class DrawingHandler implements Listener {

    private final DrawPlugin main;

    public DrawingHandler(DrawPlugin main) {
        this.main = main;

        Bukkit.getScheduler().runTaskTimerAsynchronously(main, () -> main.getGameManager().getActiveGames().forEach(game -> {
            Arena arena = game.getArena();
            if (arena.getRound().getDrawer() != null && arena.getRound().getDrawer().getTicksleft() != 0)
                    arena.getRound().getDrawer().setTicksleft(arena.getRound().getDrawer().getTicksleft() - 1);
        }), 1L, 1L);
    }

    @EventHandler
    private void onInteract(PlayerInteractEvent e)
    {
        Player player = e.getPlayer();
        DrawPlayer drawPlayer = main.getPlayerManager().get(player);

        handleDrawing(drawPlayer);
    }

    @EventHandler
    private void onMove(PlayerMoveEvent e)
    {
        Player player = e.getPlayer();
        DrawPlayer drawPlayer = main.getPlayerManager().get(player);

        handleDrawing(drawPlayer);
    }

    private void handleDrawing(DrawPlayer player) {
        Player p = player.getPlayer();
        if (!player.isDrawer())
            return;

        Block clickedBlock = p.getTargetBlock((Set<Material>) null, 100);
        Arena arena = player.getCurrentGame().getArena();

        if (!PointConverter.locationBelongs(clickedBlock.getLocation(), arena.getCanvas()))
            return;

        if (player.getSelectedPaintTool() == null)
            return;

        player.getSelectedPaintTool().apply(arena.getCanvas(), arena.getCanvas().adaptPoint(clickedBlock.getLocation()), player.getSelectedColor());
        main.getSounds().playSound(p, "tools." + player.getSelectedPaintTool().getIdentifier());
    }

}
