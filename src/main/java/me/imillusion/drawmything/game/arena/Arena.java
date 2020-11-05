package me.imillusion.drawmything.game.arena;

import lombok.Getter;
import me.imillusion.drawmything.DrawPlugin;
import me.imillusion.drawmything.data.DrawPlayer;
import me.imillusion.drawmything.game.Game;
import me.imillusion.drawmything.game.GameState;
import me.imillusion.drawmything.game.canvas.Canvas;
import me.imillusion.drawmything.game.data.Round;
import me.imillusion.drawmything.scoreboard.TeamsScoreboard;
import me.imillusion.drawmything.utils.SimplePlaceholder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class Arena {

    private final Set<UUID> players = new HashSet<>();

    @Getter
    private Round round;

    @Getter
    private ArenaMap map;

    @Getter
    private Canvas canvas;
    private final DrawPlugin main;

    @Getter
    private Game game;

    public Arena(DrawPlugin main, ArenaMap map, Game game) {
        this.main = main;
        this.map = map;
        this.game = game;

        canvas = new Canvas(map, this);
        round = new Round(main, this, new ArrayList<>());
    }

    /**
     * Gets the players currently in the arena
     *
     * @return - All the players currently in the arena
     */
    public Set<Player> getPlayers() {
        return players.stream().map(Bukkit::getPlayer).collect(Collectors.toSet());
    }

    public Player[] getPlayersArray() {
        return getPlayers().toArray(new Player[]{});
    }

    public Set<UUID> getUUIDs() {
        return players;
    }

    public boolean belongs(UUID uuid) {
        return players.contains(uuid);
    }

    public void addPlayer(UUID uuid) {
        Player p = Bukkit.getPlayer(uuid);
        DrawPlayer drawPlayer = main.getPlayerManager().get(uuid);

        canvas.renderCanvas(p);

        setPoints(uuid, 0);
        round.addPlayer(uuid);

        getPlayers().forEach(player -> {
            main.getHider().showEntity(p, player);
            if (!main.getPlayerManager().get(player).isHiding())
                main.getHider().showEntity(player, p);
        });

        TeamsScoreboard board = new TeamsScoreboard(main);
        p.setScoreboard(board.getBoard());
        drawPlayer.setScoreboard(board);
        drawPlayer.setCurrentTemplate(main.getScoreboards().getAwaitingBoard());

        main.getScoreboards().getAwaitingBoard().render(p, board);

    }

    public void removePlayer(UUID uuid) {
        players.remove(uuid);
        round.removePlayer(uuid);
    }

    public int getPoints(UUID uuid) {
        return main.getPlayerManager().get(uuid).getPoints();
    }

    public void setPoints(UUID uuid, int points) {
        players.add(uuid);
        main.getPlayerManager().get(uuid).setPoints(points);

        if (game.getGameState() == GameState.IN_GAME)
            sendScoreboard();
    }

    public void sendScoreboard() {
        String drawer = round.getDrawer() == null ? "" : round.getDrawer().getPlayer().getName();
        int roundNumber = round.getRoundNum();

        for (Player p : getPlayers()) {
            DrawPlayer drawPlayer = main.getPlayerManager().get(p);
            drawPlayer.setCurrentTemplate(main.getScoreboards().getIngameBoard());

            int points = drawPlayer.getPoints();
            int position = drawPlayer.getPosition();

            SimplePlaceholder[] pairs = new SimplePlaceholder[]{
                    new SimplePlaceholder("%points%", String.valueOf(points)),
                    new SimplePlaceholder("%position%", String.valueOf(position)),
                    new SimplePlaceholder("%drawer%", drawer),
                    new SimplePlaceholder("%round%", String.valueOf(roundNumber))
            };

            List<SimplePlaceholder> list = new ArrayList<>(Arrays.asList(pairs));

            if (drawPlayer.getLastScoreboardPlaceholders() != null)
                list.addAll(Arrays.asList(drawPlayer.getLastScoreboardPlaceholders()));

            drawPlayer.getCurrentTemplate().render(p, drawPlayer.getScoreboard(), list.toArray(new SimplePlaceholder[]{}));
        }
    }
}
