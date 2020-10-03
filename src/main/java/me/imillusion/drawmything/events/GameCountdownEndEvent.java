package me.imillusion.drawmything.events;

import lombok.Getter;
import me.imillusion.drawmything.game.Game;
import me.imillusion.drawmything.game.arena.Arena;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class GameCountdownEndEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private Game game;
    private Arena arena;
    private Cause cause;

    public GameCountdownEndEvent(Game game, Cause cause) {
        this.game = game;
        this.cause = cause;

        this.arena = game.getArena();

        Bukkit.getPluginManager().callEvent(this);
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public enum Cause {
        NOT_ENOUGH_PLAYERS, GAME_START
    }
}
