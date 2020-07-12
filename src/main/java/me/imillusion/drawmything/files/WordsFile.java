package me.imillusion.drawmything.files;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;

public class WordsFile extends YMLBase {

    @Getter
    private List<String> words;

    public WordsFile(JavaPlugin plugin) {
        super(plugin, new File(plugin.getDataFolder(), "words.yml"), true);

        words = getConfiguration().getStringList("words");
    }
}
