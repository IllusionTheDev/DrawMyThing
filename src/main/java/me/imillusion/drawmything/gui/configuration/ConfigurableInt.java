package me.imillusion.drawmything.gui.configuration;

import me.imillusion.drawmything.files.YMLBase;
import me.imillusion.drawmything.gui.SettingsItems;
import me.imillusion.drawmything.gui.menu.Menu;
import me.imillusion.drawmything.utils.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ConfigurableInt {

    private int val;
    private final String path;
    private final YMLBase base;

    private final ItemStack item;

    public ConfigurableInt(int val, String path, YMLBase base, int slot, Menu menu, Menu previous) {
        this.val = val;
        this.path = path;
        this.base = base;

        item = new ItemBuilder(Material.SKULL_ITEM)
                .name("&a" + path)
                .skullHash("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTRkNDliYWU5NWM3OTBjM2IxZmY1YjJmMDEwNTJhNzE0ZDYxODU0ODFkNWIxYzg1OTMwYjNmOTlkMjMyMTY3NCJ9fX0=")
                .lore("", "&bLeft Click &7to increase by 1", "&cRight Click &7to decrease by 1", "", "&7Current Value: &6" + val)
                .build();

        menu.setItem(slot, item, (event) -> {
            if (event.isLeftClick())
                this.val = this.val + 1;
            if (event.isRightClick())
                this.val = this.val - 1;

            event.getInventory().setItem(slot, updateItem());
            event.setCancelled(true);
        });

        menu.setItem(menu.getSize() - 2, SettingsItems.SAVE, (e) -> {
            e.setCancelled(true);
            this.save();
        });

        menu.setItem(menu.getSize() - 1, SettingsItems.BACK_1, (e) -> {
            e.setCancelled(true);
            e.getWhoClicked().closeInventory();
            e.getWhoClicked().openInventory(previous.getInventory());
        });
    }

    private ItemStack updateItem()
    {
        ItemMeta meta = item.getItemMeta();

        List<String> lore = meta.getLore();

        lore.set(4, ChatColor.translateAlternateColorCodes('&', "&7Current Value: &6" + val));

        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public final void save()
    {
        base.getConfiguration().set(path, val);
        base.save();
    }

}
