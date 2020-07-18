package me.imillusion.drawmything.game;

import me.imillusion.drawmything.DrawPlugin;
import me.imillusion.drawmything.data.DrawPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class GameCountdown {

    private final Map<Game, Integer> countdowns = new HashMap<>();
    private final DrawPlugin main;

    public GameCountdown(DrawPlugin main) {
        this.main = main;

        Bukkit.getScheduler().scheduleSyncRepeatingTask(main, this::tick, 20L, 20L);
    }

    public void start(Game game)
    {
        for (int i = game.getArena().getPlayers().size(); i >= main.getSettings().getMinplayers(); i--) {
            if (main.getSettings().getStartTimes().containsKey(i)) {
                countdowns.put(game, main.getSettings().getStartTimes().get(i));
                game.setActiveCountdown(true);

                for (Player player : game.getArena().getPlayers()) {
                    DrawPlayer dp = main.getPlayerManager().get(player);
                    dp.setCurrentTemplate(main.getScoreboards().getCountdownBoard());
                    dp.getCurrentTemplate().render(player, dp.getScoreboard(), main.getScoreboards().obtainSecondsPlaceholder(main.getSettings().getStartTimes().get(i)));
                }
            }
        }
    }

    private void tick()
    {
        if (countdowns.isEmpty()) //small ram savings
            return;

        for (Game game : new HashMap<>(countdowns).keySet()) //copying map to avoid CME's
        {
            if (main.getSettings().getMinplayers() > game.getArena().getPlayers().size()) {
                stopCountdown(game);
                main.getTitles().playTitle("countdown.not-enough-players",
                        game.getArena().getPlayers().toArray(new Player[]{}));
                continue;
            }

            int time = countdowns.get(game);

            if (--time == 0) {
                game.start();
                stopCountdown(game);
                continue;
            }

            for (int i = game.getArena().getPlayers().size(); i > main.getSettings().getMinplayers(); i--) {
                if (main.getSettings().getStartTimes().containsKey(i) && main.getSettings().getStartTimes().get(i) > time)
                    time = main.getSettings().getStartTimes().get(i);
            }

            for (Player p : game.getArena().getPlayers()) {
                p.setLevel(time);
                DrawPlayer drawPlayer = main.getPlayerManager().get(p);
                //manually updating
                drawPlayer.getCurrentTemplate().render(p, drawPlayer.getScoreboard(), main.getScoreboards().obtainSecondsPlaceholder(time));
                main.getSounds().playSound(p, "countdown." + time + "-seconds-left");
                main.getMessages().sendMessage(p, time + "-seconds-left");
            }

            main.getTitles().playTitle("countdown." + time + "-seconds-left", game.getArena().getPlayers().toArray(new Player[]{}));

            countdowns.replace(game, time);
        }
    }

    public String getTime(Game game)
    {
        int time = countdowns.get(game);

        return main.getScoreboards().obtainSecondsPlaceholder(time).getValue();
    }

    public void stopCountdown(Game game)
    {
        countdowns.remove(game);
        game.getArena().getPlayers().forEach(player -> {
            player.setLevel(0);
            DrawPlayer dp = main.getPlayerManager().get(player);
            dp.setCurrentTemplate(null);
        });
        game.setActiveCountdown(false);
    }
}
