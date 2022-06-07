package me.gamersclub.lobby.commands;

import me.gamersclub.lobby.storage.SQLiteLogManager;
import me.gamersclub.lobby.storage.SQLiteMuteManager;
import me.gamersclub.lobby.storage.SQLiteUserCacheManager;
import me.gamersclub.lobby.util.configuration.ConfigManager;
import me.gamersclub.lobby.util.logging.GamersClubLogger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;

@SuppressWarnings({"CanBeFinal", "FieldMayBeFinal"})
public class UnmuteCommand implements CommandExecutor {
    private final GamersClubLogger log = new GamersClubLogger();
    private String playerUnmute = ChatColor.translateAlternateColorCodes('&',ConfigManager.getConfigString("unmute.unmuted-player"));

    public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String s, String[] args) {
        if (command.getName().equalsIgnoreCase("unmute") && sender.hasPermission("gclp.unmute")) {
            if(args.length != 1) {
                sender.sendMessage(ConfigManager.getConfigString("unmute.usage"));
                return true;
            }

            Player target = Bukkit.getPlayer(args[0]);
            OfflinePlayer offlineTarget = Bukkit.getPlayer(args[0]);

            //Check if the player specified isn't online
            if (target == null) {
                //check if we have the player in the uuid cache
                if (!SQLiteUserCacheManager.cacheUsernameCheck(args[0])) {
                    return true;
                }
                //use offline player if player was found in said cache
                offlineTarget = SQLiteUserCacheManager.cacheUsernameGet(args[0]);
            }

            if (offlineTarget != null) {
                SQLiteMuteManager.deleteMute(offlineTarget.getUniqueId());
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9Player &r&f"+ args[0] + "&r&9 was unmuted!"));
                log.info("&9Player &r&f"+ args[0] + " &9was unmuted.");

                if (sender instanceof ConsoleCommandSender) {
                    SQLiteLogManager.addLog(target.getUniqueId().toString(),target.getName(),"(No reason specified)", LocalDate.now().toString(),"UNMUTE");
                    return true;
                }

                Player mod = (Player) sender;
                SQLiteLogManager.addLog(target.getUniqueId().toString(),target.getName(),mod.getUniqueId().toString(),mod.getName(),"(No reason specified)", LocalDate.now().toString(),"UNMUTE");
            }
            else {
                SQLiteMuteManager.deleteMute(target.getUniqueId());
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9Player &r&f"+ target.getName() + "&r&9 was unmuted!"));
                log.info("&9Player &r&f"+ target.getName() + " &9was unmuted.");
                target.sendMessage(playerUnmute);

                Player mod = (Player) sender;

                SQLiteLogManager.addLog(target.getUniqueId().toString(),target.getName(),mod.getUniqueId().toString(),mod.getName(),"(No reason specified)", LocalDate.now().toString(),"UNMUTE");
            }


        }
        return true;
    }
}
