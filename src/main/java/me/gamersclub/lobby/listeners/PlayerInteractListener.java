package me.gamersclub.lobby.listeners;

import me.gamersclub.lobby.guis.ServerSelector;
import me.gamersclub.lobby.util.configuration.ConfigManager;
import me.gamersclub.lobby.util.items.ItemStackFactory;
import me.gamersclub.lobby.util.player.BedrockPlayerChecker;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;

public class PlayerInteractListener implements Listener {
    private final ItemStackFactory isf = new ItemStackFactory();
    private final ServerSelector srv = new ServerSelector();


    @EventHandler
    public void onItemUse(PlayerInteractEvent event) throws NullPointerException {

        Player player = event.getPlayer();

        ArrayList<String> lore = new ArrayList<>();
        String item = ConfigManager.getConfigString("server-selector.item");
        String name = ConfigManager.getConfigString("server-selector.name");
        String lore1 = ConfigManager.getConfigString("server-selector.lore1");
        String lore2 = ConfigManager.getConfigString("server-selector.lore2");
        lore.add(lore1);
        lore.add(lore2);

        if (event.getItem() == null) {
            return;
        }

        //srv selector item
        if (event.getItem().equals(isf.createItem(item,name,lore))) {
            HumanEntity humanEntity = event.getPlayer();
            if (BedrockPlayerChecker.isBedrockPlayer(player.getUniqueId())) {
                ServerSelector.ServerSelectorForm(player.getUniqueId());
            }
            else {
                humanEntity.openInventory(srv.ServerSelectorUI());
            }
        }
    }
}
