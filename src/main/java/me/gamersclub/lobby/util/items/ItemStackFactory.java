package me.gamersclub.lobby.util.items;

import me.gamersclub.lobby.util.configuration.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static me.gamersclub.lobby.GamersClubMain.isServer1_18_2OrAbove;

public class ItemStackFactory {


    public ItemStack createPlayerHead(Player p,String lore) {
        ItemStack itemStack;
        Material material;

        //see GamersClubMain#isOrIsAbove1_18_2
        if (isServer1_18_2OrAbove) {
            material = Material.PLAYER_HEAD;
            itemStack = new ItemStack(material, 1);
            SkullMeta skull = (SkullMeta) itemStack.getItemMeta();
            PlayerProfile playerProfile = Bukkit.createPlayerProfile(p.getUniqueId(),p.getName());
            assert skull != null;

            skull.setDisplayName("§e§l" + p.getName());
            skull.setOwnerProfile(playerProfile);
            skull.setLore(new ArrayList<>(Collections.singleton(lore)));
            itemStack.setItemMeta(skull);
        } else {
            material = Material.getMaterial(ConfigManager.getConfigString("mute.fallback-item"));
            itemStack = new ItemStack(Objects.requireNonNull(material), 1);
            ItemMeta itemMeta = itemStack.getItemMeta();

            assert itemMeta != null;
            itemMeta.setDisplayName("§e§l" + p.getName());
            itemMeta.setLore(new ArrayList<>(Collections.singleton(lore)));
            itemStack.setItemMeta(itemMeta);
        }

        return itemStack;
    }

    //item with multiple lines of lore
    public ItemStack createItem(String material, String name,List<String> lore) {
        ItemStack itemStack = new ItemStack(Objects.requireNonNull(Material.getMaterial(material)),1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.setDisplayName(name);
            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);
        }
        return itemStack;
    }

    //item with 1 line of lore
    public ItemStack createItem(String material,String name,String lore) {
        ItemStack itemStack = new ItemStack(Objects.requireNonNull(Material.getMaterial(material)),1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.setDisplayName(name);
            itemMeta.setLore(new ArrayList<>(Collections.singleton(lore)));
            itemStack.setItemMeta(itemMeta);
        }
        return itemStack;
    }

    //no lore item
    public ItemStack createItem(String material,String name) {
        ItemStack itemStack = new ItemStack(Objects.requireNonNull(Material.getMaterial(material)),1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.setDisplayName(name);
            itemStack.setItemMeta(itemMeta);
        }
        return itemStack;
    }

    //bg item
    public ItemStack background() {
        ItemStack glass = new ItemStack(Objects.requireNonNull(Material.getMaterial(ConfigManager.getConfigString("general.background"))),1);
        ItemMeta glassMeta = glass.getItemMeta();
        if (glassMeta != null) {
            glassMeta.setDisplayName(ConfigManager.getConfigString("general.no-name"));
            glass.setItemMeta(glassMeta);
        }
        return glass;
    }

    //exit item
    public ItemStack exit() {
        ItemStack exit = new ItemStack(Objects.requireNonNull(Material.getMaterial(ConfigManager.getConfigString("general.exit.java.item"))),1);
        ItemMeta exitMeta = exit.getItemMeta();
        if (exitMeta != null) {
            exitMeta.setDisplayName(ConfigManager.getConfigString("general.exit.java.name"));
            exitMeta.setLore(new ArrayList<>(Collections.singleton(ConfigManager.getConfigString("general.exit.java.lore"))));
            exit.setItemMeta(exitMeta);
        }
        return exit;
    }
}
