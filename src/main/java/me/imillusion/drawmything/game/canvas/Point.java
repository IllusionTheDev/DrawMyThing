package me.imillusion.drawmything.game.canvas;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.DyeColor;

@Getter
@Setter
@AllArgsConstructor
public class Point {

    private int x;
    private int y;

    private DyeColor color;

    public Point(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
}
