package me.imillusion.drawmything.cleanup;

import me.imillusion.drawmything.DrawPlugin;
import org.bukkit.entity.Player;

import java.util.function.BiConsumer;

public class FindNewGameAction implements BiConsumer<DrawPlugin, Player> {

    @Override
    public void accept(DrawPlugin main, Player player) {
        main.getPlayerManager().get(player).setup();
    }
}
