package me.imillusion.drawmything.game.handler;

import me.imillusion.drawmything.DrawPlugin;
import me.imillusion.drawmything.game.Game;
import me.imillusion.drawmything.game.data.drawing.Drawer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class DrawerMoveHandler implements Listener {

    private DrawPlugin main;

    public DrawerMoveHandler(DrawPlugin main) {
        this.main = main;
    }

    @EventHandler
    private void onMove(PlayerMoveEvent e)
    {
        Player player = e.getPlayer();
        Game game = main.getGameManager().getPlayerGame(player.getUniqueId());

        if(game == null)
            return;

        Drawer drawer = game.getArena().getRound().getDrawer();

        if(drawer == null)
            return;

        if(main.getSettings().getDrawingTime() - game.getArena().getRound().getDrawingTime() > 1)
        if(drawer.getUuid().equals(player.getUniqueId()))
            if(e.getFrom().getX() != e.getTo().getX() || e.getFrom().getY() != e.getTo().getY() || e.getFrom().getZ() != e.getTo().getZ())
            {
                Location copy = e.getFrom().clone();
                copy.setPitch(e.getTo().getPitch());
                copy.setYaw(e.getTo().getYaw());
                e.getPlayer().teleport(copy);
            }
    }
}
