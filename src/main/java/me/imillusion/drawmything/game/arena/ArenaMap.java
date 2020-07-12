package me.imillusion.drawmything.game.arena;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.imillusion.drawmything.game.data.drawing.colors.ColorSelectionArea;
import org.bukkit.Location;

import java.util.List;

@AllArgsConstructor
public class ArenaMap {

    @Getter
    private Location topLeft, bottomRight, spawnLocation, drawLocation;
    @Getter
    private List<ColorSelectionArea> colorSelectionAreas;
}
