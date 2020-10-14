package me.imillusion.drawmything.events;

import lombok.Getter;
import me.imillusion.drawmything.events.game.GameEvent;
import me.imillusion.drawmything.game.Game;
import me.imillusion.drawmything.game.data.Round;

@Getter
public class RoundTickEvent extends GameEvent {

    private Round round;

    public RoundTickEvent(Game game) {
        super(game);

        this.round = getArena().getRound();
    }
}
