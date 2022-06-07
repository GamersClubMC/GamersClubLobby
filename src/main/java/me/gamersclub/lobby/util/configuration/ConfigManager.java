package me.gamersclub.lobby.util.configuration;

import org.bukkit.Bukkit;

import java.util.List;
import java.util.Objects;

public class ConfigManager {
    private final static String NAME = "GamersClubLobbyPlugin";

    public static String getConfigString(String value) {
        return Objects.requireNonNull(Bukkit.getPluginManager().getPlugin(NAME)).getConfig().getString(value);
    }

    //reserved for future config changes
    public static Boolean getConfigBoolean(String value) {
        return Objects.requireNonNull(Bukkit.getPluginManager().getPlugin(NAME)).getConfig().getBoolean(value);
    }

    public static double getConfigDouble(String value) {
        return Objects.requireNonNull(Bukkit.getPluginManager().getPlugin(NAME)).getConfig().getDouble(value);
    }

    public static float getConfigFloat(String value) {
        return (float) Objects.requireNonNull(Bukkit.getPluginManager().getPlugin(NAME)).getConfig().getInt(value);
    }

    public static List<String> getConfigStringList(String value) {
        return Objects.requireNonNull(Bukkit.getPluginManager().getPlugin(NAME)).getConfig().getStringList(value);
    }

    public static String getConfigPrefix() {
        return Objects.requireNonNull(Bukkit.getPluginManager().getPlugin(NAME)).getConfig().getString("settings.prefix");
    }
}
