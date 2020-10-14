package me.imillusion.drawmything.events.game;

import me.imillusion.drawmything.game.Game;
import me.imillusion.drawmything.game.arena.Arena;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public abstract class GameEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private Game game;
    private Arena arena;

    public GameEvent(Game game) {
        this.game = game;

        this.arena = game.getArena();

        Bukkit.getPluginManager().callEvent(this);
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public Game getGame() {
        return game;
    }

    public Arena getArena() {
        return arena;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
