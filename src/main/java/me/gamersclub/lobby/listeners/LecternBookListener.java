package me.gamersclub.lobby.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTakeLecternBookEvent;

public class LecternBookListener implements Listener {

    @EventHandler
    public void onLecternBookTake (PlayerTakeLecternBookEvent event) {
        if (!event.getPlayer().hasPermission("gclp.lectern.bypass")) {
            event.setCancelled(true);
        }
    }
}
