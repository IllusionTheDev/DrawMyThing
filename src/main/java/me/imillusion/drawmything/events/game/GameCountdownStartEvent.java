package me.imillusion.drawmything.events.game;

import lombok.Getter;
import me.imillusion.drawmything.game.Game;

@Getter
public class GameCountdownStartEvent extends GameEvent {

    public GameCountdownStartEvent(Game game) {
        super(game);
    }
}
