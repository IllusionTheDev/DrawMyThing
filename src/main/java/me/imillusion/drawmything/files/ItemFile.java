package me.imillusion.drawmything.files;

import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ItemFile extends YMLBase {

    private final Map<String, ItemStack> items = new HashMap<>();

    public ItemFile(JavaPlugin plugin) {
        super(plugin, new File(plugin.getDataFolder(), "items.yml"), true);


    }

    public boolean isEnabled(String name)
    {
        return getConfiguration().getBoolean("items." + name + ".enabled");
    }

    public Set<Action> getActions(String name)
    {
        return Arrays.stream(getConfiguration().getString("items." + name + ".actions").split(" "))
                .map(Action::valueOf)
                .collect(Collectors.toSet());
    }

    //public void setItem(String name, int slot)
    //{

    //}
}
