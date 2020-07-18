package me.imillusion.drawmything.placeholders;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.imillusion.drawmything.DrawPlugin;
import me.imillusion.drawmything.data.DrawPlayer;
import org.bukkit.entity.Player;

public class PAPIHook extends PlaceholderExpansion {

    private final DrawPlugin main;

    public PAPIHook(DrawPlugin main) {
        this.main = main;
    }

    @Override
    public String getIdentifier() {
        return "drawmything";
    }

    @Override
    public String getAuthor() {
        return "ImIllusion";
    }

    @Override
    public String getVersion() {
        return "1";
    }

    @Override
    public String onPlaceholderRequest(Player p, String params) {
        DrawPlayer dp = main.getPlayerManager().get(p);

        if (params.equalsIgnoreCase("seconds") && dp.getCurrentGame().isActiveCountdown())
            return main.getGameCountdown().getTime(dp.getCurrentGame());
        if (params.equalsIgnoreCase("points"))
            return String.valueOf(dp.getPoints());
        if (params.equalsIgnoreCase("position"))
            return String.valueOf(dp.getPosition());
        if (params.equalsIgnoreCase("drawer") && dp.getCurrentGame().isStarted())
            return dp.getCurrentGame().getArena().getRound().getDrawer().getPlayer().getName();
        if (params.equalsIgnoreCase("round") && dp.getCurrentGame().isStarted())
            return String.valueOf(dp.getCurrentGame().getArena().getRound().getRoundNum());
        if (params.equalsIgnoreCase("word") && dp.getCurrentGame().isStarted())
            return dp.getCurrentGame().getArena().getRound().getWord();
        if (params.equalsIgnoreCase("obfuscatedword") && dp.getCurrentGame().isStarted())
            return dp.getCurrentGame().getArena().getRound().getObfuscated();


        return null;
    }
}
