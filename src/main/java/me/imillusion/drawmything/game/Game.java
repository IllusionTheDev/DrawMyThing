package me.imillusion.drawmything.game;

import lombok.Getter;
import lombok.Setter;
import me.imillusion.drawmything.DrawPlugin;
import me.imillusion.drawmything.data.DrawPlayer;
import me.imillusion.drawmything.events.game.GameCleanupEvent;
import me.imillusion.drawmything.events.game.GameStartEvent;
import me.imillusion.drawmything.events.game.GameWinEvent;
import me.imillusion.drawmything.game.arena.Arena;
import me.imillusion.drawmything.game.arena.ArenaMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.Set;

public class Game {

    private final DrawPlugin main;
    @Getter
    private Arena arena;
    @Getter
    @Setter
    private GameState gameState;


    Game(DrawPlugin main, ArenaMap arenaMap) {
        this.main = main;
        arena = new Arena(main, arenaMap, this);
        main.getGameManager().registerGame(this);
    }

    public void start() {
        gameState = GameState.IN_GAME;
        arena.getRound().reset();
        arena.sendScoreboard();

        main.getTitles().playTitle("game-start", arena.getPlayersArray());

        new GameStartEvent(this);
    }

    public void end() {
        new GameWinEvent(this, getWinningPlayer());

        Bukkit.getScheduler().scheduleSyncDelayedTask(main, this::cleanup, 200L);
    }

    private DrawPlayer getWinningPlayer() {
        return arena
                .getPlayers()
                .stream()
                .map(main.getPlayerManager()::get)
                .min(Comparator.comparingInt(DrawPlayer::getPoints))
                .orElse(null);
    }

    private void cleanup() {
        if (gameState != GameState.FINISHED || arena == null)
            return;

        new GameCleanupEvent(this);
        Set<Player> players = arena.getPlayers();
        players.forEach(player -> main.getAction().apply(main, player));
        gameState = GameState.PREGAME;
        arena = null;
        main.getGameManager().unregisterGame(this);
    }
}
