package me.gamersclub.lobby.listeners;

import me.gamersclub.lobby.guis.ServerSelector;
import me.gamersclub.lobby.util.configuration.ConfigManager;
import me.gamersclub.lobby.util.items.ItemStackFactory;
import me.gamersclub.lobby.util.player.BedrockPlayerChecker;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import static me.gamersclub.lobby.GamersClubMain.openedFormList;
import static me.gamersclub.lobby.GamersClubMain.plugin;

public class PlayerInteractListener implements Listener {
    private final ItemStackFactory isf = new ItemStackFactory();
    private final ServerSelector srv = new ServerSelector();


    @EventHandler
    public void onItemUse(PlayerInteractEvent event) throws NullPointerException {
        Player player = event.getPlayer();
        String playerUUID = event.getPlayer().getUniqueId().toString();

        String item = ConfigManager.getConfigString("server-selector.item");
        String name = ConfigManager.getConfigString("server-selector.name");

        if (event.getItem() == null) {
            return;
        }

        //srv selector item
        if (event.getItem().equals(isf.createItem(item,name,ConfigManager.getConfigStringList("server-selector.lore")))) {
            HumanEntity humanEntity = event.getPlayer();
            if (BedrockPlayerChecker.isBedrockPlayer(player.getUniqueId())) {
                if (!openedFormList.contains(playerUUID)) {
                    //If the player is not in the form list, open the form and add them to said list.
                    ServerSelector.ServerSelectorForm(player.getUniqueId());
                    openedFormList.add(playerUUID);

                    //Remove the player from the list after 5 ticks.
                    final int listEntryNum = openedFormList.indexOf(playerUUID);
                    Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> openedFormList.remove(listEntryNum),5L);
                }
            } else {
                humanEntity.openInventory(srv.ServerSelectorUI());
            }
        }
    }
}
