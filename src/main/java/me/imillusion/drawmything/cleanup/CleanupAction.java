package me.imillusion.drawmything.cleanup;

import me.imillusion.drawmything.DrawPlugin;
import org.bukkit.entity.Player;

import java.util.function.BiConsumer;

public enum CleanupAction {
    KICK(new KickAction()),
    FIND_NEW_GAME(new FindNewGameAction()),
    SERVER_CHANGE(new ServerRedirectAction());

    private BiConsumer<DrawPlugin, Player> action;

    CleanupAction(BiConsumer<DrawPlugin, Player> action) {
        this.action = action;
    }

    public void apply(DrawPlugin main, Player player) {
        action.accept(main, player);
    }
}
