package me.imillusion.drawmything.pregame;

import me.imillusion.drawmything.DrawPlugin;
import me.imillusion.drawmything.data.DrawPlayer;
import me.imillusion.drawmything.game.Game;
import me.imillusion.drawmything.game.GameState;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinHandler implements Listener {

    private final DrawPlugin main;

    public JoinHandler(DrawPlugin main) {
        this.main = main;
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent e)
    {
        DrawPlayer drawPlayer = new DrawPlayer(main, e.getPlayer().getUniqueId());

        if (!e.getPlayer().isOp()) {
            e.getPlayer().setGameMode(GameMode.ADVENTURE);
            e.getPlayer().getInventory().clear();
        }

        e.getPlayer().getInventory().setItem(8, main.getHidingHandler().getInactiveItem());
        e.getPlayer().getActivePotionEffects().forEach(effect -> e.getPlayer().removePotionEffect(effect.getType()));

        Game game = main.getGameManager().getFirstAvailableGame();
        drawPlayer.setCurrentGame(game);

        game.getArena().addPlayer(e.getPlayer().getUniqueId());
        e.getPlayer().teleport(game.getArena().getMap().getSpawnLocation());
        e.getPlayer().setWalkSpeed(0.2f);

        if (game.getArena().getPlayers().size() >= main.getSettings().getMinplayers() && game.getGameState() != GameState.COUNTDOWN)
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
