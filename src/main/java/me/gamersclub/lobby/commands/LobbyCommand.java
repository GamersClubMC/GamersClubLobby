package me.gamersclub.lobby.commands;

import me.gamersclub.lobby.util.configuration.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.NotNull;

public class LobbyCommand implements CommandExecutor {

    public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("lobby")) {
            if (sender instanceof ConsoleCommandSender) {
                sender.sendMessage(ConfigManager.getConfigString("general.non-console-cmd"));
                return true;
            }

            //lobby warp location
            double x = ConfigManager.getConfigDouble("general.lobby.x");
            double y = ConfigManager.getConfigDouble("general.lobby.y");
            double z = ConfigManager.getConfigDouble("general.lobby.x");
            float pitch = ConfigManager.getConfigFloat("general.lobby.pitch");
            float yaw = ConfigManager.getConfigFloat("general.lobby.yaw");
            String world = ConfigManager.getConfigString("general.lobby.world");
            Location spawn = new Location((Bukkit.getWorld(world)),x,y,z,pitch,yaw);

            Player player = (Player) sender;
            player.teleport(spawn, PlayerTeleportEvent.TeleportCause.PLUGIN);
            sender.sendMessage(ConfigManager.getConfigString("general.lobby-msg"));
        }
        return true;
    }

}
