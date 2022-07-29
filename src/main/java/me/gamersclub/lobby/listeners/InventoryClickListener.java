package me.gamersclub.lobby.listeners;

import me.gamersclub.lobby.guis.AdminMenu;
import me.gamersclub.lobby.guis.MuteGUI;
import me.gamersclub.lobby.guis.ServerSelector;
import me.gamersclub.lobby.storage.SQLiteLogManager;
import me.gamersclub.lobby.storage.SQLiteMuteManager;
import me.gamersclub.lobby.storage.SQLiteUserCacheManager;
import me.gamersclub.lobby.util.configuration.ConfigManager;
import me.gamersclub.lobby.util.logging.GamersClubLogger;
import org.bukkit.*;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.meta.SkullMeta;

import java.time.LocalDate;

import static me.gamersclub.lobby.GamersClubMain.isServer1_18_2OrAbove;

@SuppressWarnings("ConstantConditions")
public class InventoryClickListener implements Listener {
    private final MuteGUI mute = new MuteGUI();
    private final AdminMenu am = new AdminMenu();
    private final ServerSelector srv = new ServerSelector();
    private final GamersClubLogger log = new GamersClubLogger();

    @EventHandler
    public void onInvClick(InventoryClickEvent event) throws NullPointerException {
        final HumanEntity humanEntity = event.getWhoClicked(); // The player that clicked the item
        final Player player = (Player) event.getWhoClicked(); // Also, the player that clicked the item
        Player target;

        if (event.getView().getTitle().equalsIgnoreCase(ConfigManager.getConfigString("server-selector.name"))) {
            event.setCancelled(true);
            if (event.getSlot() == 8) {
                humanEntity.closeInventory();
            } else if (event.getSlot() == 11) {
                player.performCommand("anarchy");
                humanEntity.closeInventory();

            } else if (event.getSlot() == 13) {
                player.performCommand("lobby");
                humanEntity.closeInventory();
            } else if (event.getSlot() == 15) {
                player.teleport(new Location((Bukkit.getWorld("world")),0.5, 72, 0.5,135,60), PlayerTeleportEvent.TeleportCause.PLUGIN);
                humanEntity.closeInventory();
            } else if (event.getSlot() == 26 && player.hasPermission("gclp.admin")) {
                humanEntity.closeInventory();
                humanEntity.openInventory(am.adminMenu());
            }
        } else if (event.getView().getTitle().equalsIgnoreCase(ConfigManager.getConfigString("admin-gui.name"))) {
            event.setCancelled(true);
            if (event.getSlot() == 8) {
                humanEntity.closeInventory();
            } else if (event.getSlot() == 11) {
                humanEntity.closeInventory();
                humanEntity.openInventory(mute.MuteUI());
            } else if (event.getSlot() == 13) {
                humanEntity.closeInventory();
                humanEntity.openInventory(mute.UnmuteUI());
            } else if (event.getSlot() == 15) {
                humanEntity.closeInventory();
                humanEntity.openInventory(srv.ServerSelectorUI());
            }
        } else if (event.getView().getTitle().equalsIgnoreCase(ConfigManager.getConfigString("mute.name"))) {
            event.setCancelled(true);

            if (event.getSlot() == 8) {
                humanEntity.closeInventory();
            }

            if (event.getCurrentItem() == null) {
                return;
            }

            if (isServer1_18_2OrAbove) {
                if (!event.getCurrentItem().getType().equals(Material.PLAYER_HEAD)) {
                    return;
                }

                SkullMeta skullMeta = (SkullMeta) event.getCurrentItem().getItemMeta();
                target = Bukkit.getPlayer(skullMeta.getOwningPlayer().getUniqueId());
            } else {
                if (!event.getCurrentItem().getType().equals(Material.getMaterial(ConfigManager.getConfigString("mute.fallback-item")))) {
                    return;
                }
                target = Bukkit.getPlayer(event.getCurrentItem().getItemMeta().getDisplayName().substring(4));
            }

            humanEntity.closeInventory();
            mute.ReasonUI(player,target);

        }
        else if (event.getView().getTitle().equalsIgnoreCase(ConfigManager.getConfigString("unmute.name"))) {
            event.setCancelled(true);

            if (event.getSlot() == 8) {
                humanEntity.closeInventory();
            }

            if (event.getCurrentItem() == null) {
                return;
            }
            if (!event.getCurrentItem().getType().equals(Material.PAPER)) {
                return;
            }



            String targetName = event.getCurrentItem().getItemMeta().getDisplayName();

            OfflinePlayer offlinePlayer = SQLiteUserCacheManager.cacheUsernameGet(targetName);
            SQLiteMuteManager.deleteMute(offlinePlayer.getUniqueId());


            humanEntity.closeInventory();

            if (!Bukkit.getPlayerExact(targetName).isOnline()) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9Player &r&f"+ targetName + "&r&9 was unmuted!"));
                log.info("&9Player &r&f"+ targetName + " &9was unmuted.");
                SQLiteLogManager.addLog(offlinePlayer.getUniqueId().toString(),targetName,player.getUniqueId().toString(),player.getName(),"(No reason specified)", LocalDate.now().toString(),"UNMUTE");
            }
            else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9Player &r&f"+ targetName + "&r&9 was unmuted!"));
                log.info("&9Player &r&f"+ targetName + " &9was unmuted.");

                target = Bukkit.getPlayer(offlinePlayer.getUniqueId());
                target.sendMessage(ChatColor.translateAlternateColorCodes('&',ConfigManager.getConfigString("unmute.unmuted-player")));
                SQLiteLogManager.addLog(target.getUniqueId().toString(),target.getName(),player.getUniqueId().toString(),player.getName(),"(No reason specified)", LocalDate.now().toString(),"UNMUTE");
            }

        }

    }
}
