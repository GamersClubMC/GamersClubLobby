package me.gamersclub.lobby.listeners;

import me.gamersclub.lobby.util.configuration.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) throws NullPointerException {
        if (!event.getPlayer().hasPermission("gclp.bypass-block-breaking")) {
            event.setCancelled(true);
            Player player = Bukkit.getPlayer(event.getPlayer().getName());
            assert player != null;
            player.sendMessage(ChatColor.RED + ConfigManager.getConfigString("general.break-disabled"));
        }
    }
}
