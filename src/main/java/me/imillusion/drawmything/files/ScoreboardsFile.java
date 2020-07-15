package me.imillusion.drawmything.files;

import lombok.Getter;
import me.imillusion.drawmything.DrawPlugin;
import me.imillusion.drawmything.scoreboard.ScoreboardAnimation;
import me.imillusion.drawmything.scoreboard.ScoreboardTemplate;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScoreboardsFile extends YMLBase {

    private Map<String, ScoreboardAnimation> animations = new HashMap<>();

    private DrawPlugin main;

    @Getter
    private ScoreboardTemplate awaitingBoard;

    public ScoreboardsFile(DrawPlugin plugin) {
        super(plugin, new File(plugin.getDataFolder(), "scoreboards.yml"), true);

        this.main = plugin;

        getConfiguration().getConfigurationSection("animations").getKeys(false)
                .forEach(s -> animations.put("%animation_" + s + "%",
                        new ScoreboardAnimation(getConfiguration().getStringList("animations." + s))));

        awaitingBoard = getTemplate("awaiting-players");
    }

    private ScoreboardTemplate getTemplate(String name)
    {
        String title = getConfiguration().getString(name + ".title");
        int updateTicks = getConfiguration().getInt(name + ".update-every");
        List<String> text = getConfiguration().getStringList(name + ".text");

        return new ScoreboardTemplate(main, title, updateTicks, animations, text);
    }


}
