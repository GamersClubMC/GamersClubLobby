package me.gamersclub.lobby.commands;

import me.gamersclub.lobby.util.configuration.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static me.gamersclub.lobby.GamersClubMain.isMaintenanceEnabled;
import static me.gamersclub.lobby.GamersClubMain.playersAlreadyKicked;

public class MaintenanceCommand implements CommandExecutor {

    public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String s, String[] args) {
        if (command.getName().equalsIgnoreCase("maintenance") && sender.hasPermission("gclp.maintenance")) {
            if (args.length == 0) {
                sender.sendMessage(ConfigManager.getConfigString("maintenance.usage"));
                return true;
            }
            //check if it matches
            if (!args[0].equalsIgnoreCase("on") && !args[0].equalsIgnoreCase("off")) {
                sender.sendMessage(ConfigManager.getConfigString("maintenance.usage"));
                return true;
            }

            if (args[0].equalsIgnoreCase("on")) {
                sender.sendMessage("§bEnabling maintenance..");
                isMaintenanceEnabled = true;

            }
            else if (args[0].equalsIgnoreCase("off")) {
                sender.sendMessage("§cDisabling maintenance..");
                isMaintenanceEnabled = false;
                playersAlreadyKicked = false;
                sender.sendMessage("§bMaintenance disabled.");
            }

            if (isMaintenanceEnabled) {
                if (!playersAlreadyKicked) {
                    Player[] playerlist = Bukkit.getOnlinePlayers().toArray(new Player[0]);

                    for (int p = 0; p < playerlist.length; p++) {
                        if (!playerlist[p].hasPermission("gclp.maintenance.bypass"))
                            playerlist[p].kickPlayer(ConfigManager.getConfigString("maintenance.kick-msg"));
                    }
                    playersAlreadyKicked = true;
                    sender.sendMessage("§Successfully kicked all players with the reason: " + ConfigManager.getConfigString("maintenance.kick-msg"));
                }

            }

        }
        return true;
    }

}
