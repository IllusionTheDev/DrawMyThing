package me.imillusion.drawmything.game;

import lombok.Getter;
import lombok.Setter;
import me.imillusion.drawmything.DrawPlugin;
import me.imillusion.drawmything.events.GameCleanupEvent;
import me.imillusion.drawmything.events.GameStartEvent;
import me.imillusion.drawmything.game.arena.Arena;
import me.imillusion.drawmything.game.arena.ArenaMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.UUID;

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

    public void start()
    {
        gameState = GameState.IN_GAME;
        arena.getRound().reset();
        arena.sendScoreboard();

        main.getTitles().playTitle("game-start", arena.getPlayers().toArray(new Player[]{}));

        new GameStartEvent(this);
    }

    public void end()
    {
        /*List<Pair<UUID, Integer>> sortedPoints = new ArrayList<>();

        for (Player p : arena.getPlayers()) //not the best thing but I need to populate the list
        {
            DrawPlayer drawPlayer = main.getPlayerManager().get(p);
            sortedPoints.add(new Pair<>(drawPlayer.getUuid(), drawPlayer.getPoints()));
        }

        sortedPoints.sort(Comparator.comparingInt(Pair::getValue));

        List<String> messages = new ArrayList<>();
        messages.add("&8&m-----------------------");
        messages.add(" ");
        messages.add("  &6" + getName(sortedPoints.get(sortedPoints.size() - 1).getKey()) + " &7 won with &6" + sortedPoints.get(sortedPoints.size() - 1).getValue() + " &7points.");
        messages.add(" ");

        int pos = 1;
        for (int i = ((sortedPoints.size() < 5 ? sortedPoints.size() : 5) - 1); i >= 0; i--) //inverse change since this sorte
        {
            UUID uuid = sortedPoints.get(i).getKey();
            messages.add(" " + color(pos, sortedPoints.size()) + pos++ + ") - " + getName(uuid) + " &7with &6" + sortedPoints.get(i).getValue() + " &7points.");
        }

        messages.add(" ");
        messages.add(" &7Your position: &a#%position%");
        messages.add(" ");
        messages.add("&8&m-----------------------");

        for (Player p : arena.getPlayers()) {
            for (String m : messages)
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', m
                        .replace("%position%", String.valueOf(getPosition(p.getUniqueId(), sortedPoints)))));
        }

        */
        Bukkit.getScheduler().scheduleSyncDelayedTask(main, this::cleanup, 200L);
    }

    private int getPosition(UUID uuid)
    {
        return main.getPlayerManager().get(uuid).getPosition();
    }

    private String color(int position, int size)
    {
        if (position == 1)
            return "&a&l";
        if (position == 2)
            return "&e&l";

        return size > position ? "&e&l" : "&c&l";

    }

    private String getName(UUID uuid)
    {
        return Bukkit.getOfflinePlayer(uuid).getName();
    }

    private void cleanup()
    {
        if (gameState != GameState.FINISHED || arena == null)
            return;

        new GameCleanupEvent(this);
        Set<Player> players = arena.getPlayers();
        players.forEach(main::sendToLobby);
        gameState = GameState.PREGAME;
        arena = null;
        main.getGameManager().unregisterGame(this);
    }
}
