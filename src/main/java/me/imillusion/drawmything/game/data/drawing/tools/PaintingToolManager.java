package me.imillusion.drawmything.game.data.drawing.tools;

import lombok.Getter;
import me.imillusion.drawmything.DrawPlugin;
import me.imillusion.drawmything.utils.ItemBuilder;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class PaintingToolManager {

    private final DrawPlugin main;

    @Getter
    private List<PaintingTool> registeredTools = new ArrayList<>();

    public PaintingToolManager(DrawPlugin main) {
        this.main = main;

        ConfigurationSection section = main.getItems().getConfiguration().getConfigurationSection("items.tools");

        registerTool(section.getConfigurationSection("brush"), BrushTool.class);
        registerTool(section.getConfigurationSection("fill"), FillTool.class);
        registerTool(section.getConfigurationSection("blank-page"), BlankPageTool.class);
    }

    public void registerTool(PaintingTool tool) {
        registeredTools.add(tool);
    }

    public void registerTool(ConfigurationSection section, Class<? extends PaintingTool> clazz) {
        if (section == null)
            return;

        if (!section.getBoolean("enabled", false))
            return;

        ItemStack item = ItemBuilder.fromSection(section);
        int slot = section.getInt("slot");
        String identifier = section.getCurrentPath();

        String[] split = identifier.split("\\.");

        identifier = split[split.length - 1];

        Constructor<?>[] constructors = clazz.getConstructors();

        try {
            if (constructors.length == 0)
                registeredTools.add(clazz.newInstance());

            Constructor<?> constructor = constructors[0];
            Class<?>[] args = constructor.getParameterTypes();

            if (args.length == 0) {
                registeredTools.add((PaintingTool) constructor.newInstance());
                return;
            }

            if (args[0].equals(DrawPlugin.class)) {
                registeredTools.add((PaintingTool) constructor.newInstance(
                        main,
                        slot,
                        item,
                        identifier,
                        main.getItems().getActions("tools." + identifier)));
                return;
            }

            registeredTools.add((PaintingTool) constructor.newInstance(
                    slot,
                    item,
                    identifier,
                    main.getItems().getActions("tools." + identifier)));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    /**
     * Gets a PaintingTool by item, can be null.
     *
     * @param item - The item to get from
     * @return - The PaintingTool found, if not null
     */
    public PaintingTool getToolByItem(ItemStack item) {
        return registeredTools.stream()
                .filter(tool -> tool.getItem().equals(item))
                .findFirst()
                .orElse(null);
    }

    public PaintingTool getToolByIdentifier(String identifier) {
        return registeredTools.stream()
                .filter(tool -> tool.getIdentifier().equalsIgnoreCase(identifier))
                .findFirst()
                .orElse(null);
    }
}
