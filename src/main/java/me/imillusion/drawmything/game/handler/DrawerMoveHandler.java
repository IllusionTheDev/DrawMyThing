package me.imillusion.drawmything.game.handler;

import me.imillusion.drawmything.DrawPlugin;
import me.imillusion.drawmything.data.DrawPlayer;
import me.imillusion.drawmything.game.Game;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class DrawerMoveHandler implements Listener {

    private final DrawPlugin main;

    public DrawerMoveHandler(DrawPlugin main) {
        this.main = main;
    }

    @EventHandler
    private void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        Game game = main.getGameManager().getPlayerGame(player.getUniqueId());

        if (game == null)
            return;

        DrawPlayer drawer = main.getPlayerManager().get(player);

        if (drawer == null || !drawer.isDrawer() || main.getSettings().getDrawingTime() - game.getArena().getRound().getDrawingTime() <= 1)
            return;

        if (e.getFrom().getX() != e.getTo().getX() || e.getFrom().getY() != e.getTo().getY() || e.getFrom().getZ() != e.getTo().getZ()) {
            Location copy = e.getFrom().clone();
            copy.setPitch(e.getTo().getPitch());
            copy.setYaw(e.getTo().getYaw());
            e.getPlayer().teleport(copy);
        }
    }
}
