package me.imillusion.drawmything.game.arena;

import lombok.Getter;
import me.imillusion.drawmything.DrawPlugin;
import me.imillusion.drawmything.game.Game;
import me.imillusion.drawmything.game.canvas.Canvas;
import me.imillusion.drawmything.game.data.Round;
import me.imillusion.drawmything.utils.scoreboard.TeamsScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class Arena {

    @Getter
    private Map<UUID, Integer> points = new HashMap<>();
    @Getter
    private Map<UUID, TeamsScoreboard> boards = new HashMap<>();

    @Getter
    private Round round;

    @Getter
    private ArenaMap map;

    @Getter
    private Canvas canvas;

    @Getter
    private DrawPlugin main;

    @Getter
    private Game game;

    /**
     * Gets the players currently in the arena
     *
     * @return - All the players currently in the arena
     */
    public Set<Player> getPlayers()
    {
        return points.keySet().stream().map(Bukkit::getPlayer).collect(Collectors.toSet());
    }

    public Set<UUID> getUUIDs()
    {
        return points.keySet();
    }

    public boolean belongs(UUID uuid)
    {
        return points.containsKey(uuid);
    }

    public void addPlayer(UUID uuid)
    {
        Player p = Bukkit.getPlayer(uuid);

        canvas.renderCanvas(p);

        setPoints(uuid, 0);
        round.addPlayer(uuid);

        getPlayers().forEach(player -> {
            main.getHider().showEntity(p, player);
            if(!main.getHidingHandler().isHiding(p))
                main.getHider().showEntity(player, p);
        });

        TeamsScoreboard countdownboard = new TeamsScoreboard();

        countdownboard.title("&a&lDrawMyThing");
        countdownboard.line(9, "&8&m------------------------", 9);
        countdownboard.line(8, "", 8);
        countdownboard.line(7, " &6Welcome, &b" + p.getName(), 7);
        countdownboard.line(6, " &6Awaiting for more players..", 6);
        countdownboard.line(5, "", 5);
        countdownboard.line(4, "        &7play.pteromc.com", 4);
        countdownboard.line(3, "&8&m------------------------", 3);

        p.setScoreboard(countdownboard.getBoard());
        boards.put(uuid, countdownboard);
    }

    public void removePlayer(UUID uuid)
    {
        points.remove(uuid);
        round.removePlayer(uuid);
        sendScoreboard();
    }

    public int getPoints(UUID uuid)
    {
        return points.get(uuid);
    }

    public void setPoints(UUID uuid, int points)
    {
        this.points.putIfAbsent(uuid, 0);
        this.points.replace(uuid, points);

        if(game.isStarted())
            sendScoreboard();
    }

    public void sendScoreboard()
    {
        List<Map.Entry<UUID, Integer>> sortedPoints = this.points.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toList());

        List<String> base = Arrays.asList(
                "&8&m------------------------",
                "");

        for(Player p : getPlayers())
        {
            List<String> copy = new ArrayList<>(base);

            for(int i = ((sortedPoints.size() < 5 ? sortedPoints.size() : 5) - 1); i >= 0; i--) //inverse change since this sorte
            {
                UUID uuid = sortedPoints.get(i).getKey();
                Player pl = Bukkit.getPlayer(uuid);

                copy.add((uuid.equals(p.getUniqueId()) ? " &a" : " &c") + pl.getName() + "&7: &6" + sortedPoints.get(i).getValue());
            }

            copy.add(" ");
            copy.add(" &aYour Points: &e" + points.get(p.getUniqueId()));
            copy.add(" ");
            copy.add("        &7play.pteromc.com");
            copy.add("&8&m------------------------");

            boards.get(p.getUniqueId()).write(copy);
        }
    }

    public Arena(DrawPlugin main, ArenaMap map, Game game)
    {
        this.main = main;
        this.map = map;
        this.game = game;

        canvas = new Canvas(map, this);
        round = new Round(main, this, new ArrayList<>(points.keySet()));
    }
}
