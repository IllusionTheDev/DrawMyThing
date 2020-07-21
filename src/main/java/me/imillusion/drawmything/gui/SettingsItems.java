package me.imillusion.drawmything.gui;

import lombok.Getter;
import me.imillusion.drawmything.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@Getter
public final class SettingsItems {

    private static ItemStack ADD = new ItemBuilder(Material.SKULL_ITEM)
            .name("&a&lAdd (+1)")
            .skullHash("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjBiNTVmNzQ2ODFjNjgyODNhMWMxY2U1MWYxYzgzYjUyZTI5NzFjOTFlZTM0ZWZjYjU5OGRmMzk5MGE3ZTcifX19")
            .build();
    private static ItemStack REMOVE = new ItemBuilder(Material.SKULL_ITEM)
            .name("&c&lRemove (-1)")
            .skullHash("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzNlNGI1MzNlNGJhMmRmZjdjMGZhOTBmNjdlOGJlZjM2NDI4YjZjYjA2YzQ1MjYyNjMxYjBiMjVkYjg1YiJ9fX0=")
            .build();
    private static ItemStack NEXT_PAGE = new ItemBuilder(Material.SKULL_ITEM)
            .name("&a&lNext Page >")
            .skullHash("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmIzYThjNmFlZjMzMmJjMmE0ZGVkZDIwZjY1ZTZhNmZiMGFhMzJlMjI3NmY3ODFmMmRjMjRjODI1MGQyIn19fQ==")
            .build();
    private static ItemStack PREVIOUS_PAGE = new ItemBuilder(Material.SKULL_ITEM)
            .name("&c&l< Previous Page")
            .skullHash("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmIzYThjNmFlZjMzMmJjMmE0ZGVkZDIwZjY1ZTZhNmZiMGFhMzJlMjI3NmY3ODFmMmRjMjRjODI1MGQyIn19fQ==")
            .build();

    private SettingsItems()
    {
        //Empty constructor to prevent initialization
    }
}