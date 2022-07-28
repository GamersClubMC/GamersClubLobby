package me.gamersclub.lobby.util.configuration;

import java.util.List;

import static me.gamersclub.lobby.GamersClubMain.plugin;

public class ConfigManager {

    public static String getConfigString(String value) {
        return plugin.getConfig().getString(value);
    }

    public static Boolean getConfigBoolean(String value) {
        return plugin.getConfig().getBoolean(value);
    }

    public static double getConfigDouble(String value) {
        return plugin.getConfig().getDouble(value);
    }

    public static float getConfigFloat(String value) {
        return (float) plugin.getConfig().getDouble(value);
    }

    public static List<String> getConfigStringList(String value) {
        return plugin.getConfig().getStringList(value);
    }
}
