package me.gamersclub.lobby.commands;

import me.gamersclub.lobby.guis.ServerSelector;
import me.gamersclub.lobby.util.player.BedrockPlayerChecker;
import me.gamersclub.lobby.util.configuration.ConfigManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ServerSelectorCommand implements CommandExecutor {

    final ServerSelector srv = new ServerSelector();

    public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String s, String[] args) {
        if (command.getName().equalsIgnoreCase("serverselector")) {
            if (sender instanceof ConsoleCommandSender) {
                sender.sendMessage(ConfigManager.getConfigString("general.non-console-cmd"));
                return true;
            }

            Player player = (Player) sender;
            if (BedrockPlayerChecker.isBedrockPlayer(player.getUniqueId()))
                ServerSelector.ServerSelectorForm(player.getUniqueId());
            else
                player.openInventory(srv.ServerSelectorUI());

        }
        return true;
    }
}
