package me.imillusion.drawmything.cleanup;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.imillusion.drawmything.DrawPlugin;
import org.bukkit.entity.Player;

import java.util.function.BiConsumer;

public class ServerRedirectAction implements BiConsumer<DrawPlugin, Player> {

    @Override
    public void accept(DrawPlugin main, Player player) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(main.getSettings().getBungeeLobby());

        player.sendPluginMessage(main, "BungeeCord", out.toByteArray());
    }
}
