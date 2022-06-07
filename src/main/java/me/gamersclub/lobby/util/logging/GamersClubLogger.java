package me.gamersclub.lobby.util.logging;

import me.gamersclub.lobby.util.configuration.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.logging.Level;

public class GamersClubLogger {
    
    public void info(String message) {
        Bukkit.getLogger().log(Level.INFO,ChatColor.translateAlternateColorCodes(('&'), ConfigManager.getConfigPrefix() + " " + message));
    }

    public void error(String message) {
        Bukkit.getLogger().log(Level.WARNING,ChatColor.translateAlternateColorCodes(('&'),ConfigManager.getConfigPrefix() + " " + message));
    }
}
