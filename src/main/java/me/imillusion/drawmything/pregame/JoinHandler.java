package me.imillusion.drawmything.pregame;

import me.imillusion.drawmything.DrawPlugin;
import me.imillusion.drawmything.data.DrawPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinHandler implements Listener {

    private final DrawPlugin main;

    public JoinHandler(DrawPlugin main) {
        this.main = main;
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent e) {
        DrawPlayer drawPlayer = new DrawPlayer(main, e.getPlayer().getUniqueId());

        e.setJoinMessage("");
        drawPlayer.setup();
    }
}
