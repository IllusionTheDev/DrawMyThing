package me.imillusion.drawmything.files;

import lombok.Getter;
import me.imillusion.drawmything.gui.configuration.Path;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Getter
public class SettingsFile extends YMLBase {

    @Path(path = "minimum-players")
    private int minplayers;
    @Path(path = "maximum-players")
    private int maxPlayers;

    @Path(path = "drawing-time")
    private int drawingTime;
    @Path(path = "drawing-line-ticks")
    private int drawingLineTicks;

    private final Map<Integer, Integer> startTimes = new HashMap<>();

    private String bungeeLobby;

    public SettingsFile(JavaPlugin plugin) {
        super(plugin, new File(plugin.getDataFolder(), "settings.yml"), true);

        minplayers = getConfiguration().getInt("minimum-players", 2);
        maxPlayers = getConfiguration().getInt("maximum-players", 16);
        drawingTime = getConfiguration().getInt("drawing-time");
        drawingLineTicks = getConfiguration().getInt("drawing-line-ticks", 5);

        for (String key : getConfiguration().getConfigurationSection("start-time").getKeys(false))
            startTimes.put(Integer.valueOf(key), getConfiguration().getInt("start-time." + key));

        bungeeLobby = getConfiguration().getString("bungee-lobby");
    }


}
