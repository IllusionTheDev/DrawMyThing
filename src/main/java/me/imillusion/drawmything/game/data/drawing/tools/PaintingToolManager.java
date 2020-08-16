package me.imillusion.drawmything.game.data.drawing.tools;

import lombok.Getter;
import me.imillusion.drawmything.DrawPlugin;
import me.imillusion.drawmything.utils.ItemBuilder;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaintingToolManager {

    private final DrawPlugin main;

    private final Map<String, Class<? extends PaintingTool>> toolClasses = new HashMap<>();

    @Getter
    private List<PaintingTool> registeredTools = new ArrayList<>();

    public PaintingToolManager(DrawPlugin main) {
        this.main = main;

        toolClasses.put("brush", BrushTool.class);
        toolClasses.put("fill", FillTool.class);
        toolClasses.put("blank-page", BlankPageTool.class);

        ConfigurationSection section = main.getItems().getConfiguration().getConfigurationSection("items.tools");

        for (String id : section.getKeys(false))
            registerTool(section.getConfigurationSection(id));
    }

    public void registerTool(PaintingTool tool) {
        registeredTools.add(tool);
    }

    public void registerTool(ConfigurationSection section) {
        if (!section.getBoolean("enabled", false))
            return;

        ItemStack item = ItemBuilder.fromSection(section);
        int slot = section.getInt("slot");
        String identifier = section.getCurrentPath();

        String[] split = identifier.split("\\.");

        identifier = split[split.length - 1];

        if (!toolClasses.containsKey(identifier))
            return;

        Class<? extends PaintingTool> clazz = toolClasses.get(identifier);

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
                registeredTools.add((PaintingTool) constructor.newInstance(main, slot, item, identifier, main.getItems().getActions(identifier)));
                return;
            }

            registeredTools.add((PaintingTool) constructor.newInstance(slot, item, identifier, main.getItems().getActions(identifier)));
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
