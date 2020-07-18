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

        if ("seconds".equalsIgnoreCase(params) && dp.getCurrentGame().isActiveCountdown())
            return main.getGameCountdown().getTime(dp.getCurrentGame());
        if ("points".equalsIgnoreCase(params))
            return String.valueOf(dp.getPoints());
        if ("position".equalsIgnoreCase(params))
            return String.valueOf(dp.getPosition());
        if ("drawer".equalsIgnoreCase(params) && dp.getCurrentGame().isStarted())
            return dp.getCurrentGame().getArena().getRound().getDrawer().getPlayer().getName();
        if ("round".equalsIgnoreCase(params) && dp.getCurrentGame().isStarted())
            return String.valueOf(dp.getCurrentGame().getArena().getRound().getRoundNum());
        if ("word".equalsIgnoreCase(params) && dp.getCurrentGame().isStarted())
            return dp.getCurrentGame().getArena().getRound().getWord();
        if ("obfuscatedword".equalsIgnoreCase(params) && dp.getCurrentGame().isStarted())
            return dp.getCurrentGame().getArena().getRound().getObfuscated();


        return null;
    }

}
