package me.gamersclub.lobby;

import me.gamersclub.lobby.commands.*;
import me.gamersclub.lobby.listeners.*;
import me.gamersclub.lobby.storage.SQLiteStorageManager;
import me.gamersclub.lobby.util.configuration.ConfigManager;
import me.gamersclub.lobby.util.logging.GamersClubLogger;
import me.gamersclub.lobby.tabcompleter.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GamersClubMain extends JavaPlugin {
    public static Plugin plugin;
    public static boolean isPaperServer = false;
    public static boolean logMutes = true;
    public static boolean isMaintenanceEnabled = false;
    public static boolean playersAlreadyKicked = false;
    public static boolean debugMode = false;
    //queue used to prevent bedrock from spam opening forms
    @SuppressWarnings("CanBeFinal")
    public static List<String> openedFormList = new ArrayList<>();

    private final GamersClubLogger log = new GamersClubLogger();

    @Override
    public void onEnable() {
        plugin = this;
        debugMode = this.getConfig().getBoolean("settings.debug-mode");
        log.info("*******************************");
        log.info("Gamers Club Lobby Plugin v2.1.0");
        log.info("*******************************");

        //Check if stuff exists
        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            saveResource("config.yml", false);
        }

        //Check if is paper server
        try {
            Class.forName("com.destroystokyo.paper.ParticleBuilder");
            isPaperServer = true;

        } catch (ClassNotFoundException ignored) {}
        log.debug("PaperMC detected: " + isPaperServer);

        logMutes = ConfigManager.getConfigBoolean("general.log-muted-player-messages");
        log.debug("Muted player message logging: " + logMutes);

        //DB setup
        log.debug("Setting up database");
        SQLiteStorageManager.setupStorage();

        //Register commands
        log.debug("Registering commands (8)");
        Objects.requireNonNull(this.getCommand("anarchy")).setExecutor(new AnarchyCommand());
        Objects.requireNonNull(this.getCommand("gclp")).setExecutor(new GclpCommand());
        Objects.requireNonNull(this.getCommand("serverselector")).setExecutor(new ServerSelectorCommand());
        Objects.requireNonNull(this.getCommand("help")).setExecutor(new HelpCommand());
        Objects.requireNonNull(this.getCommand("lobby")).setExecutor(new LobbyCommand());
        Objects.requireNonNull(this.getCommand("maintenance")).setExecutor(new MaintenanceCommand());
        Objects.requireNonNull(this.getCommand("mute")).setExecutor(new MuteCommand());
        Objects.requireNonNull(this.getCommand("unmute")).setExecutor(new UnmuteCommand());

        //Register tab completer
        log.debug("Registering Tab Completer (1)");
        Objects.requireNonNull(this.getCommand("mute")).setTabCompleter(new MuteTabCompleter());

        //Register listeners
        log.debug("Registering listeners (14)");
        this.getServer().getPluginManager().registerEvents(new CommandPreProcessListener(), this);
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
        this.getServer().getPluginManager().registerEvents(new LecternBookListener(), this);

        if (isPaperServer) {
            log.debug("Registering Paper specific listeners (2)");
            this.getServer().getPluginManager().registerEvents(new AsyncTabCompleteListener(), this);
            this.getServer().getPluginManager().registerEvents(new AsyncChatListener(), this);
        } else {
            log.debug("Registering additional listeners (2)");
            this.getServer().getPluginManager().registerEvents(new TabCompleteListener(), this);
            this.getServer().getPluginManager().registerEvents(new PlayerChatListener(), this);
        }

        //Register messengers
        log.debug("Registering messengers (1)");
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        log.info("Successfully started!");
    }

    @Override
    public void onDisable() {
        //unregister messengers
        log.debug("Unregistering messengers (1)");
        this.getServer().getMessenger().unregisterOutgoingPluginChannel(this, "BungeeCord");

        //close db
        log.debug("Closing database");
        SQLiteStorageManager.closeStorage();
    }
}
