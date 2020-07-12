package me.imillusion.drawmything.gui;

import lombok.Getter;
import me.imillusion.drawmything.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class SettingsGUI implements InventoryHolder {

    @Getter
    private Inventory inventory;

    private static final ItemStack UP_ARROW = new ItemBuilder(Material.SKULL_ITEM)
            .name("&a&lAdd +1")
            .skull("MHF_UP")
            .build();
}
