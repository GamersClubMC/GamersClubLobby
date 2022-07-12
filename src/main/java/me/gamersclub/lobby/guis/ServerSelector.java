package me.gamersclub.lobby.guis;

import me.gamersclub.lobby.util.configuration.ConfigManager;
import me.gamersclub.lobby.util.items.ItemStackFactory;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.cumulus.util.FormImage;
import org.geysermc.floodgate.api.FloodgateApi;

import java.util.ArrayList;
import java.util.UUID;

public class ServerSelector {
    private final ItemStackFactory item = new ItemStackFactory();

    public Inventory ServerSelectorUI() {
        Inventory inv = Bukkit.createInventory(null, 27, ConfigManager.getConfigString("server-selector.name"));

        for(int i = 0; i < 27; i++) {
            inv.setItem(i,item.background());
        }

        ArrayList<String> anarchyLore = new ArrayList<>();
        String anarchyItem = ConfigManager.getConfigString("server-selector.anarchy.java.item");
        String anarchyName = ConfigManager.getConfigString("server-selector.anarchy.java.name");
        anarchyLore.add(ConfigManager.getConfigString("server-selector.anarchy.java.lore1"));
        anarchyLore.add(ConfigManager.getConfigString("server-selector.anarchy.java.lore2"));

        String lobbyItem = ConfigManager.getConfigString("server-selector.lobby.java.item");
        String lobbyName = ConfigManager.getConfigString("server-selector.lobby.java.name");
        String lobbyLore = ConfigManager.getConfigString("server-selector.lobby.java.lore");

        String noItem = ConfigManager.getConfigString("general.no-item");
        String noName = ConfigManager.getConfigString("general.no-name");

        inv.setItem(8, item.exit());
        inv.setItem(11,item.createItem(anarchyItem,anarchyName,anarchyLore));
        inv.setItem(13,item.createItem(lobbyItem,lobbyName,lobbyLore));
        inv.setItem(15,item.createItem(noItem,noName));

        return inv;
    }

    public static void ServerSelectorForm(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        boolean isAdmin = player.hasPermission("gclp.admin");

        //form info
        String title = ConfigManager.getConfigString("server-selector.name");
        String content = ConfigManager.getConfigString("server-selector.desc");

        //admin button
        String adminButton = ConfigManager.getConfigString("server-selector.admin-name");
        String adminImg = ConfigManager.getConfigString("server-selector.admin-image");

        //lobby button
        String lobbyButton = ConfigManager.getConfigString("server-selector.lobby.bedrock.name");
        String lobbyImg = ConfigManager.getConfigString("server-selector.lobby.bedrock.image");

        //anarchy button
        String anarchyButton = ConfigManager.getConfigString("server-selector.anarchy.bedrock.name");
        String anarchyImg = ConfigManager.getConfigString("server-selector.anarchy.bedrock.image");

        //exit button
        String exitButton = ConfigManager.getConfigString("general.exit.bedrock.name");
        String exitImg = ConfigManager.getConfigString("general.exit.bedrock.image");

        SimpleForm.Builder form = SimpleForm.builder()
            .title(title)
            .content(content)
            .optionalButton(adminButton, FormImage.Type.URL,adminImg,isAdmin) //rory!
            .button(lobbyButton, FormImage.Type.URL,lobbyImg)
            .button(anarchyButton,FormImage.Type.URL,anarchyImg)
            .button(exitButton,FormImage.Type.URL, exitImg);

        form.closedOrInvalidResultHandler(() -> {
        });

        form.validResultHandler(response -> {
            if (response.clickedButtonId() == 0) {
                player.performCommand("gclp admin");
            }
            else if (response.clickedButtonId() == 1) {
                player.performCommand("lobby");
            }
            else if (response.clickedButtonId() == 2) {
                player.performCommand("anarchy");
            }
        });

        FloodgateApi.getInstance().sendForm(uuid,form);
    }
}
