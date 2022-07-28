package me.gamersclub.lobby.listeners;

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent;
import me.gamersclub.lobby.storage.SQLiteMuteManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.Arrays;

public class AsyncTabCompleteListener implements Listener {

    @EventHandler
    public void onAsyncTabComplete(AsyncTabCompleteEvent event) {
        if (event.getBuffer().contains("/gclp ")) {
            event.setCompletions(new ArrayList<>(Arrays.asList("anarchy", "help", "lobby")));
        } else if (event.getBuffer().contains("/help ")) {
            event.setCancelled(true);
        } else if (event.getBuffer().contains("/anarchy ")) {
            event.setCancelled(true);
        } else if (event.getBuffer().contains("/lobby ")) {
            event.setCancelled(true);
        } else if (event.getBuffer().contains("/serverselector ")) {
            event.setCancelled(true);
        } else if (event.getBuffer().contains("/maintenance ")) {
            event.setCompletions(new ArrayList<>(Arrays.asList("on", "off")));
        } else if (event.getBuffer().contains("/unmute ")) {
            event.setCompletions(SQLiteMuteManager.getUsernames());
        }
    }
}
