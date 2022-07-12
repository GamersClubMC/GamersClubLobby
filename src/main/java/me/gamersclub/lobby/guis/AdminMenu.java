package me.gamersclub.lobby.guis;

import me.gamersclub.lobby.util.configuration.ConfigManager;
import me.gamersclub.lobby.util.items.ItemStackFactory;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.floodgate.api.FloodgateApi;

import java.util.UUID;

public class AdminMenu {
    private final ItemStackFactory item = new ItemStackFactory();

    public Inventory adminMenu() {
        Inventory adminMenu = Bukkit.createInventory(null, 27, ConfigManager.getConfigString("admin-gui.name"));

        //Mute button
        String muteItem = ConfigManager.getConfigString("admin-gui.java.mute.item");
        String muteName = ConfigManager.getConfigString("admin-gui.java.mute.name");
        String muteLore = ConfigManager.getConfigString("admin-gui.java.mute.lore");

        //Unmute button
        String unmuteItem = ConfigManager.getConfigString("admin-gui.java.unmute.item");
        String unmuteName = ConfigManager.getConfigString("admin-gui.java.unmute.name");
        String unmuteLore = ConfigManager.getConfigString("admin-gui.java.unmute.lore");

        //Server Selector button
        String srvSelectItem = ConfigManager.getConfigString("server-selector.item");
        String srvSelectName = ConfigManager.getConfigString("server-selector.name");
        String srvSelectLore = ConfigManager.getConfigString("admin-gui.srv-select-lore");

        //Fill in the background with the glass background.
        for (int i = 0; i < 27; i++) {
            adminMenu.setItem(i,item.background());
        }

        adminMenu.setItem(8, item.exit());
        adminMenu.setItem(11,item.createItem(muteItem,muteName,muteLore));
        adminMenu.setItem(13,item.createItem(unmuteItem,unmuteName, unmuteLore));
        adminMenu.setItem(15,item.createItem(srvSelectItem,srvSelectName, srvSelectLore));

        return adminMenu;
    }

    public void AdminForm(UUID uuid) {
        SimpleForm.Builder form = SimpleForm.builder()
            .title(ConfigManager.getConfigString("admin-gui.name"))
            .button(ConfigManager.getConfigString("admin-gui.bedrock.mute"))
            .button(ConfigManager.getConfigString("admin-gui.bedrock.unmute"))
            .button(ConfigManager.getConfigString("general.exit.bedrock.name"));

        form.closedOrInvalidResultHandler(() -> {
        });

        form.validResultHandler(response -> {
            if (response.clickedButtonId() == 0) {
                MuteGUI.MuteForm(uuid);
            }
            else if (response.clickedButtonId() == 1) {
                MuteGUI.UnmuteForm(uuid);
            }
        });

        FloodgateApi.getInstance().sendForm(uuid,form);
    }
}
