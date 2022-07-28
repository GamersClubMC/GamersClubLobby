package me.gamersclub.lobby.util.logging;

import org.bukkit.ChatColor;

import java.util.logging.Level;

import static me.gamersclub.lobby.GamersClubMain.*;

public class GamersClubLogger {
    
    public void info(String message) {
        plugin.getLogger().log(Level.INFO,ChatColor.translateAlternateColorCodes(('&'),message));
    }

    public void warn(String message) {
        plugin.getLogger().log(Level.WARNING,ChatColor.translateAlternateColorCodes(('&'),message));
    }

    public void debug(String message) {
        if (debugMode) {
            plugin.getLogger().log(Level.INFO,ChatColor.translateAlternateColorCodes(('&'),"[DEBUG] " + message));
        }
    }
}
