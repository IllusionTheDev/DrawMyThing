package me.imillusion.drawmything.events;

import lombok.Getter;
import me.imillusion.drawmything.game.Game;
import me.imillusion.drawmything.game.arena.Arena;
import me.imillusion.drawmything.game.canvas.Canvas;
import me.imillusion.drawmything.game.data.Round;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class GameStartEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private Game game;
    private Arena arena;
    private Round round;
    private Canvas canvas;

    public GameStartEvent(Game game) {
        this.game = game;

        this.arena = game.getArena();
        this.round = arena.getRound();
        this.canvas = arena.getCanvas();

        Bukkit.getPluginManager().callEvent(this);
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
