package me.gamersclub.lobby;

import me.gamersclub.lobby.commands.*;
import me.gamersclub.lobby.listeners.*;
import me.gamersclub.lobby.storage.SQLiteStorageManager;
import me.gamersclub.lobby.tabcompleter.MuteTabCompleter;
import me.gamersclub.lobby.util.configuration.ConfigManager;
import me.gamersclub.lobby.util.logging.GamersClubLogger;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GamersClubMain extends JavaPlugin {
    public static Plugin plugin;
    public static boolean isPaperServer = false;
    public static boolean isServer1_18_2OrAbove = false;
    public static boolean logMutes = true;
    public static boolean isMaintenanceEnabled = false;
    public static boolean playersAlreadyKicked = false;
    public static boolean debugMode = false;

    //queue used to prevent bedrock from spam opening forms
    @SuppressWarnings("CanBeFinal")
    public static List<String> openedFormQueue = new ArrayList<>();
    //another queue used to fix spamming the sign in the lobby
    @SuppressWarnings("CanBeFinal")
    public static List<String> anarchyCooldownList = new ArrayList<>();

    private final GamersClubLogger log = new GamersClubLogger();

    @Override
    public void onEnable() {
        plugin = this;
        debugMode = this.getConfig().getBoolean("settings.debug-mode");
        log.info("*******************************");
        log.info("Gamers Club Lobby Plugin v2.1.1");
        log.info("*******************************");

        //Check if stuff exists
        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            saveResource("config.yml", false);
        }

        //update config version # on config changes
        //is 3.0 because a lot of changes have happened tbh
        if (ConfigManager.getConfigDouble("config-version") != 3.0) {
            log.warn("Your configuration is out of date! Please regenerate it when possible!");
        }

        isServer1_18_2OrAbove = isOrIsAbove1_18_2();
        if (!isServer1_18_2OrAbove) {
            log.warn("Detected server version as being below 1.18.x!");
            log.warn("Player heads inside of the Mute UI won't work properly on this version and will be replaced with the item specified in the config!");
        } else {
            log.info("Detected server version as being above 1.18.x");
            log.info("Player heads inside of the Mute UI will work properly.");
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
        log.debug("Registering commands (9)");
        Objects.requireNonNull(this.getCommand("anarchy")).setExecutor(new AnarchyCommand());
        Objects.requireNonNull(this.getCommand("anarchy-icd")).setExecutor(new AnarchyImplCommand());
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

        //paper specific features
        if (isPaperServer) {
            log.debug("Registering Paper specific listeners (2)");

            try {
                Class.forName("com.destroystokyo.paper.event.server.AsyncTabCompleteEvent");
                this.getServer().getPluginManager().registerEvents(new AsyncTabCompleteListener(), this);
            } catch (ClassNotFoundException e) {
                log.warn("This version of paper does not support the AsyncTabCompleteEvent!");
                log.warn("Using TabCompleteEvent instead!");
                this.getServer().getPluginManager().registerEvents(new TabCompleteListener(), this);
            }
            try {
                Class.forName("io.papermc.paper.event.player.AsyncChatEvent");
                this.getServer().getPluginManager().registerEvents(new AsyncChatListener(), this);
            } catch (ClassNotFoundException e) {
                log.warn("This version of paper does not support the AsyncChatEvent!");
                log.warn("Using AsyncPlayerChatEvent instead!");
                this.getServer().getPluginManager().registerEvents(new PlayerChatListener(), this);
            }
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

    /*
        The plugin barely worked on 1.15.2. Major functions weren't working.
        1.14.4 refuses to start with Java 16.

        1.16.x was the lowest version the plugin ran on without major breaking issues, same with 1.17.x
        1.18.x and above are versions with working player heads in the mute UI.

        My solution to fix player heads is to replace them with a piece of paper if the server version is below 1.18.2.
        Theoretically this entire solution should not require manually adding the supported version number every update.
        I'll probably eventually figure out player heads on older versions, but until then.
    */
    public boolean isOrIsAbove1_18_2() {
        return Double.parseDouble(this.getServer().getBukkitVersion().substring(2, 6).replace("-R", "").strip()) >= 18.2;
    }
}
