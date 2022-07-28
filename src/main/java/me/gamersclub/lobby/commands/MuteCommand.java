package me.gamersclub.lobby.commands;

import me.gamersclub.lobby.storage.SQLiteUserCacheManager;
import me.gamersclub.lobby.storage.SQLiteMuteManager;
import me.gamersclub.lobby.util.configuration.ConfigManager;
import me.gamersclub.lobby.util.logging.GamersClubLogger;
import me.gamersclub.lobby.util.player.PlayerLookup;
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
import java.util.UUID;

public class MuteCommand implements CommandExecutor {
    private final GamersClubLogger log = new GamersClubLogger();
    private final PlayerLookup pl = new PlayerLookup();
    Player cmdSender;


    public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String s, String[] args) {
        if (command.getName().equalsIgnoreCase("mute") && sender.hasPermission("gclp.mute")) {
            if (args.length == 1 || args.length == 0) {
                sender.sendMessage(ConfigManager.getConfigString("mute.errors.usage"));
                return true;
            }

            //set vars
            String startDate = LocalDate.now().toString();
            String day = args[1];
            String reason;
            String endDate;


            //set a default reason if none is specified
            if (args.length == 2) {
                reason = "(No reason provided)";
            } else {
                reason = args[2];
            }
            //allow reasons to be longer than a single word
            if (args.length > 3) {
                for (int i = 3; i < args.length; i++) {
                    String cache = args[i];
                    reason = reason.concat(" " + cache);
                }
            }

            Player target = Bukkit.getPlayer(args[0]);
            OfflinePlayer offlineTarget = Bukkit.getPlayer(args[0]);
            boolean isPlayerALookup = false;
            UUID lookupPlayer = null;

            //Check if the player specified isn't online
            if (target == null) {
                //check if we have the player in the uuid cache
                if (!SQLiteUserCacheManager.cacheUsernameCheck(args[0])) {
                    lookupPlayer = pl.getUUID(args[0]);
                    if (lookupPlayer == (null)) {
                        sender.sendMessage("§cThat player does not exist!");
                        return true;
                    }

                    isPlayerALookup = true;
                    //add user to cache to not be constantly looking up
                    SQLiteUserCacheManager.cacheAdd(lookupPlayer,args[0]);

                } else {
                    //use offline player if player was found in said cache
                    offlineTarget = SQLiteUserCacheManager.cacheUsernameGet(args[0]);
                }
            }

            try {
                endDate = LocalDate.now().plusDays(Long.parseLong(day)).toString();
            } catch (NumberFormatException ignored) {
                sender.sendMessage(ConfigManager.getConfigString("mute.errors.invalid-int"));
                return true;
            }

            if (sender instanceof ConsoleCommandSender) {

                if (isPlayerALookup) {
                    SQLiteMuteManager.addConsoleMute(lookupPlayer, args[0], reason, startDate, endDate);
                    log.info("&9Player &r&f" + args[0] + " &9was muted with reason: &r&c" + reason + "&r&9 until: &r&f" + endDate + "&r&9.");
                } else if (offlineTarget != null) {
                    SQLiteMuteManager.addConsoleMute(offlineTarget, args[0], reason, startDate, endDate);
                    log.info("&9Player &r&f" + args[0] + " &9was muted with reason: &r&c" + reason + "&r&9 until: &r&f" + endDate + "&r&9.");
                } else {
                    SQLiteMuteManager.addConsoleMute(target, reason, startDate, endDate);
                    log.info("&9Player &r&f" + target.getName() + " &9was muted with reason: &r&c" + reason + "&r&9 until: &r&f" + endDate + "&r&9.");
                }
            } else if (sender instanceof Player) {
                cmdSender = (Player) sender;

                if (isPlayerALookup) {
                    SQLiteMuteManager.addMute(lookupPlayer, args[0], (Player) sender, reason, startDate, endDate);
                    cmdSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9Player &r&f" + args[0] + "&r&9 was muted with reason: &r&c" + reason + "&r&9 until: &r" + endDate + "&r&9."));
                    log.info("&9Player &r&f" + args[0] + " &9was muted with reason: &r&c" + reason + "&r&9 until: &r&f" + endDate + "&r&9.");
                } else if (offlineTarget != null) {
                    SQLiteMuteManager.addMute(offlineTarget, (Player) sender, reason, startDate, endDate);
                    cmdSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9Player &r&f" + args[0] + "&r&9 was muted with reason: &r&c" + reason + "&r&9 until: &r" + endDate + "&r&9."));
                    log.info("&9Player &r&f" + args[0] + " &9was muted with reason: &r&c" + reason + "&r&9 until: &r&f" + endDate + "&r&9.");
                } else {
                    SQLiteMuteManager.addMute(target, (Player) sender, reason, startDate, endDate);
                    cmdSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9Player &r&f" + target.getName() + "&r&9 was muted with reason: &r&c" + reason + "&r&9 until: &r" + endDate + "&r&9."));
                    log.info("&9Player &r&f" + target.getName() + " &9was muted with reason: &r&c" + reason + "&r&9 until: &r&f" + endDate + "&r&9.");
                }
            }
        }
        return true;
    }
}
