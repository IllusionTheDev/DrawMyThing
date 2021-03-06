package me.imillusion.drawmything.game.data.drawing.tools;

import me.imillusion.drawmything.game.canvas.Canvas;
import me.imillusion.drawmything.game.canvas.Point;
import org.bukkit.DyeColor;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;

public class FillTool extends PaintingTool {


    public FillTool(int slot, ItemStack item, String identifier, Set<Action> actions) {
        super(slot, item, identifier, actions);
    }

    /**
     * @param canvas - The canvas where to apply this effect
     * @param point  - The point clicked
     * @param color  - The color to draw with
     */
    @Override
    public void apply(Canvas canvas, Point point, DyeColor color) {
        if (point.getColor().equals(color))
            return;

        Set<Point> points = new HashSet<>();

        points.add(point);
        getPoints(canvas, point, points);

        canvas.drawPixels(color, points.toArray(new Point[]{}));
    }

    /**
     * Recursive function to obtain all the points to fill with
     *
     * @param canvas      - The canvas where to apply
     * @param point       - The point to look for
     * @param foundPoints - A set of points found, avoiding infinite looping
     */
    private void getPoints(Canvas canvas, Point point, Set<Point> foundPoints) {
        Set<Point> points = new HashSet<>();

        for (int x = -1; x <= 1; x++)
            for (int y = -1; y <= 1; y++) {
                if (x != 0 && y != 0 || x == 0 && y == 0)
                    continue;

                Point newPoint = canvas.getRelative(point, x, y);
                if (newPoint != null && !foundPoints.contains(newPoint) && newPoint.getColor().equals(point.getColor()))
                    points.add(newPoint);
            }

        foundPoints.addAll(points);

        for (Point p : points)
            getPoints(canvas, p, foundPoints);
    }
}
