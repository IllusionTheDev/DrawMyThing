package me.imillusion.drawmything.utils;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;

import java.util.HashMap;
import java.util.Map;

public final class ColorConverter {

    private static ColorConverter instance;
    @Getter
    private final Map<DyeColor, ChatColor> colors = new HashMap<>();

    private ColorConverter() {
        colors.put(DyeColor.BLACK, ChatColor.BLACK);
        colors.put(DyeColor.BLUE, ChatColor.DARK_BLUE);
        colors.put(DyeColor.BROWN, ChatColor.GOLD);
        colors.put(DyeColor.CYAN, ChatColor.DARK_AQUA);
        colors.put(DyeColor.GRAY, ChatColor.DARK_GRAY);
        colors.put(DyeColor.GREEN, ChatColor.DARK_GREEN);
        colors.put(DyeColor.LIGHT_BLUE, ChatColor.AQUA);
        colors.put(DyeColor.LIME, ChatColor.GREEN);
        colors.put(DyeColor.MAGENTA, ChatColor.LIGHT_PURPLE);
        colors.put(DyeColor.ORANGE, ChatColor.GOLD);
        colors.put(DyeColor.PINK, ChatColor.LIGHT_PURPLE);
        colors.put(DyeColor.PURPLE, ChatColor.DARK_PURPLE);
        colors.put(DyeColor.RED, ChatColor.RED);
        colors.put(DyeColor.SILVER, ChatColor.GRAY);
        colors.put(DyeColor.WHITE, ChatColor.WHITE);
        colors.put(DyeColor.YELLOW, ChatColor.YELLOW);
    }

    public static ColorConverter get()
    {
        return instance == null ? instance = new ColorConverter() : instance;
    }

    public String getLastColors(String string)
    {
        StringBuilder result = new StringBuilder();

        for (int i = string.length() - 2; i >= 0; i--) {
            char section = string.charAt(i);
            if (section != ChatColor.COLOR_CHAR)
                continue;
            char id = string.charAt(i + 1);

            if (!String.valueOf(id).matches("[0-9a-flkm]"))
                continue;

            result.append(id);

            if (!ChatColor.getByChar(id).isFormat())
                break;

        }

        result.reverse();
        return result.toString().replaceAll("(.)", ChatColor.COLOR_CHAR + "$0");
    }
}
