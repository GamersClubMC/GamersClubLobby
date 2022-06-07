package me.gamersclub.lobby.commands;

import me.gamersclub.lobby.util.configuration.ConfigManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HelpCommand implements CommandExecutor {
    public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String label, String[] args) {
        if (command.getName().equalsIgnoreCase("help")) {
            List<String> help = ConfigManager.getConfigStringList("help");
            int i = 0;
            while (i < help.size()) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes(('&'), help.get(i)));
                i++;
            }
        }
        return true;
    }
}
