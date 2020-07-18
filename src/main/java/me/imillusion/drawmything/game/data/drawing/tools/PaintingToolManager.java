package me.imillusion.drawmything.game.data.drawing.tools;

import lombok.Getter;
import me.imillusion.drawmything.DrawPlugin;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PaintingToolManager {

    @Getter
    private List<PaintingTool> registeredTools = new ArrayList<>();

    public PaintingToolManager(DrawPlugin main)
    {
        registeredTools.add(new BrushTool(main));
        registeredTools.add(new FillTool());
        registeredTools.add(new BlankPageTool());
    }

    public void registerTool(PaintingTool tool)
    {
        registeredTools.add(tool);
    }

    /**
     * Gets a PaintingTool by item, can be null.
     *
     * @param item - The item to get from
     * @return - The PaintingTool found, if not null
     */
    public PaintingTool getToolByItem(ItemStack item)
    {
        return registeredTools.stream()
                .filter(tool -> tool.getItem().equals(item))
                .findFirst()
                .orElse(null);
    }
}
