package me.imillusion.drawmything.game.data.drawing.tools;

import me.imillusion.drawmything.game.canvas.Canvas;
import me.imillusion.drawmything.game.canvas.Point;
import org.bukkit.DyeColor;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public class BlankPageTool extends PaintingTool {

    public BlankPageTool(int slot, ItemStack item, String identifier, Set<Action> actions) {
        super(slot, item, identifier, actions);
    }

    @Override
    public void apply(Canvas canvas, Point point, DyeColor color) {
        canvas.clear();
    }

}
