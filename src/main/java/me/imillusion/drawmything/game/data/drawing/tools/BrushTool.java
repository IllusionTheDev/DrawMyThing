package me.imillusion.drawmything.game.data.drawing.tools;

import me.imillusion.drawmything.DrawPlugin;
import me.imillusion.drawmything.data.DrawPlayer;
import me.imillusion.drawmything.game.arena.Arena;
import me.imillusion.drawmything.game.canvas.Canvas;
import me.imillusion.drawmything.game.canvas.Point;
import me.imillusion.drawmything.utils.ItemBuilder;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class BrushTool implements PaintingTool {

    private DrawPlugin main;
    private ItemStack item;

    BrushTool(DrawPlugin main) {
        this.main = main;
        item = new ItemBuilder(Material.WOOD_SPADE)
                .name("&a&lBrush &8(Right Click)")
                .build();
    }

    @Override
    public void applyMove(Canvas canvas, Point point, DyeColor color) {
        Arena arena = canvas.getArena();
        DrawPlayer drawer = arena.getRound().getDrawer();

        if (drawer.getTicksleft() != 0) {
            if (drawer.getLastPoint().equals(point))
                return;

            arena.getCanvas().drawPixels(drawer.getSelectedColor(),
                    getPointsBetween(arena.getCanvas(),
                            canvas.adaptLocation(drawer.getLastPoint()), canvas.adaptLocation(point)));
            drawer.setLastPoint(point);
        }
    }

    @Override
    public void apply(Canvas canvas, Point point, DyeColor color) {
        Arena arena = canvas.getArena();
        DrawPlayer drawer = arena.getRound().getDrawer();

        if (drawer.getTicksleft() != 0) {
            if (!drawer.getLastPoint().equals(point))
                arena.getCanvas().drawPixels(drawer.getSelectedColor(),
                        getPointsBetween(arena.getCanvas(), canvas.adaptLocation(drawer.getLastPoint()), canvas.adaptLocation(point)));
        } else
            arena.getCanvas().drawPizel(point, drawer.getSelectedColor());


        drawer.setTicksleft(main.getSettings().getDrawingLineTicks());
        drawer.setLastPoint(point);

    }

    @Override
    public int getSlot() {
        return 1;
    }

    @Override
    public ItemStack getItem() {
        return item;
    }

    private Point[] getPointsBetween(Canvas canvas, Location one, Location two)
    {
        Vector vectorOne = one.toVector().clone();
        Vector vectorTwo = two.toVector().clone().subtract(vectorOne);

        List<Point> list = new ArrayList<>();

        BlockIterator iterator = new BlockIterator(one.getWorld(), vectorOne, vectorTwo, 0, (int) one.distance(two));

        while (iterator.hasNext()) {
            Block block = iterator.next();
            if (canvas.adaptPoint(block.getLocation()) != null)
                list.add(canvas.adaptPoint(block.getLocation()));
        }

        return list.toArray(new Point[]{});
    }
}
