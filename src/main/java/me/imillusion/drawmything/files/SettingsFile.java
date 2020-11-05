package me.imillusion.drawmything.files;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Getter
public class SettingsFile extends YMLBase {

    private int rounds;

    private int minplayers;
    private int maxPlayers;

    private int drawingTime;
    private int drawingLineTicks;

    private String formula;

    private final Map<Integer, Integer> startTimes = new HashMap<>();

    private String bungeeLobby;

    public SettingsFile(JavaPlugin plugin) {
        super(plugin, new File(plugin.getDataFolder(), "settings.yml"), true);

        rounds = getConfiguration().getInt("rounds", 3);
        minplayers = getConfiguration().getInt("minimum-players", 2);
        maxPlayers = getConfiguration().getInt("maximum-players", 16);
        drawingTime = getConfiguration().getInt("drawing-time");
        drawingLineTicks = getConfiguration().getInt("drawing-line-ticks", 5);
        formula = getConfiguration().getString("points-formula");

        for (String key : getConfiguration().getConfigurationSection("start-time").getKeys(false))
            startTimes.put(Integer.valueOf(key), getConfiguration().getInt("start-time." + key));

        bungeeLobby = getConfiguration().getString("bungee-lobby");
    }


}
