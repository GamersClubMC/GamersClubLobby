package me.gamersclub.lobby.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.TabCompleteEvent;

import java.util.ArrayList;
import java.util.Arrays;

public class TabCompleteListener implements Listener {

    @EventHandler
    public void onTabComplete(TabCompleteEvent event) {
        if (event.getBuffer().equalsIgnoreCase("/gclp ")) {
            event.setCompletions(new ArrayList<>(Arrays.asList("anarchy", "help", "lobby")));
        }
        else if (event.getBuffer().equalsIgnoreCase("/help ")) {
            event.setCancelled(true);
        }
        else if (event.getBuffer().equalsIgnoreCase("/anarchy ")) {
            event.setCancelled(true);
        }
        else if (event.getBuffer().equalsIgnoreCase("/lobby ")) {
            event.setCancelled(true);
        }
        else if (event.getBuffer().equalsIgnoreCase("/serverselector ")) {
            event.setCancelled(true);
        }
        else if (event.getBuffer().equalsIgnoreCase("/maintenance ")) {
            event.setCompletions(new ArrayList<>(Arrays.asList("on", "off")));
        }
    }
}
