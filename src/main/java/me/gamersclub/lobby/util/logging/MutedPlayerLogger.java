package me.gamersclub.lobby.util.logging;

import me.gamersclub.lobby.util.configuration.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class MutedPlayerLogger {
    private final GamersClubLogger logger = new GamersClubLogger();

    public void logMute(UUID sender, String message, String endDate) {
        String senderName = Bukkit.getPlayer(sender).getName();
        List<String> names = Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
        Player target;

        for (int i = 0; i < names.size(); i++) {
            target = Bukkit.getPlayer(names.get(i));

            if (target.hasPermission("gclp.admin")) {
                target.sendMessage(String.format(ConfigManager.getConfigString("general.tried-to-talk"),senderName,endDate));
                target.sendMessage(String.format(ConfigManager.getConfigString("general.tried-to-talk-log"),message));

                logger.info(String.format(ConfigManager.getConfigString("general.tried-to-talk"),senderName,endDate));
                logger.info(String.format(ConfigManager.getConfigString("general.tried-to-talk-log"),message));
            }
        }
    }
}
