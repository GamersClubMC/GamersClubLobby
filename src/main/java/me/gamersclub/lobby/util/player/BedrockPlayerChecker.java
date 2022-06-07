package me.gamersclub.lobby.util.player;

import org.geysermc.floodgate.api.FloodgateApi;

import java.util.UUID;

public class BedrockPlayerChecker {

    public static boolean isBedrockPlayer(UUID uuid)  {
        return FloodgateApi.getInstance().isFloodgatePlayer(uuid);
    }
}
