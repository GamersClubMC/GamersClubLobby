package me.gamersclub.lobby.commands;

import me.gamersclub.lobby.guis.AdminMenu;
import me.gamersclub.lobby.util.player.BedrockPlayerChecker;
import me.gamersclub.lobby.util.configuration.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GclpCommand implements CommandExecutor {

    final AdminMenu am = new AdminMenu();

    public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String s, String[] args) {
        Player player = (Player) sender;
        if (command.getName().equalsIgnoreCase("gclp")) {
            switch (args[0]) {
                case "anarchy":
                    player.performCommand("anarchy");
                    break;
                case "lobby":
                    player.performCommand("lobby");
                    break;
                case "reload":
                    if (sender.hasPermission("gclp.reload")) {
                        Bukkit.getPluginManager().getPlugin("GamersClubLobbyPlugin").reloadConfig();
                        sender.sendMessage(ChatColor.AQUA + "Config reloaded!");
                    } else
                        sender.sendMessage(ChatColor.RED + ConfigManager.getConfigString("general.no-permission"));
                    break;
                case "admin":
                    if (player.hasPermission("gclp.admin")) {
                        if (BedrockPlayerChecker.isBedrockPlayer(player.getUniqueId()))
                            am.AdminForm(player.getUniqueId());
                        else
                            player.openInventory(am.adminMenu());
                    }
                    break;
                default:
                    sender.sendMessage(ChatColor.RED + "Usage: /gclp [args]");
                    break;
            }
        }
        return true;
    }
}
