package me.gamersclub.lobby.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCreativeEvent;

public class InventoryCreativeListener implements Listener {

    @EventHandler
    public void onInvCreative(InventoryCreativeEvent event) throws NullPointerException  {
        event.setCancelled(true);
    }
}
