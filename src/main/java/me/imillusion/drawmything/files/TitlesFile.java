package me.imillusion.drawmything.files;

import me.imillusion.drawmything.utils.Pair;
import me.imillusion.drawmything.utils.TitleUtil;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class TitlesFile extends YMLBase {

    public TitlesFile(JavaPlugin plugin) {
        super(plugin, new File(plugin.getDataFolder(), "titles.yml"), true);
    }

    public void playTitle(String name, Pair<String, String>[] placeholders, Player... players)
    {
        if(!getConfiguration().contains("titles." + name))
            return;

        String title = StringEscapeUtils.unescapeJava(getConfiguration().getString("titles." + name + ".title"));
        String sub = StringEscapeUtils.unescapeJava(getConfiguration().getString("titles." + name + ".subtitle"));
        int fadeIn = getConfiguration().getInt("titles." + name + ".fadein");
        int stay = getConfiguration().getInt("titles." + name + ".stay");
        int fadeOut = getConfiguration().getInt("titles." + name + ".fadeout");

        for(Pair<String, String> pair : placeholders)
        {
            title = title.replace(pair.getKey(), pair.getValue());
            sub = sub.replace(pair.getKey(), pair.getValue());
        }

        TitleUtil.sendTitle(title, sub, fadeIn, stay, fadeOut, players);

    }

    public void playTitle(String name, Player... players)
    {
        playTitle(name, new Pair[]{}, players);
    }
}
