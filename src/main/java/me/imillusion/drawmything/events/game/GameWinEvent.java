package me.imillusion.drawmything.events.game;

import lombok.Getter;
import me.imillusion.drawmything.data.DrawPlayer;
import me.imillusion.drawmything.game.Game;
import org.bukkit.entity.Player;

@Getter
public class GameWinEvent extends GameEvent {

    private DrawPlayer winner;

    public GameWinEvent(Game game, DrawPlayer winner) {
        super(game);
        this.winner = winner;

        call();
    }

    public Player getWinnerPlayer() {
        return winner.getPlayer();
    }

}
