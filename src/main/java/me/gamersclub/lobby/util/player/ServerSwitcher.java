package me.gamersclub.lobby.util.player;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Objects;

public class ServerSwitcher {

    public void switchServer(Player player, String server) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(server);
        player.sendPluginMessage(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("GamersClubLobbyPlugin")), "BungeeCord", out.toByteArray());
    }
}
