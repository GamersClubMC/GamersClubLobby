package me.gamersclub.lobby.listeners;

import me.gamersclub.lobby.util.configuration.ConfigManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import static me.gamersclub.lobby.GamersClubMain.isMaintenanceEnabled;

public class PlayerPreLoginListener implements Listener {

    @EventHandler
    public void onLogin(PlayerLoginEvent event) throws NullPointerException {
        if (isMaintenanceEnabled && !event.getPlayer().hasPermission("gclp.maintenance.bypass")) {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, ConfigManager.getConfigString("maintenance.kick-msg"));
        }
        else {
            event.allow();
        }

    }
}
