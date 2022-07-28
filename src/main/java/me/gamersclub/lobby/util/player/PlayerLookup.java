package me.gamersclub.lobby.util.player;

import org.geysermc.floodgate.api.FloodgateApi;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class PlayerLookup {
    private final String notInCache = "Unable to find user in our cache. Please try specifying their Floodgate UUID instead";
    private final String mojangAPI = "https://api.mojang.com/users/profiles/minecraft/{}";
    private final String geyserAPI = "https://api.geysermc.org/v2/xbox/xuid/{}";

    public UUID getBedrockUUID(@NotNull String username) {
        String prefix = FloodgateApi.getInstance().getPlayerPrefix();
        if (username.contains(prefix)) {
            username = username.replaceFirst(prefix,"");
        }

        String textFromUrl = jsonFromURL(geyserAPI.replace("{}", username));
        if (textFromUrl != null) {
            JSONObject jsonObject = new JSONObject(textFromUrl);

            if (jsonObject.has("message") && jsonObject.getString("message").equalsIgnoreCase(notInCache)) {
                //player does not exist or is not in the api cache
                return null;
            }

            return FloodgateApi.getInstance().createJavaPlayerId((long) jsonObject.get("xuid"));
        }
        return null;
    }

    public UUID getUUID(String username) {
        String textFromUrl = jsonFromURL(mojangAPI.replace("{}", username));
        if (textFromUrl != null) {
            if (textFromUrl.equalsIgnoreCase("")) {
                return getBedrockUUID(username);
            }

            JSONObject jsonObject = new JSONObject(textFromUrl);
            return formatUUID(jsonObject.get("id").toString());
        }
        return null;
    }

    public String jsonFromURL(String url) throws JSONException {
        try {
            try (InputStream inputStream = new URL(url).openStream()) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

                return readAll(bufferedReader);
            }
        } catch (IOException e) {
            return null;
        }
    }

    private @NotNull String readAll(@NotNull BufferedReader bufferedReader) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        int count;
        while ((count = bufferedReader.read()) != -1) {
            stringBuilder.append((char) count);
        }
        return stringBuilder.toString();
    }

    private @Nullable UUID formatUUID(@NotNull String uuid){
        if(uuid.length() != 32){
            return null;
        }

        String first = uuid .substring(0,8);
        String second = uuid.substring(8,12);
        String third = uuid.substring(12,16);
        String fourth = uuid.substring(16,20);
        String fifth = uuid.substring(20);

        return UUID.fromString(first + "-" + second + "-" + third + "-" + fourth + "-" + fifth);
    }

}
