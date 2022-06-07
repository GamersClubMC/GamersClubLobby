package me.gamersclub.lobby.storage;

import me.gamersclub.lobby.util.configuration.ConfigManager;
import me.gamersclub.lobby.util.logging.GamersClubLogger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("CanBeFinal")
public class SQLiteStorageManager {
    protected static Connection connection;

    final static Plugin plugin = Bukkit.getPluginManager().getPlugin("GamersClubLobbyPlugin");
    final static GamersClubLogger log = new GamersClubLogger();
    static String console = ConfigManager.getConfigString("mute.console-mute-name");

    public static void setupStorage() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + "plugins/GamersClubLobbyPlugin/" + ConfigManager.getConfigString("settings.db-name") + ".sqlite");

            Statement createTables = connection.createStatement();
            createTables.executeUpdate("CREATE TABLE IF NOT EXISTS `UUIDCache` (UUID char(36), Username varchar(16), LastUpdated varchar(500))");
            createTables.executeUpdate("CREATE TABLE IF NOT EXISTS `mutes` (UUID char(36), Username varchar(16),Reason varchar(500), MutedBy varchar(16), MutedByUUID varchar(36), StartDate varchar(500), EndDate varchar(500))");
            createTables.executeUpdate("CREATE TABLE IF NOT EXISTS `mod_logs` (UUID char(36), Username varchar(16), Reason varchar(500), IssuedBy varchar(16), IssuedByUUID varchar(36), DateActionTaken varchar(500), TypeOfPunishment varchar(20))");
            createTables.close();
        }
        catch (ClassNotFoundException | SQLException e) {
            log.error("Error: " + e);
        }
    }

    public static void closeStorage() {
        try {
            connection.close();
        } catch (SQLException e) {
            log.error("Error: " + e);
        }
    }

    public static @NotNull List<String> getUsernames() {
        List<String> names = new ArrayList<>();
        String query = "SELECT * FROM `mutes` ";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                names.add(rs.getString("Username"));
            }
            rs.close();
        }
        catch (SQLException e) {
            log.error("Error: " + e);
        }
        return names;
    }

}
