package me.imillusion.drawmything.data;

import me.imillusion.drawmything.game.Game;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class DrawPlayerManager {

    private Map<UUID, DrawPlayer> players = new HashMap<>();

    public Collection<DrawPlayer> getAllPlayers()
    {
        return players.values();
    }

    public List<DrawPlayer> getPlayers(Game game)
    {
        return players.values().stream().filter(player -> player.getCurrentGame().equals(game)).collect(Collectors.toList());
    }

    public DrawPlayer get(Player player)
    {
        return get(player.getUniqueId());
    }

    public DrawPlayer get(UUID uuid)
    {
        return players.get(uuid);
    }

    void register(DrawPlayer player)
    {
        players.put(player.getUuid(), player);
    }

    public void unregister(DrawPlayer player)
    {
        players.remove(player.getUuid());
    }

    public void unregister(Player player)
    {
        players.remove(player.getUniqueId());
    }
}
