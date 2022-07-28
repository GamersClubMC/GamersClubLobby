package me.gamersclub.lobby.commands;

import me.gamersclub.lobby.guis.AdminMenu;
import me.gamersclub.lobby.util.player.BedrockPlayerChecker;
import me.gamersclub.lobby.util.configuration.ConfigManager;
import me.gamersclub.lobby.util.player.PlayerLookup;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

import static me.gamersclub.lobby.GamersClubMain.logMutes;

public class GclpCommand implements CommandExecutor {
    private final AdminMenu am = new AdminMenu();
    private final PlayerLookup playerLookup = new PlayerLookup();
    private Player player;

    public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String s, String[] args) {
        if (command.getName().equalsIgnoreCase("gclp")) {
            if (args.length == 0) {
                sender.sendMessage(ChatColor.RED + "Usage: /gclp [args]");
                return true;
            }

            switch (args[0]) {
                case "anarchy":
                    if (sender instanceof ConsoleCommandSender) {
                        sender.sendMessage(ConfigManager.getConfigString("general.non-console-cmd"));
                        break;
                    }

                    player = (Player) sender;
                    player.performCommand("anarchy");
                    break;
                case "lobby":
                    if (sender instanceof ConsoleCommandSender) {
                        sender.sendMessage(ConfigManager.getConfigString("general.non-console-cmd"));
                        break;
                    }

                    player = (Player) sender;
                    player.performCommand("lobby");
                    break;
                case "help":
                    List<String> help = ConfigManager.getConfigStringList("help");
                    int i = 0;
                    while (i < help.size()) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes(('&'), help.get(i)));
                        i++;
                    }
                    break;
                case "reload":
                    if (sender.hasPermission("gclp.reload")) {
                        Bukkit.getPluginManager().getPlugin("GamersClubLobbyPlugin").reloadConfig();
                        logMutes = ConfigManager.getConfigBoolean("general.log-muted-player-messages");
                        sender.sendMessage(ChatColor.AQUA + "Config reloaded!");
                    } else
                        sender.sendMessage(ChatColor.RED + ConfigManager.getConfigString("general.no-permission"));
                    break;
                case "admin":
                    if (sender instanceof ConsoleCommandSender) {
                        sender.sendMessage(ConfigManager.getConfigString("general.non-console-cmd"));
                        break;
                    }

                    player = (Player) sender;
                    if (sender.hasPermission("gclp.admin")) {
                        if (BedrockPlayerChecker.isBedrockPlayer(player.getUniqueId()))
                            am.AdminForm(player.getUniqueId());
                        else
                            player.openInventory(am.adminMenu());
                    }
                    break;
                case "lookup":
                    if (sender.hasPermission("gclp.lookup")) {
                        if (args.length >= 2) {
                            UUID uuid = playerLookup.getBedrockUUID(args[1]);

                            if (uuid == null) {
                                sender.sendMessage(ChatColor.RED + "Player not found!");
                                sender.sendMessage(ChatColor.RED + "Either the player is not in the Geyser API cache or does not exist!");
                                return true;
                            }

                            sender.sendMessage("§bJava UUID for user §r" + args[1] + "§b:");
                            sender.sendMessage(uuid.toString());
                        } else {
                            sender.sendMessage(ChatColor.RED + "Usage: /gclp lookup [Xbox Gamertag]");
                        }
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
