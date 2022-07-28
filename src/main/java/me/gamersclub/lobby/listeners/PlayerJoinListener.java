package me.gamersclub.lobby.listeners;

import me.gamersclub.lobby.storage.SQLiteUserCacheManager;
import me.gamersclub.lobby.util.configuration.ConfigManager;
import me.gamersclub.lobby.util.items.ItemStackFactory;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerJoinListener implements Listener {
    private final ItemStackFactory isf = new ItemStackFactory();


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) throws NullPointerException {
        Player player = Bukkit.getPlayer(event.getPlayer().getName());
        assert player != null;

        event.setJoinMessage("");

        //lobby warp location
        double x = ConfigManager.getConfigDouble("general.lobby.x");
        double y = ConfigManager.getConfigDouble("general.lobby.y");
        double z = ConfigManager.getConfigDouble("general.lobby.x");
        float pitch = ConfigManager.getConfigFloat("general.lobby.pitch");
        float yaw = ConfigManager.getConfigFloat("general.lobby.yaw");
        String world = ConfigManager.getConfigString("general.lobby.world");
        Location spawn = new Location((Bukkit.getWorld(world)),x,y,z,pitch,yaw);

        //server selector strings
        String item = ConfigManager.getConfigString("server-selector.item");
        String name = ConfigManager.getConfigString("server-selector.name");

        //warp to spawn
        player.teleport(spawn, PlayerTeleportEvent.TeleportCause.PLUGIN);

        //reset inv
        player.getInventory().clear();
        player.getInventory().setItem(4,isf.createItem(item,name,ConfigManager.getConfigStringList("server-selector.lore")));

        //reset status effects
        player.getActivePotionEffects().clear();
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,999999,2,false,false));
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setCompassTarget(spawn);
        player.setTotalExperience(0);

        SQLiteUserCacheManager.cacheAdd(player.getUniqueId(),player.getName());
    }
}
