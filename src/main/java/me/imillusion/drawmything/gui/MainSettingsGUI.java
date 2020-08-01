package me.imillusion.drawmything.gui;

import com.google.common.base.Strings;
import lombok.Getter;
import me.imillusion.drawmything.DrawPlugin;
import me.imillusion.drawmything.files.YMLBase;
import me.imillusion.drawmything.gui.configuration.ConfigurableDouble;
import me.imillusion.drawmything.gui.configuration.ConfigurableInt;
import me.imillusion.drawmything.gui.menu.Menu;
import me.imillusion.drawmything.utils.ItemBuilder;
import me.imillusion.drawmything.utils.PrimitiveUnboxer;
import me.imillusion.drawmything.utils.StringComparator;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//I'm sorry
public class MainSettingsGUI {

    private final DrawPlugin main;

    private final List<YMLBase> configFiles = new ArrayList<>();

    private final Class<?>[] classes = {
            ConfigurableInt.class,
            ConfigurableDouble.class
    };

    private int slot = 0;

    @Getter
    private Menu menu;

    public MainSettingsGUI(DrawPlugin main)
    {
        this.main = main;
        setup();
    }

    private void setup()
    {
        loadConfigs();

        menu = new Menu(((configFiles.size() / 9) + 1) * 9, centerTitle("Select your file"), "main-menu");

        for (int i = 0; i < configFiles.size(); i++) {
            YMLBase base = configFiles.get(i);
            Menu sub = createMenu(base);

            menu.setItem(i, getItem(base.getFile().getName()), (e) -> {
                e.getWhoClicked().closeInventory();
                e.getWhoClicked().openInventory(sub.getInventory());
            });
        }


        menu.protect();
        menu.build();
    }

    private void loadConfigs()
    {
        for (Field field : main.getClass().getDeclaredFields()) {
            if (field.getType().getSuperclass() == null)
                continue;

            Class<?> superClass = field.getType().getSuperclass();

            if (!superClass.equals(YMLBase.class))
                continue;

            try {
                field.setAccessible(true);
                configFiles.add((YMLBase) superClass.cast(field.get(main)));
                field.setAccessible(false);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        configFiles.sort((o1, o2) -> o1.getFile()
                .getName().compareToIgnoreCase(o2.getFile().getName()));
    }

    private <T extends YMLBase> Menu createMenu(T base)
    {
        FileConfiguration config = base.getConfiguration();
        Map<String, Object> configurableValues = new HashMap<>();

        createItems(config, configurableValues);
        Menu menu = new Menu(((configurableValues.size() / 9) + 1) * 9, centerTitle(base.getFile().getName()), base.getFile().getName());

        List<Map.Entry<String, Object>> list = configurableValues.entrySet().stream()
                .sorted((e1, e2) -> new StringComparator().compare(e1.getKey(), e2.getKey()))
                .collect(Collectors.toList());

        list.forEach((entry) -> {
            String path = entry.getKey().startsWith(".") ? entry.getKey().replaceFirst("\\.", "") : entry.getKey();
            Object object = entry.getValue();

            Class<?> clazz = PrimitiveUnboxer.unbox(object.getClass());
            System.out.println(clazz);
            for (Class<?> configurableClass : classes)
                if (configurableClass.getConstructors()[0].getParameterTypes()[0].equals(clazz)) {
                    try {
                        configurableClass.getConstructors()[0].newInstance(object, path, base, slot++, menu, this.menu);
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }

        });
        slot = 0;

        return menu.build();
    }

    private void createItems(ConfigurationSection section, Map<String, Object> map)
    {
        for (String key : section.getKeys(false)) {
            if (section.isConfigurationSection(key))
                createItems(section.getConfigurationSection(key), map);
            else {
                map.put(section.getCurrentPath() + "." + key, section.get(key));
            }
        }
    }

    private ItemStack getItem(String name)
    {
        return new ItemBuilder(Material.PAPER)
                .name("&b" + name)
                .build();
    }

    public String centerTitle(String title) {
        return Strings.repeat(" ", 27 - ChatColor.stripColor(title).length()) + title;
    }
}
