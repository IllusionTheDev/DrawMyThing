package me.imillusion.drawmything.utils.scoreboard;

import me.clip.placeholderapi.PlaceholderAPI;
import me.imillusion.drawmything.DrawPlugin;
import me.imillusion.drawmything.utils.Pair;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ScoreboardTemplate {

    private Map<String, ScoreboardAnimation> animations;
    private List<String> text;
    private String title;

    private int taskid;

    public ScoreboardTemplate(DrawPlugin main, String title, int updateTime, Map<String, ScoreboardAnimation> animations, List<String> text)
    {
        this.animations = animations;
        this.text = text;
        this.title = ChatColor.translateAlternateColorCodes('&', title);

        if(updateTime > 0)
        taskid = Bukkit.getScheduler().scheduleSyncRepeatingTask(main, this::update, 0L, updateTime);
    }

    private void update()
    {
        animations.forEach((string, animation) -> animation.tick());
    }

    public void render(Player player, TeamsScoreboard board)
    {
        render(player, board, (Pair<String, String>[]) null);
    }

    @SafeVarargs
    public final void render(Player player, TeamsScoreboard board, Pair<String, String>... placeholders)
    {
        List<String> copy = new ArrayList<>(text);

        for(String s : animations.keySet())
            if(title.contains(s))
                board.title(title.replaceAll(s, PlaceholderAPI.setPlaceholders(player, setPlaceholders(animations.get(s).getCurrentText(), placeholders))));
            else
                board.title(PlaceholderAPI.setPlaceholders(player, setPlaceholders(title, placeholders)));

        for(int i = 0; i < copy.size(); i++)
        {
            String line = copy.get(i);
            for(String s : animations.keySet())
                if(line.contains(s))
                    line = line.replaceAll(s, setPlaceholders(animations.get(s).getCurrentText(), placeholders));
            copy.set(i, PlaceholderAPI.setPlaceholders(player, setPlaceholders(line, placeholders)));
        }

        board.write(copy);
    }

    @SafeVarargs
    private final String setPlaceholders(String input, Pair<String, String>... placeholders)
    {
        for (Pair<String, String> placeholder : placeholders) {
            input = input.replace(placeholder.getKey(), placeholder.getValue());
        }

        return input;
    }

    public void dispose()
    {
        Bukkit.getScheduler().cancelTask(taskid);
        title = null;
        text = null;
        animations.clear();
        animations = null;
    }



}
