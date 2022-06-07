package me.gamersclub.lobby.util.player;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

public class PlayerLookup {

    private final Plugin plugin = Bukkit.getPluginManager().getPlugin("GamersClubLobbyPlugin");

    public UUID getUUID(String username) {

        final UUID[] cache = {null};

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                HttpURLConnection connection = getGetConnection("https://api.mojang.com/users/profiles/minecraft/" + username);

                int status = connection.getResponseCode();

                Reader streamReader;

                if (status > 299) {
                    return;
                } else {
                    streamReader = new InputStreamReader(connection.getInputStream());
                }

                BufferedReader reader = new BufferedReader(streamReader);
                String line = reader.readLine();

                reader.close();
                connection.disconnect();

                cache[0] = formatUUID(StringUtils.substringBetween(line, "{", "}").split(",")[1].split(":")[1].replaceAll("\"", ""));
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        });
        return cache[0];
    }

    private UUID formatUUID(String uuid){
        if(uuid.length() != 32){
            return null;
        }

        String first = uuid .substring(0,8);
        String second = uuid.substring(8,12);
        String third = uuid.substring(12,16);
        String fourth = uuid.substring(16,20);
        String fifth = uuid.substring(20);

        return UUID.fromString(first+"-"+second+"-"+third+"-"+fourth+"-"+fifth);
    }

    private HttpURLConnection getGetConnection(String url) throws IOException {
        URL statusURL = new URL(url);

        HttpURLConnection connection = (HttpURLConnection) statusURL.openConnection();
        connection.setRequestMethod("GET");

        return connection;
    }

}
