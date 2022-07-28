package me.gamersclub.lobby.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.gamersclub.lobby.storage.SQLiteMuteManager;
import me.gamersclub.lobby.util.configuration.ConfigManager;
import me.gamersclub.lobby.util.logging.MutedPlayerLogger;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

import static me.gamersclub.lobby.GamersClubMain.logMutes;

public class AsyncChatListener implements Listener {
    private final MutedPlayerLogger mutedPlayerLogger = new MutedPlayerLogger();

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void muteCheck(@NotNull AsyncChatEvent event) throws NullPointerException {
        UUID uuid = event.getPlayer().getUniqueId();
        String endDate = SQLiteMuteManager.infoMute(uuid, "ENDDATE");
        try {
            Date dataDate = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
            String curDate = LocalDate.now().toString();
            Date currDate = new SimpleDateFormat("yyyy-MM-dd").parse(curDate);
            if (dataDate.compareTo(currDate) > 0) {
                if (logMutes) {
                    String message = PlainTextComponentSerializer.plainText().serialize(event.message());
                    mutedPlayerLogger.logMute(uuid, message, endDate);
                }
                event.setCancelled(true);
                event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&4You are muted until: &r&f" + endDate + "&r&4!"));
            } else if (endDate.equals("2019-01-01")) {
                event.setCancelled(false);
            } else if (dataDate.compareTo(currDate) < 0) {
                SQLiteMuteManager.deleteMute(event.getPlayer().getUniqueId());
                event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getConfigString("unmute.mute-expired")));
            }
        } catch (ParseException | NullPointerException e) {
            throw new RuntimeException(e);
        }
    }
}
