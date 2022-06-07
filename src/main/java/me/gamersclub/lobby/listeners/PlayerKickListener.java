package me.gamersclub.lobby.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;

public class PlayerKickListener implements Listener {

    @EventHandler
    public void onKick (PlayerKickEvent event) {
        event.setLeaveMessage("");
    }
}
