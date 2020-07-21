package me.imillusion.drawmything.gui;

import com.google.common.base.Strings;
import lombok.Getter;
import me.imillusion.drawmything.DrawPlugin;
import me.imillusion.drawmything.files.YMLBase;
import me.imillusion.drawmything.gui.configuration.ConfigurableInt;
import me.imillusion.drawmything.gui.configuration.Path;
import me.imillusion.drawmything.gui.menu.Menu;
import me.imillusion.drawmything.utils.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//I'm sorry
public class MainSettingsGUI {

    private final DrawPlugin main;

    private final List<YMLBase> configFiles = new ArrayList<>();

    private final Class<?>[] classes = {
            ConfigurableInt.class
    };

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
        Class clazz = base.getClass();
        List<String> fieldName = new ArrayList<>();

        for (Field field : clazz.getDeclaredFields())
            if (field.isAnnotationPresent(Path.class))
                fieldName.add(field.getName());

        if (fieldName.isEmpty())
            return new Menu(27, clazz.getName(), clazz.getName()).build();

        Collections.sort(fieldName);
        int slot = 0;

        Menu menu = new Menu(((fieldName.size() / 9) + 1) * 9, base.getFile().getName(), base.getFile().getName());

        for (String name : fieldName) {
            try {
                Field field = clazz.getDeclaredField(name);

                field.setAccessible(true);

                for (Class<?> configurableClass : classes)
                    if (configurableClass.getConstructors()[0].getParameterTypes()[0].equals(field.getType())) {
                        String path = field.getAnnotation(Path.class).path();
                        configurableClass.getConstructors()[0].newInstance(field.get(base), path, base, slot++, menu, this.menu);
                    }

                field.setAccessible(false);
            } catch (NoSuchFieldException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
                e.printStackTrace();
            }
        }

        menu.build();
        return menu;
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
