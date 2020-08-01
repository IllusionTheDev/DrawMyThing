package me.imillusion.drawmything.gui;

import me.imillusion.drawmything.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public final class SettingsItems {

    public static ItemStack ADD = new ItemBuilder(Material.SKULL_ITEM)
            .name("&a&lAdd (+1)")
            .skullHash("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjBiNTVmNzQ2ODFjNjgyODNhMWMxY2U1MWYxYzgzYjUyZTI5NzFjOTFlZTM0ZWZjYjU5OGRmMzk5MGE3ZTcifX19")
            .build();
    public static ItemStack REMOVE = new ItemBuilder(Material.SKULL_ITEM)
            .name("&c&lRemove (-1)")
            .skullHash("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzNlNGI1MzNlNGJhMmRmZjdjMGZhOTBmNjdlOGJlZjM2NDI4YjZjYjA2YzQ1MjYyNjMxYjBiMjVkYjg1YiJ9fX0=")
            .build();
    public static ItemStack NEXT_PAGE = new ItemBuilder(Material.SKULL_ITEM)
            .name("&a&lNext Page >")
            .skullHash("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmIzYThjNmFlZjMzMmJjMmE0ZGVkZDIwZjY1ZTZhNmZiMGFhMzJlMjI3NmY3ODFmMmRjMjRjODI1MGQyIn19fQ==")
            .build();
    public static ItemStack PREVIOUS_PAGE = new ItemBuilder(Material.SKULL_ITEM)
            .name("&c&l< Previous Page")
            .skullHash("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmIzYThjNmFlZjMzMmJjMmE0ZGVkZDIwZjY1ZTZhNmZiMGFhMzJlMjI3NmY3ODFmMmRjMjRjODI1MGQyIn19fQ==")
            .build();

    public static ItemStack SAVE = new ItemBuilder(Material.SKULL_ITEM)
            .name("&a&lSave")
            .skullHash("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWUyOGJlYTlkMzkzNzNkMzZlZThmYTQwZWM4M2Y5YzNmY2RkOTMxNzUyMjc3NDNmOWRkMWY3ZTc4ODZiN2VlNSJ9fX0=")
            .build();

    public static ItemStack BACK_1 = new ItemBuilder(Material.SKULL_ITEM)
            .name("&c&lGo Back")
            .skullHash("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzg2MTg1YjFkNTE5YWRlNTg1ZjE4NGMzNGYzZjNlMjBiYjY0MWRlYjg3OWU4MTM3OGU0ZWFmMjA5Mjg3In19fQ==")
            .build();

    public static ItemBuilder NOTE = new ItemBuilder(Material.SKULL_ITEM)
            .skullHash("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWRjOWQxYjdmYzkxMjViOTYzMzNmZTJkMWFjZDFjYTgzZDhmN2Y5ZjM5N2JmNzA0OWVmMmI2YjhiNzZmN2ZmMSJ9fX0=");


    private SettingsItems()
    {
        //Empty constructor to prevent initialization
    }
}