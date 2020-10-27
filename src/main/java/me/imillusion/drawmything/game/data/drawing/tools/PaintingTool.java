package me.imillusion.drawmything.game.data.drawing.tools;

import me.imillusion.drawmything.DrawPlugin;
import me.imillusion.drawmything.game.canvas.Canvas;
import me.imillusion.drawmything.game.canvas.Point;
import org.bukkit.DyeColor;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public abstract class PaintingTool {

    protected DrawPlugin main;
    private int slot;
    private ItemStack item;
    private String identifier;
    private Set<Action> clickActions;

    private boolean useConstructor = false;

    public PaintingTool() {
        //For API usage, must override all getters
    }

    public PaintingTool(int slot, ItemStack item, String identifier, Set<Action> actions) {
        this(null, slot, item, identifier, actions);
    }

    public PaintingTool(DrawPlugin main, int slot, ItemStack item, String identifier, Set<Action> actions) {
        this.main = main;
        this.slot = slot;
        this.item = item;
        this.identifier = identifier;
        this.clickActions = actions;

        this.useConstructor = true;
    }

    public abstract void apply(Canvas canvas, Point point, DyeColor color);

    public void applyMove(Canvas canvas, Point point, DyeColor color) {
    }

    public int getSlot() {
        if (!useConstructor)
            throw new UnsupportedOperationException("Usage of blank constructor with default method.");
        return slot;
    }

    public ItemStack getItem() {
        if (!useConstructor)
            throw new UnsupportedOperationException("Usage of blank constructor with default method.");
        return item;
    }

    public String getIdentifier() {
        if (!useConstructor)
            throw new UnsupportedOperationException("Usage of blank constructor with default method.");
        return identifier;
    }

    public Set<Action> getClickActions() {
        if (!useConstructor)
            throw new UnsupportedOperationException("Usage of blank constructor with default method.");
        return clickActions;
    }
}
