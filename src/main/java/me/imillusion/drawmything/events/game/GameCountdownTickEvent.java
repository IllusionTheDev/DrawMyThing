package me.imillusion.drawmything.events.game;

import lombok.Getter;
import me.imillusion.drawmything.game.Game;

@Getter
public class GameCountdownTickEvent extends GameEvent {

    private int timeUntilStart;

    public GameCountdownTickEvent(Game game, int timeUntilStart) {
        super(game);
        this.timeUntilStart = timeUntilStart;
        call();
    }

}
