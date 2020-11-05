package me.imillusion.drawmything.game.data.drawing.tools;

import me.imillusion.drawmything.DrawPlugin;
import me.imillusion.drawmything.data.DrawPlayer;
import me.imillusion.drawmything.game.arena.Arena;
import me.imillusion.drawmything.game.canvas.Canvas;
import me.imillusion.drawmything.game.canvas.Point;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class BrushTool extends PaintingTool {

    private final DrawPlugin main;

    public BrushTool(DrawPlugin main, int slot, ItemStack item, String identifier, Set<Action> actions) {
        super(main, slot, item, identifier, actions);
        this.main = main;
    }

    @Override
    public void applyMove(Canvas canvas, Point point, DyeColor color) {
        Arena arena = canvas.getArena();
        DrawPlayer drawer = arena.getRound().getDrawer();

        int distance = drawer.getBrushSize();
        Point lastPoint = drawer.getLastPoint();

        if (drawer.getTicksleft() != 0) {
            if (drawer.getLastPoint().equals(point))
                return;

            canvas.drawPixels(
                    drawer.getSelectedColor(),
                    getPointsBetween(
                            canvas,
                            canvas.adaptLocation(lastPoint),
                            canvas.adaptLocation(point),
                            distance));
            drawer.setLastPoint(point);
        }
    }

    @Override
    public void apply(Canvas canvas, Point point, DyeColor color) {
        Arena arena = canvas.getArena();
        DrawPlayer drawer = arena.getRound().getDrawer();

        int distance = drawer.getBrushSize();
        Point lastPoint = drawer.getLastPoint();

        if (drawer.getTicksleft() != 0) {
            if (!lastPoint.equals(point))
                canvas.drawPixels(
                        drawer.getSelectedColor(),
                        getPointsBetween(
                                canvas,
                                canvas.adaptLocation(lastPoint),
                                canvas.adaptLocation(point),
                                distance));

            drawer.setTicksleft(main.getSettings().getDrawingLineTicks());
            drawer.setLastPoint(point);
            return;
        }

        canvas.drawPixels(drawer.getSelectedColor(),
                getBrushPoints(
                        point,
                        distance,
                        canvas));
        drawer.setTicksleft(main.getSettings().getDrawingLineTicks());
        drawer.setLastPoint(point);

    }

    private Point[] getBrushPoints(Point origin, int distance, Canvas canvas) {
        if (distance == 1)
            return new Point[]{origin};

        List<Point> points = new ArrayList<>();
        int difference = distance >> 1;

        for (int x = 0; x <= distance; x++)
            for (int y = 0; y <= distance; y++) {
                boolean xAllow = !(x == 0 || x == distance);
                boolean yAllow = !(y == 0 || y == distance);

                if (!xAllow && !yAllow)
                    continue;

                Point foundPoint = canvas.getRelative(origin, x - difference, y - difference);

                if (foundPoint == null || foundPoint.getColor() == origin.getColor())
                    continue;

                points.add(foundPoint);
            }

        return points.toArray(new Point[]{});
    }

    private Point[] getPointsBetween(Canvas canvas, Location one, Location two, int distance) {
        Vector vectorOne = one.toVector().clone();
        Vector vectorTwo = two.toVector().clone().subtract(vectorOne);

        List<Point> list = new ArrayList<>();

        BlockIterator iterator = new BlockIterator(one.getWorld(), vectorOne, vectorTwo, 0, (int) one.distance(two));

        while (iterator.hasNext()) {
            Block block = iterator.next();
            if (canvas.adaptPoint(block.getLocation()) != null)
                list.add(canvas.adaptPoint(block.getLocation()));
        }

        for (Point point : list)
            Stream.of(getBrushPoints(point, distance, canvas)).filter(p -> !list.contains(p))
                    .forEach(list::add);

        return list.toArray(new Point[]{});
    }
}
