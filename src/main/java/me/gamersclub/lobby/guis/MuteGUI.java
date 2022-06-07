package me.gamersclub.lobby.guis;

import me.gamersclub.lobby.storage.SQLiteLogManager;
import me.gamersclub.lobby.storage.SQLiteMuteManager;
import me.gamersclub.lobby.storage.SQLiteStorageManager;
import me.gamersclub.lobby.util.configuration.ConfigManager;
import me.gamersclub.lobby.util.items.ItemStackFactory;
import me.gamersclub.lobby.util.logging.GamersClubLogger;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.geysermc.cumulus.CustomForm;
import org.geysermc.cumulus.response.CustomFormResponse;
import org.geysermc.floodgate.api.FloodgateApi;

import java.time.LocalDate;
import java.util.*;

public class MuteGUI {
    private final ItemStackFactory item = new ItemStackFactory();
    private final GamersClubLogger log = new GamersClubLogger();

    public Inventory MuteUI() {
        Inventory muteInv = Bukkit.createInventory(null, 54, ConfigManager.getConfigString("mute.name"));

        Player[] playerlist = Bukkit.getOnlinePlayers().toArray(new Player[0]);

        //Fill in the top with the glass background.
        for (int i = 0; i < 9; i++) {
            muteInv.setItem(i,item.background());
        }

        //Fill in inv with player heads
        for (int p = 0; p < playerlist.length; p++) {
            //Don't add more heads if we reach the limit of the inventory
            if (p+9 >= 54) {
                break;
            }
            ItemStack playerHead = item.createPlayerHead(playerlist[p],ConfigManager.getConfigString("mute.click-lore"));
            muteInv.setItem(p+9, playerHead);
        }

        muteInv.setItem(8,item.exit());

        return muteInv;
    }

    public Inventory UnmuteUI() {
        Inventory unmuteInv = Bukkit.createInventory(null, 54, ConfigManager.getConfigString("unmute.name"));

        String[] mutedList = SQLiteStorageManager.getUsernames().toArray(new String[0]);

        //Fill in the top with the glass background.
        for (int i = 0; i < 9; i++) {
            unmuteInv.setItem(i,item.background());
        }

        //reused code from mute ui
        for (int p = 0; p < mutedList.length; p++) {
            if (p+9 >= 54) {
                break;
            }

            ItemStack playerHead = item.createItem("PAPER",mutedList[p],ConfigManager.getConfigString("unmute.click-lore"));
            unmuteInv.setItem(p+9, playerHead);
        }
        unmuteInv.setItem(8,item.exit());

        return unmuteInv;
    }

     public void ReasonUI(Player player,Player target) {
        final String[] reason = new String[1];

        //thank you wesjd for making this it's a lifesaver
         new AnvilGUI.Builder()
                 .onClose(player1 -> reason[0] = "(No reason provided)")
                 .onComplete((player1, text) -> {
                     reason[0] = text;
                     String startDate = LocalDate.now().toString();
                     String endDate = LocalDate.now().plusDays(7).toString();
                     SQLiteMuteManager.addMute(target,player1,reason[0],startDate,endDate);
                     player1.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9Player &r&f" + target.getName() + "&r&9 was muted with reason: &r&c" + reason[0] + "&r&9 until: &r" + endDate + "&r&9."));
                     log.info("&9Player &r&f" + target.getName() + " &9was muted with reason: &r&c" + reason[0] + "&r&9 until: &r&f" + endDate + "&r&9.");
                     return AnvilGUI.Response.close();
                 })
                 .text("(No reason provided)")
                 .itemLeft(new ItemStack(Objects.requireNonNull(Material.getMaterial(ConfigManager.getConfigString("mute.item")))))
                 .title(ConfigManager.getConfigString("mute.reason-name"))
                 .plugin(Bukkit.getPluginManager().getPlugin("GamersClubLobbyPlugin"))
                 .open(player);
    }

    //fancy mute thing for bedrock, credit to GeyserAdminTools for the base code
    public static void MuteForm(UUID uuid) {
        GamersClubLogger log = new GamersClubLogger();

        Player player = Bukkit.getPlayer(uuid);
        List<String> names = Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
        String[] playerList = names.toArray(new String[0]);

        FloodgateApi.getInstance().sendForm(uuid,
            CustomForm.builder()
                .title(ConfigManager.getConfigString("mute.name"))
                .dropdown(ConfigManager.getConfigString("mute.player-menu"), playerList)
                .input(ConfigManager.getConfigString("mute.time-input"))
                .input(ConfigManager.getConfigString("mute.time-menu"))

                .responseHandler((form, responseData) -> {
                    CustomFormResponse response = form.parseResponse(responseData);
                    if (!response.isCorrect())
                        return;

                    int clickedIndex = response.getDropdown(0);
                    String day = response.getInput(1);
                    String endDate;

                    try {
                        assert day != null;
                        endDate = LocalDate.now().plusDays(Long.parseLong(day)).toString();
                    }
                    catch (Exception e) {
                        assert player != null;
                        player.sendMessage(ChatColor.RED + "Invalid number!");
                        return;
                    }
                    String reason = response.getInput(2);
                    String name = names.get(clickedIndex);
                    Player target = Bukkit.getPlayer(name);
                    String startDate = LocalDate.now().toString();
                    //database code
                    assert target != null;
                    SQLiteMuteManager.addMute(target,player,reason,startDate,endDate);

                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9Player &r&f"+ target.getName() + "&r&9 was muted for: &r&c" + reason + "&r&9 until: &r" + endDate + "&r&9."));
                    log.info("&9Player &r&f"+ target.getName() + " &9was muted with reason: &r&c" + reason + "&r&9 until: &f" + endDate + "&r&9.");
                })
        );
    }

    //also credit to GeyserAdminTools
    public static void UnmuteForm(UUID uuid) {
        GamersClubLogger log = new GamersClubLogger();
        Player player = Bukkit.getPlayer(uuid);

        String[] playerlist = SQLiteStorageManager.getUsernames().toArray(new String[0]);

        FloodgateApi.getInstance().sendForm(uuid,
            CustomForm.builder()
                .title(ConfigManager.getConfigString("unmute.name"))
                .dropdown(ConfigManager.getConfigString("unmute.player-menu"), playerlist)
                .responseHandler((form, responseData) -> {
                    CustomFormResponse response = form.parseResponse(responseData);
                    if (!response.isCorrect()) {
                        return;
                    }

                    if (playerlist.length == 0) {
                        return;
                    }

                    int clickedIndex = response.getDropdown(0);
                    String name = SQLiteStorageManager.getUsernames().get(clickedIndex);
                    Player target = Bukkit.getPlayer(name);

                    SQLiteMuteManager.deleteMute(target.getUniqueId());
                    Bukkit.getPlayer(uuid).sendMessage(ChatColor.translateAlternateColorCodes('&', "&9Player &r&f"+ target.getName() + "&r&9 was unmuted!"));
                    log.info("&9Player &r&f"+ target.getName() + " &9was unmuted.");
                    target.sendMessage(ChatColor.translateAlternateColorCodes('&',ConfigManager.getConfigString("unmute.unmuted-player")));

                    SQLiteLogManager.addLog(target.getUniqueId().toString(),target.getName(),player.getUniqueId().toString(),player.getName(),"(No reason specified)", LocalDate.now().toString(),"UNMUTE");
                })
        );
    }
}
