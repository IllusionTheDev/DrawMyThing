package me.imillusion.drawmything.events.game;

import lombok.Getter;
import me.imillusion.drawmything.game.Game;
import me.imillusion.drawmything.game.canvas.Canvas;
import me.imillusion.drawmything.game.data.Round;

@Getter
public class GameStartEvent extends GameEvent {

    private Round round;
    private Canvas canvas;

    public GameStartEvent(Game game) {
        super(game);

        this.round = getArena().getRound();
        this.canvas = getArena().getCanvas();

    }

}
