package me.imillusion.drawmything.files;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class SoundsFile extends YMLBase {

    public SoundsFile(JavaPlugin plugin) {
        super(plugin, new File(plugin.getDataFolder(), "sounds.yml"), true);
    }

    public void playSound(Player player, String sound)
    {
        if(!getConfiguration().contains("sounds." + sound))
            return;

        Sound s = Sound.valueOf(getConfiguration().getString("sounds." + sound + ".sound"));
        float vol = (float) getConfiguration().getDouble("sounds." + sound + ".volume");
        float pitch = (float) getConfiguration().getDouble("sounds." + sound + ".volume");

        player.playSound(player.getLocation(), s, vol, pitch);
    }
}
