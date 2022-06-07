package me.gamersclub.lobby.listeners;

import me.gamersclub.lobby.util.configuration.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceListener implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) throws NullPointerException {
        if (!event.getPlayer().hasPermission("gclp.bypass-block-placing")) {
            event.setCancelled(true);
            Player player = Bukkit.getPlayer(event.getPlayer().getName());
            assert player != null;
            player.sendMessage(ChatColor.RED + ConfigManager.getConfigString("general.build-disabled"));
        }
    }
}
