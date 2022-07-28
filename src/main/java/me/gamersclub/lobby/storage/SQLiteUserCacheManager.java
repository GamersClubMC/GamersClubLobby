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

import static me.gamersclub.lobby.GamersClubMain.plugin;

public class SQLiteUserCacheManager extends SQLiteStorageManager {

    /**
     * @param username Accepts a string.
     * @return Returns an OfflinePlayer object.
     */
    public static @Nullable OfflinePlayer cacheUsernameGet(String username) {
        if (cacheUsernameCheck(username)) {
            String query = "SELECT `UUID` FROM `UUIDCache` WHERE `USERNAME` = '" + username + "';";
            try (Statement stmt = connection.createStatement()) {
                ResultSet rs = stmt.executeQuery(query);

                if (!rs.isClosed()) {
                    OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(rs.getString("UUID")));
                    rs.close();
                    return player;
                }
            } catch (SQLException e) {
                log.warn("Error: " + e);
            }
        }
        return null;
    }

    /**
     * Checks the database cache to see if the respective UUID/username already exist in the database.
     * If they do, it will skip adding another entry, otherwise, it will add an entry with the respective UUID/username
     * @param uuid Accepts a UUID.
     * @param username Accepts a username.
     */
    public static void cacheAdd(@NotNull UUID uuid,@NotNull String username) {
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
        } catch (SQLException e) {
            log.warn("Error: " + e);
        }
    }

    /**
     * @param username Accepts a username to check.
     * @return Returns true if the username already exists. False otherwise.
     */
    public static boolean cacheUsernameCheck(String username) {
        try {
            ResultSet rs = connection.createStatement().executeQuery("SELECT `USERNAME` FROM `UUIDCache` WHERE `USERNAME` = '" + username + "';");
            if (rs.isClosed()) {
                return false;
            }

            String user = rs.getString("Username");
            rs.close();

            if (user.equals(username)) {
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }


    /**
     * @param uuid Accepts a UUID.
     * @param username Accepts a username.
     * @return Returns true if the UUID found matches the one provided, false if it doesn't exist.
     */
    public static boolean cacheCheck(@NotNull UUID uuid, String username) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT `UUID`,`USERNAME` FROM `UUIDCache` WHERE `UUID` = '" + uuid + "';");
            ResultSet result = statement.executeQuery();

            if (result.isClosed()) {
                return false;
            }
            result.next();

            String targetUUID = result.getString("UUID");
            String targetUsername = result.getString("Username");

            result.close();

            if (targetUUID.equals(uuid.toString()) && targetUsername.equals(username)) {
                log.debug("User: " + targetUsername + " is already in the database cache.");
                return true;
            }
            else if (!targetUsername.equals(username)) {
                log.debug("User: " + username + " is already in the database cache with an outdated username.");
                log.debug("Old username: " + targetUsername);
                log.debug("New username: " + username);

                //silently update the player's username in the background and return true
                Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                    try {
                        String lastUpdated = LocalDate.now().toString();
                        PreparedStatement update = connection.prepareStatement("UPDATE `UUIDCache` SET `Username` = '" + username + "' WHERE `UUID` = '" + uuid + "';");
                        update.executeUpdate();
                        update = connection.prepareStatement("UPDATE `UUIDCache` SET `LastUpdated` = '" + lastUpdated + "' WHERE `UUID` = '" + uuid + "';");
                        update.executeUpdate();
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
