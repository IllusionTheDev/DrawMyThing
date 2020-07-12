package me.imillusion.drawmything.game.data.drawing;

import lombok.Getter;
import lombok.Setter;
import me.imillusion.drawmything.game.canvas.Point;
import me.imillusion.drawmything.game.data.drawing.tools.PaintingTool;
import me.imillusion.drawmything.game.data.drawing.tools.PaintingToolManager;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;

import java.util.UUID;

@Getter
public class Drawer {

    private PaintingToolManager paintingToolManager;

    /**
     * The UUID of the drawer
     */
    private UUID uuid;

    /**
     * The current color selected
     */
    @Setter
    private DyeColor selectedColor = DyeColor.WHITE;

    /**
     * The size of the brush
     */
    @Setter
    private int brushSize = 1;

    /**
     * The ticks left during drawing, used to help with straight lines
     */
    @Setter
    private int ticksleft = 0;

    /**
     * The current word to draw with
     */
    private String word;

    @Setter
    private Point lastPoint;

    /**
     * Gets the current painting tool selected
     *
     * @return - The paint tool
     */
    public PaintingTool getSelectedPaintTool()
    {
        return paintingToolManager.getToolByItem(getPlayer().getItemInHand());
    }

    public Player getPlayer()
    {
        return Bukkit.getPlayer(uuid);
    }

    public Drawer(PaintingToolManager toolManager, UUID uuid, String word) {
        this.paintingToolManager = toolManager;
        this.uuid = uuid;
        this.word = word;

        toolManager.getRegisteredTools().forEach(tool -> {
            getPlayer().getInventory().setItem(tool.getSlot() - 1, tool.getItem());
        });
    }
}
