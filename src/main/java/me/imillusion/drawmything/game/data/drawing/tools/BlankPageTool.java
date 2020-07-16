package me.imillusion.drawmything.game.data.drawing.tools;

import me.imillusion.drawmything.game.canvas.Canvas;
import me.imillusion.drawmything.game.canvas.Point;
import me.imillusion.drawmything.utils.ItemBuilder;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class BlankPageTool implements PaintingTool {

    private final ItemStack item;

    public BlankPageTool() {
        this.item = new ItemBuilder(Material.PAPER)
                .name("&a&lBlank Page &8(Right click)")
                .build();
    }

    @Override
    public void apply(Canvas canvas, Point point, DyeColor color) {
        canvas.clear();
    }

    @Override
    public int getSlot() {
        return 8;
    }

    @Override
    public ItemStack getItem() {
        return item;
    }
}
