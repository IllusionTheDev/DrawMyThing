package me.imillusion.drawmything.game;

import lombok.Getter;
import me.imillusion.drawmything.DrawPlugin;
import me.imillusion.drawmything.game.arena.ArenaMap;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class GameManager {

    private List<ArenaMap> gameMaps;
    private DrawPlugin main;
    @Getter
    private List<Game> activeGames = new ArrayList<>();
    private Random random = new Random();
    public GameManager(DrawPlugin main, List<ArenaMap> gameMaps) {
        this.main = main;
        this.gameMaps = gameMaps;

        Bukkit.getScheduler().scheduleSyncRepeatingTask(main, () -> activeGames.forEach(game -> {
            if (game.isStarted()) {
                game.getArena().getRound().tick();
            }

        }), 20L, 20L);
    }

    public Game getPlayerGame(UUID uuid)
    {
        return activeGames.parallelStream()
                .filter(game -> game.getArena().belongs(uuid))
                .findFirst()
                .orElse(null);
    }

    public Game getFirstAvailableGame()
    {
        return activeGames.parallelStream()
                .filter(game -> !game.isStarted())
                .filter(game -> game.getArena().getPlayers().size() < main.getSettings().getMaxPlayers())
                .findFirst()
                .orElse(new Game(main, getRandomMap()));
    }

    private ArenaMap getRandomMap()
    {
        return gameMaps.size() == 1 ? gameMaps.get(0) : gameMaps.get(random.nextInt(gameMaps.size()));
    }

    void registerGame(Game game)
    {
        activeGames.add(game);
    }

    void unregisterGame(Game game)
    {
        activeGames.remove(game);
    }


}
