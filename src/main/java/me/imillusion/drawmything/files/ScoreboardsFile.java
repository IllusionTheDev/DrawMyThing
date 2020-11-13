package me.imillusion.drawmything.files;

import lombok.Getter;
import me.imillusion.drawmything.DrawPlugin;
import me.imillusion.drawmything.scoreboard.ScoreboardAnimation;
import me.imillusion.drawmything.scoreboard.ScoreboardTemplate;
import me.imillusion.drawmything.utils.Pair;
import me.imillusion.drawmything.utils.SimplePlaceholder;
import org.bukkit.ChatColor;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScoreboardsFile extends YMLBase {

    private final Map<String, ScoreboardAnimation> animations = new HashMap<>();
    private final Map<Character, Pair<Integer, Integer>> countdownColors = new HashMap<>();

    private final DrawPlugin main;

    @Getter
    private ScoreboardTemplate awaitingBoard;
    @Getter
    private ScoreboardTemplate countdownBoard;
    @Getter
    private ScoreboardTemplate ingameBoard;

    public ScoreboardsFile(DrawPlugin plugin) {
        super(plugin, new File(plugin.getDataFolder(), "scoreboards.yml"), true);

        this.main = plugin;

        getConfiguration().getConfigurationSection("animations").getKeys(false)
                .forEach(s -> animations.put("%animation_" + s + "%",
                        new ScoreboardAnimation(getConfiguration().getStringList("animations." + s))));

        getConfiguration().getConfigurationSection("countdown-color-range").getKeys(false)
                .forEach(s -> countdownColors.put(s.charAt(0), new Pair<Integer, Integer>(getConfiguration().getInt("countdown-color-range." + s + ".from"), getConfiguration().getInt("countdown-color-range." + s + ".to"))));

        awaitingBoard = getTemplate("awaiting-players");
        countdownBoard = getTemplate("pregame-countdown");
        ingameBoard = getTemplate("in-game");
    }

    private ScoreboardTemplate getTemplate(String name) {
        String title = getConfiguration().getString(name + ".title");
        int updateTicks = getConfiguration().getInt(name + ".update-every");
        List<String> text = getConfiguration().getStringList(name + ".text");

        return new ScoreboardTemplate(main, title, updateTicks, animations, text);
    }

    public SimplePlaceholder obtainSecondsPlaceholder(int seconds) {
        StringBuilder color = new StringBuilder();

        countdownColors.forEach((key, val) ->
        {
            if (seconds <= val.getKey() && seconds >= val.getValue())
                color.append("&").append(key);
        });

        System.out.println((color.toString() + seconds));
        return new SimplePlaceholder("%seconds%", "" + ChatColor.translateAlternateColorCodes('&', color.append(seconds).toString()));
    }

    public void dispose() {
        animations.values().forEach(ScoreboardAnimation::dispose);
        awaitingBoard.dispose();
        countdownBoard.dispose();
        ingameBoard.dispose();
    }

}
