package me.imillusion.drawmything.scoreboard;

import me.clip.placeholderapi.PlaceholderAPI;
import me.imillusion.drawmything.DrawPlugin;
import me.imillusion.drawmything.utils.Pair;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScoreboardTemplate {

    private final Map<String, ScoreboardAnimation> animations;
    private final DrawPlugin main;
    private List<String> text;
    private String title;
    private int taskid;

    public ScoreboardTemplate(DrawPlugin main, String title, int updateTime, Map<String, ScoreboardAnimation> animations, List<String> text)
    {
        this.main = main;
        this.animations = new HashMap<>();

        animations.forEach((key, value) -> this.animations.put(key, value.clone()));
        this.text = text;
        this.title = ChatColor.translateAlternateColorCodes('&', title);

        if (updateTime > 0)
            taskid = Bukkit.getScheduler().scheduleSyncRepeatingTask(main, this::update, updateTime, updateTime);
    }

    private void update()
    {
        animations.forEach((string, animation) -> animation.tick());
        main.getPlayerManager().getAllPlayers().stream()
                .filter(player -> player.getCurrentTemplate() != null && player.getCurrentTemplate().equals(this))
                .forEach(player -> render(player.getPlayer(), player.getScoreboard(), player.getLastScoreboardPlaceholders()));
    }

    @SafeVarargs
    public final void render(Player player, TeamsScoreboard board, Pair<String, String>... placeholders)
    {
        List<String> copy = new ArrayList<>(text);

        main.getPlayerManager().get(player).setLastScoreboardPlaceholders(placeholders);

        if (animations.isEmpty())
            board.title(setPlaceholders(player, title, placeholders));
        else {
            String titleCopy = title;

            for (Map.Entry<String, ScoreboardAnimation> entry : animations.entrySet()) {
                String s = entry.getKey();
                ScoreboardAnimation val = entry.getValue();
                if (title.contains(s))
                    titleCopy = titleCopy.replaceAll(s, setPlaceholders(player, val.getCurrentText(), placeholders));
                else
                    titleCopy = setPlaceholders(player, titleCopy, placeholders);
            }

            board.title(titleCopy);
        }


        for (int i = 0; i < copy.size(); i++) {
            String line = copy.get(i);

            for (Map.Entry<String, ScoreboardAnimation> entry : animations.entrySet()) {
                String s = entry.getKey();
                ScoreboardAnimation val = entry.getValue();
                if (line.contains(s))
                    line = line.replaceAll(s, setPlaceholders(player, val.getCurrentText(), placeholders));
            }

            copy.set(i, setPlaceholders(player, line, placeholders));
        }

        board.write(copy);
    }

    @SafeVarargs
    private final String setPlaceholders(Player player, String input, Pair<String, String>... placeholders)
    {
        if (placeholders == null)
            return input;

        String val = input;

        for (Pair<String, String> placeholder : placeholders) {
            val = val.replace(placeholder.getKey(), placeholder.getValue());
        }

        if (DrawPlugin.hookPlaceholders())
            val = PlaceholderAPI.setPlaceholders(player, val);

        return val;
    }

    public void dispose()
    {
        Bukkit.getScheduler().cancelTask(taskid);
        title = null;
        text = null;
        animations.clear();
    }


}
