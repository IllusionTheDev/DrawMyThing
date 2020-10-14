package me.imillusion.drawmything.events.game;

import lombok.Getter;
import me.imillusion.drawmything.game.Game;

@Getter
public class GameCountdownEndEvent extends GameEvent {

    private Cause cause;

    public GameCountdownEndEvent(Game game, Cause cause) {
        super(game);
        this.cause = cause;
    }

    public enum Cause {
        NOT_ENOUGH_PLAYERS, GAME_START
    }
}
