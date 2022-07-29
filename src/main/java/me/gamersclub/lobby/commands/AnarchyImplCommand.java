package me.gamersclub.lobby.commands;

import me.gamersclub.lobby.util.configuration.ConfigManager;
import me.gamersclub.lobby.util.player.PlayerCooldownManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AnarchyImplCommand implements CommandExecutor {

    public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String s, String[] args) {
        if (command.getName().equalsIgnoreCase("anarchy-icd")) {
            if (sender instanceof ConsoleCommandSender) {
                sender.sendMessage(ConfigManager.getConfigString("general.non-console-cmd"));
                return true;
            }

            Player player = (Player) sender;
            String uuid = player.getUniqueId().toString();
            if (!PlayerCooldownManager.isInAnarchyQueue(uuid)) {
                player.performCommand("anarchy");
            }
        }
        return true;
    }
}
