package me.imillusion.drawmything.utils;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ItemBuilder {

    private static Table<String, Method, Class<?>> configurableValues = HashBasedTable.create();

    static {
        try {
            configurableValues.put("data", ItemBuilder.class.getMethod("data", int.class), int.class);
            configurableValues.put("name", ItemBuilder.class.getMethod("name", String.class), String.class);
            configurableValues.put("amount", ItemBuilder.class.getMethod("amount", int.class), int.class);
            configurableValues.put("lore", ItemBuilder.class.getMethod("lore", String[].class), String[].class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private final Material material;
    private int amount = 1;
    private final List<String> lore = new ArrayList<>();
    private String name = "";
    private ItemFlag[] itemFlags = null;
    private short data = -1;
    private String skullName = null;

    public ItemBuilder(Material material) {
        this.material = material;
    }

    public static ItemStack fromSection(ConfigurationSection section)
    {
        ItemBuilder builder = new ItemBuilder(Material.valueOf(section.getString("material")));

        configurableValues.cellSet().forEach(cell -> {
            String id = cell.getRowKey();
            Method method = cell.getColumnKey();
            Class<?> clazz = cell.getValue();

            if (section.contains(id)) {
                try {
                    method.invoke(builder, clazz.cast(section.get(id)));
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        });

        return builder.build();
    }

    public ItemBuilder amount(int newAmount)
    {
        if (newAmount > 64)
            newAmount = 64;
        this.amount = newAmount;
        return this;
    }

    public ItemBuilder data(int num)
    {
        this.data = (short) num;
        return this;
    }

    public ItemBuilder lore(String... lore)
    {
        for (String s : lore)
            this.lore.add(ChatColor.translateAlternateColorCodes('&', s));
        return this;
    }

    public ItemBuilder name(String name)
    {
        this.name = ChatColor.translateAlternateColorCodes('&', name);
        return this;
    }

    public ItemBuilder flags(ItemFlag... flags)
    {
        this.itemFlags = flags;
        return this;
    }

    public ItemBuilder skull(String name)
    {
        Validate.isTrue(material.name().contains("SKULL"), "Attempt to set skull data on non skull item");
        this.skullName = name;
        return this;
    }

    public ItemStack build()
    {
        ItemStack item = new ItemStack(material, amount);

        if (data != -1)
            item.setDurability(data);

        ItemMeta meta = item.getItemMeta();

        if (name != null && !name.isEmpty())
            meta.setDisplayName(name);
        if (lore != null && !lore.isEmpty())
            meta.setLore(lore);
        if (itemFlags != null)
            meta.addItemFlags(itemFlags);
        if (skullName != null)
            ((SkullMeta) meta).setOwner(skullName);

        item.setItemMeta(meta);
        return item;
    }
}
