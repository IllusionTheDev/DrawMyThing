package me.imillusion.drawmything.pregame;

import me.imillusion.drawmything.DrawPlugin;
import me.imillusion.drawmything.game.Game;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinHandler implements Listener {

    private DrawPlugin main;

    public JoinHandler(DrawPlugin main) {
        this.main = main;
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent e)
    {
        if(!e.getPlayer().isOp())
        {
            e.getPlayer().setGameMode(GameMode.ADVENTURE);
            e.getPlayer().getInventory().clear();
        }

        e.getPlayer().getInventory().setItem(8, main.getHidingHandler().getInactiveItem());

        Game game = main.getGameManager().getFirstAvailableGame();

        game.getArena().addPlayer(e.getPlayer().getUniqueId());
        e.getPlayer().teleport(game.getArena().getMap().getSpawnLocation());
        e.getPlayer().setWalkSpeed(0.2f);

        if(game.getArena().getPlayers().size() >= main.getSettings().getMinplayers() && !game.isActiveCountdown())
            main.getGameCountdown().start(game);

        e.setJoinMessage("");

        game.getArena().getPlayers().forEach(player -> player.sendMessage(
                ChatColor.translateAlternateColorCodes('&',
                main.getMessages().getMessage("player-join")
                        .replace("%prefix%", main.getMessages().getPrefix())
                        .replace("%player%", e.getPlayer().getName())
                        .replace("%count%", String.valueOf(game.getArena().getUUIDs().size())))));
    }
}
