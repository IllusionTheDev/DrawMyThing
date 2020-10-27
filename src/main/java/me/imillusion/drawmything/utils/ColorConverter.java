package me.imillusion.drawmything.utils;

import org.bukkit.ChatColor;

public final class ColorConverter {

    private static ColorConverter instance;


    public static ColorConverter get() {
        return instance == null ? instance = new ColorConverter() : instance;
    }

    public String getLastColors(String string) {
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
