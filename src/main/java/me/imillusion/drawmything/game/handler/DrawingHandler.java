package me.imillusion.drawmything.game.handler;

import me.imillusion.drawmything.DrawPlugin;
import me.imillusion.drawmything.game.Game;
import me.imillusion.drawmything.game.arena.Arena;
import me.imillusion.drawmything.game.data.drawing.Drawer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Set;
import java.util.UUID;

public class DrawingHandler implements Listener {

    private DrawPlugin main;

    public DrawingHandler(DrawPlugin main) {
        this.main = main;

        Bukkit.getScheduler().runTaskTimerAsynchronously(main, () -> main.getGameManager().getActiveGames().forEach(game -> {
            Arena arena = game.getArena();
            if(arena.getRound().getDrawer() != null)
                if(arena.getRound().getDrawer().getTicksleft() != 0)
                    arena.getRound().getDrawer().setTicksleft(arena.getRound().getDrawer().getTicksleft() - 1);
        }), 1L, 1L);
    }

    @EventHandler
    private void onInteract(PlayerInteractEvent e)
    {
        if(e.getAction() != Action.RIGHT_CLICK_AIR)
            return;

        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        Block clickedBlock = player.getTargetBlock((Set<Material>) null, 100);
        Arena arena = main.getGameManager().getPlayerGame(uuid).getArena();

        if(!canBuild(uuid))
            return;

        Drawer drawer = arena.getRound().getDrawer();

        if(drawer.getSelectedPaintTool() != null)
            drawer.getSelectedPaintTool().apply(arena.getCanvas(), arena.getCanvas().adaptPoint(clickedBlock.getLocation()), drawer.getSelectedColor());

    }

    @EventHandler
    private void onMove(PlayerMoveEvent e)
    {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        Block clickedBlock = player.getTargetBlock((Set<Material>) null, 100);
        Game game = main.getGameManager().getPlayerGame(uuid);

        if(game == null)
            return;

        if(!canBuild(uuid))
            return;

        Arena arena = game.getArena();

        Drawer drawer = arena.getRound().getDrawer();

        if(drawer.getSelectedPaintTool() != null)
            drawer.getSelectedPaintTool().applyMove(arena.getCanvas(), arena.getCanvas().adaptPoint(clickedBlock.getLocation()), drawer.getSelectedColor());
    }

    private boolean canBuild(UUID uuid)
    {
        Player player = Bukkit.getPlayer(uuid);
        Block clickedBlock = player.getTargetBlock((Set<Material>) null, 100);
        Arena arena = main.getGameManager().getPlayerGame(uuid).getArena();

        if(arena.getRound().getDrawer() == null)
            return false;

        if(!arena.getRound().getDrawer().getUuid().equals(uuid))
            return false;

        if(!arena.getCanvas().belongs(clickedBlock.getLocation()))
            return false;

        return true;
    }



}
