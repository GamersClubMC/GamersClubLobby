package me.gamersclub.lobby.storage;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.UUID;

public class SQLiteUserCacheManager extends SQLiteStorageManager {

    public static @Nullable OfflinePlayer cacheUsernameGet(String username) {
        if (cacheUsernameCheck(username)) {
            String query = "SELECT `UUID` FROM `UUIDCache` WHERE `USERNAME` = '" + username + "';";
            try (Statement stmt = connection.createStatement()) {
                ResultSet rs = stmt.executeQuery(query);
                OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(rs.getString("UUID")));
                rs.close();
                return player;
            } catch (SQLException e) {
                SQLiteStorageManager.log.error("Error: " + e);
            }
        }
        return null;
    }

    public static void cacheAdd(@NotNull UUID uuid, String username) {
        String lastUpdated = LocalDate.now().toString();
        try {
            if (!cacheCheck(uuid, username)) {
                String sql = "(UUID, USERNAME, LASTUPDATED) VALUES (?,?,?)";
                PreparedStatement insert = connection.prepareStatement("INSERT OR REPLACE INTO `UUIDCache` " + sql);
                insert.setString(1, uuid.toString());
                insert.setString(2, username);
                insert.setString(3, lastUpdated);
                insert.executeUpdate();
            }
        }
        catch (SQLException e) {
            log.error("Error: " + e);
        }
    }

    public static boolean cacheUsernameCheck(String username) {
        try {
            ResultSet rs = connection.createStatement().executeQuery("SELECT `USERNAME` FROM `UUIDCache` WHERE `USERNAME` = '" + username + "';");
            if (rs.isClosed()) {
                return false;
            }
            final String user = rs.getString("Username");

            rs.close();

            if (user.equals(username)) {
                return true;
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }



    public static boolean cacheCheck(@NotNull UUID uuid, String username) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT `UUID`,`USERNAME` FROM `UUIDCache` WHERE `UUID` = '" + uuid + "';");
            ResultSet results = statement.executeQuery();
            results.next();

            String targetUUID = results.getString("UUID");
            String targetUsername = results.getString("Username");

            results.close();

            if (targetUUID.equals(uuid.toString()) && targetUsername.equals(username)) {
                return true;
            }
            else if (!targetUsername.equals(username)) {
                //silently update the player's username in the background and return true
                Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                    try {
                        String lastUpdated = LocalDate.now().toString();
                        PreparedStatement insert = connection.prepareStatement("UPDATE `UUIDCache` SET `Username` = '" + username + "' WHERE `UUID` = '" + uuid + "';");
                        insert.executeUpdate();
                        insert = connection.prepareStatement("UPDATE `UUIDCache` SET `LastUpdated` = '" + lastUpdated + "' WHERE `UUID` = '" + uuid + "';");
                        insert.executeUpdate();
                    }
                    catch (SQLException ignored) {}
                });
                return true;
            }
        }
        catch (SQLException ignored) {}
        return false;
    }

}
