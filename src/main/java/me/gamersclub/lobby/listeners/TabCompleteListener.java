package me.gamersclub.lobby.listeners;

import me.gamersclub.lobby.storage.SQLiteMuteManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.TabCompleteEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TabCompleteListener implements Listener {

    @EventHandler
    public void onTabComplete(TabCompleteEvent event) {
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
            List<String> mutedList = SQLiteMuteManager.getUsernames();

            if (mutedList.isEmpty()) {
                event.setCancelled(true);
            } else {
                event.setCompletions(mutedList);
            }
        }
    }
}
