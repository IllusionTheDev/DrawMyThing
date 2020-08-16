package me.imillusion.drawmything.events;

import lombok.Getter;
import me.imillusion.drawmything.scoreboard.TeamsScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.List;
import java.util.UUID;

@Getter
public class ScoreboardDisplayEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private TeamsScoreboard scoreboard;
    private List<String> newText;
    private UUID targetUUID;

    public ScoreboardDisplayEvent(TeamsScoreboard scoreboard, List<String> newText, UUID targetUUID) {
        this.scoreboard = scoreboard;
        this.newText = newText;
        this.targetUUID = targetUUID;

        Bukkit.getPluginManager().callEvent(this);
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public Player getTarget() {
        return Bukkit.getPlayer(targetUUID);
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

}
