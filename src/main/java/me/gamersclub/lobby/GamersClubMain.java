package me.gamersclub.lobby;

import me.gamersclub.lobby.commands.*;
import me.gamersclub.lobby.listeners.*;
import me.gamersclub.lobby.storage.SQLiteStorageManager;
import me.gamersclub.lobby.util.logging.GamersClubLogger;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;

public class GamersClubMain extends JavaPlugin {
    public static boolean isMaintenanceEnabled = false;
    public static boolean playersAlreadyKicked = false;
    private final GamersClubLogger log = new GamersClubLogger();

    @Override
    public void onEnable() {
        log.info("*******************************");
        log.info("Gamers Club Lobby Plugin v2.0.1");
        log.info("*******************************");

        //Check if stuff exists
        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            configFile.getParentFile();
            saveResource("config.yml", false);
        }

        //DB setup
        SQLiteStorageManager.setupStorage();

        //Register commands
        Objects.requireNonNull(this.getCommand("anarchy")).setExecutor(new AnarchyCommand());
        Objects.requireNonNull(this.getCommand("gclp")).setExecutor(new GclpCommand());
        Objects.requireNonNull(this.getCommand("serverselector")).setExecutor(new ServerSelectorCommand());
        Objects.requireNonNull(this.getCommand("help")).setExecutor(new HelpCommand());
        Objects.requireNonNull(this.getCommand("lobby")).setExecutor(new LobbyCommand());
        Objects.requireNonNull(this.getCommand("maintenance")).setExecutor(new MaintenanceCommand());
        Objects.requireNonNull(this.getCommand("mute")).setExecutor(new MuteCommand());
        Objects.requireNonNull(this.getCommand("unmute")).setExecutor(new UnmuteCommand());

        //Register listeners
        this.getServer().getPluginManager().registerEvents(new PlayerChatListener(), this);
        this.getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
        this.getServer().getPluginManager().registerEvents(new BlockPlaceListener(), this);
        this.getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
        this.getServer().getPluginManager().registerEvents(new InventoryDragListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerRespawnListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerDeathListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerKickListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerDropItemListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerPreLoginListener(), this);
        this.getServer().getPluginManager().registerEvents(new TabCompleteListener(), this);
        this.getServer().getPluginManager().registerEvents(new LecternBookListener(), this);


        //Register messengers
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        log.info("Successfully started!");
    }

    @Override
    public void onDisable() {
        //unregister messengers
        this.getServer().getMessenger().unregisterOutgoingPluginChannel(this);

        //close db
        SQLiteStorageManager.closeStorage();
    }
}
