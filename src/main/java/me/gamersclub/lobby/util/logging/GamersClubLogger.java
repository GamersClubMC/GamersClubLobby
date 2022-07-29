package me.gamersclub.lobby.util.logging;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

import static me.gamersclub.lobby.GamersClubMain.debugMode;
import static me.gamersclub.lobby.GamersClubMain.plugin;

public class GamersClubLogger {
    private final ConsoleCommandSender sender = Bukkit.getConsoleSender();
    
    public void info(@NotNull String message) {
        if (message.contains("§") || message.contains("&")) {
            sender.sendMessage("[GamersClubLP] " + ChatColor.translateAlternateColorCodes('&',message));
        } else {
            plugin.getLogger().log(Level.INFO,message);
        }
    }

    public void warn(@NotNull String message) {
        plugin.getLogger().log(Level.WARNING,message.replaceAll("§","").replaceAll("&",""));
    }

    public void debug(String message) {
        if (debugMode) {
            if (message.contains("§") || message.contains("&")) {
                sender.sendMessage("[GamersClubLP] [DEBUG] " + ChatColor.translateAlternateColorCodes('&',message));
            } else {
                plugin.getLogger().log(Level.INFO, "[DEBUG] " + message);
            }
        }
    }
}
