package me.gamersclub.lobby.commands;

import me.gamersclub.lobby.util.configuration.ConfigManager;
import me.gamersclub.lobby.util.player.ServerSwitcher;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class AnarchyCommand implements CommandExecutor {
    final ServerSwitcher sw = new ServerSwitcher();

    public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String s, String[] args) {
        if (command.getName().equalsIgnoreCase("anarchy")) {
            if (sender instanceof ConsoleCommandSender) {
                sender.sendMessage(ConfigManager.getConfigString("general.non-console-cmd"));
                return true;
            }

            sender.sendMessage(ChatColor.translateAlternateColorCodes(('&'), ConfigManager.getConfigString("general.anarchy-msg")));
            sw.switchServer(Objects.requireNonNull(((Player) sender).getPlayer()),"anarchy");
        }
        return true;
    }
}
