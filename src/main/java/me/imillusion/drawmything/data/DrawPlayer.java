package me.imillusion.drawmything.data;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import me.imillusion.drawmything.DrawPlugin;
import me.imillusion.drawmything.game.Game;
import me.imillusion.drawmything.game.canvas.Point;
import me.imillusion.drawmything.game.data.drawing.tools.PaintingTool;
import me.imillusion.drawmything.scoreboard.ScoreboardTemplate;
import me.imillusion.drawmything.scoreboard.TeamsScoreboard;
import me.imillusion.drawmything.utils.Pair;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;

import java.util.UUID;

@Getter
@Setter
public class DrawPlayer {

    @Setter(AccessLevel.NONE)
    private final DrawPlugin main;

    @Setter(AccessLevel.NONE)
    private UUID uuid;

    private TeamsScoreboard scoreboard;

    private ScoreboardTemplate currentTemplate;
    private Pair<String, String>[] lastScoreboardPlaceholders;
    private Game currentGame;

    //lobby data
    private boolean isHiding;

    //game data
    private DyeColor selectedColor = DyeColor.WHITE;
    private int brushSize = 1;
    private int ticksleft = 0;
    private String word;
    private Point lastPoint;

    public DrawPlayer(DrawPlugin main, UUID uuid) {
        this.main = main;
        this.uuid = uuid;

        main.getPlayerManager().register(this);
    }

    public PaintingTool getSelectedPaintTool()
    {
        return main.getToolManager().getToolByItem(getPlayer().getItemInHand());
    }

    public Player getPlayer()
    {
        return Bukkit.getPlayer(uuid);
    }

    public boolean isDrawer()
    {
        if (currentGame == null)
            return false;

        if (currentGame.getArena().getRound() == null)
            return false;

        if (currentGame.getArena().getRound().getDrawer() == null)
            return false;

        return currentGame.getArena().getRound().getDrawer().getUuid().equals(uuid);
    }
}
