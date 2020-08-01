package me.imillusion.drawmything.cmds;

import me.imillusion.drawmything.DrawPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SettingsCMD implements CommandExecutor {

    private final DrawPlugin main;

    public SettingsCMD(DrawPlugin main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(" ");
            return true;
        }

        main.getSettingsGUI().getMenu().open((Player) sender);
        return true;
    }

}
