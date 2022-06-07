package me.gamersclub.lobby.listeners;

import me.gamersclub.lobby.util.configuration.ConfigManager;
import me.gamersclub.lobby.util.items.ItemStackFactory;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

public class PlayerRespawnListener implements Listener {
    private final ItemStackFactory isf = new ItemStackFactory();

    @EventHandler
    public void onPlayerRespawn (PlayerRespawnEvent event) {
        Player player = event.getPlayer();

        //lobby warp location
        double x = ConfigManager.getConfigDouble("general.lobby.x");
        double y = ConfigManager.getConfigDouble("general.lobby.y");
        double z = ConfigManager.getConfigDouble("general.lobby.x");
        float pitch = ConfigManager.getConfigFloat("general.lobby.pitch");
        float yaw = ConfigManager.getConfigFloat("general.lobby.yaw");
        String world = ConfigManager.getConfigString("general.lobby.world");
        Location spawn = new Location((Bukkit.getWorld(world)),x,y,z,pitch,yaw);

        //server selector strings
        ArrayList<String> lore = new ArrayList<>();
        String item = ConfigManager.getConfigString("server-selector.item");
        String name = ConfigManager.getConfigString("server-selector.name");
        String lore1 = ConfigManager.getConfigString("server-selector.lore1");
        String lore2 = ConfigManager.getConfigString("server-selector.lore2");
        lore.add(lore1);
        lore.add(lore2);


        //reset status effects
        player.getActivePotionEffects().clear();
        player.getInventory().clear();
        player.getInventory().setItem(4,isf.createItem(item,name,lore));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,999999,2,false,false));

        player.teleport(spawn,PlayerTeleportEvent.TeleportCause.PLUGIN);
        player.teleport(spawn,PlayerTeleportEvent.TeleportCause.PLUGIN);
    }
}
