package me.imillusion.drawmything.files;

import me.imillusion.drawmything.DrawPlugin;
import me.imillusion.drawmything.game.arena.Arena;
import me.imillusion.drawmything.game.arena.ArenaMap;
import me.imillusion.drawmything.game.data.drawing.colors.ColorSelectionArea;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MapsFile extends YMLBase {

    public MapsFile(DrawPlugin plugin) {
        super(plugin, new File(plugin.getDataFolder(), "maps.yml"), true);
    }

    public List<ArenaMap> load()
    {
        List<ArenaMap> maps = new ArrayList<>();

        for(String id : getConfiguration().getConfigurationSection("maps").getKeys(false))
        {
            ConfigurationSection section = getConfiguration().getConfigurationSection("maps." + id);

            World world = Bukkit.getWorld(section.getString("world"));

            ConfigurationSection color = section.getConfigurationSection("colors");
            List<ColorSelectionArea> colorAreas = new ArrayList<>();

            for(String col : color.getKeys(false))
            {
                colorAreas.addAll(
                        ColorSelectionArea.createAreas(
                                getLocation(world, color.getConfigurationSection(col + ".topleft")),
                                getLocation(world, color.getConfigurationSection(col + ".bottomright"))));
            }

            Location spawn = getLocation(world, section.getConfigurationSection("spawn"));
            Location drawer = getLocation(world, section.getConfigurationSection("drawer"));

            Location topLeft = getLocation(world, section.getConfigurationSection("canvas.topleft"));
            Location bottomRight = getLocation(world, section.getConfigurationSection("canvas.bottomright"));

            maps.add(new ArenaMap(topLeft, bottomRight, spawn, drawer, colorAreas));
        }

        return maps;
    }

    private Location getLocation(World world, ConfigurationSection section)
    {
        return new Location(
                world,
                section.getDouble("x"),
                section.getDouble("y"),
                section.getDouble("z"),
                (float) section.getDouble("yaw", 0),
                (float) section.getDouble("pitch", 0)
        );
    }
}
