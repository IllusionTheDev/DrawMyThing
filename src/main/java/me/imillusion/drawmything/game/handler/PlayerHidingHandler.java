package me.imillusion.drawmything.game.handler;

import lombok.Getter;
import me.imillusion.drawmything.DrawPlugin;
import me.imillusion.drawmything.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerHidingHandler implements Listener {

    @Getter
    private ItemStack inactiveItem;
    @Getter
    private ItemStack activeItem;

    private final DrawPlugin main;

    public PlayerHidingHandler(DrawPlugin main) {
        this.main = main;

        this.inactiveItem = new ItemBuilder(Material.INK_SACK)
                .name("&aHide other guessers &8(Right Click)")
                .data(8)
                .build();

        this.activeItem = new ItemBuilder(Material.INK_SACK)
                .name("&aShow other guessers &8(Right Click)")
                .data(10)
                .build();
    }

    @EventHandler
    private void onClick(PlayerInteractEvent e)
    {
        if (e.getItem() != null && e.getItem().equals(inactiveItem)) {
            main.getGameManager().getPlayerGame(e.getPlayer().getUniqueId()).getArena().getPlayers().forEach(player -> main.getHider().hideEntity(e.getPlayer(), player));
            e.getPlayer().setItemInHand(activeItem);
            main.getPlayerManager().get(e.getPlayer()).setHiding(true);
        }

        if (e.getItem() != null && e.getItem().equals(activeItem)) {
            main.getGameManager().getPlayerGame(e.getPlayer().getUniqueId()).getArena().getPlayers().forEach(player -> main.getHider().showEntity(e.getPlayer(), player));
            e.getPlayer().setItemInHand(inactiveItem);
            main.getPlayerManager().get(e.getPlayer()).setHiding(false);

        }
    }

}
