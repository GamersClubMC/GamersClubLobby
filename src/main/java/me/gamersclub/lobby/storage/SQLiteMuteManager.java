package me.gamersclub.lobby.storage;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SQLiteMuteManager extends SQLiteStorageManager {

    /**
     * Deletes a mute from the database using the provided UUID.
     * @param uuid Accepts a UUID.
     */
    public static void deleteMute(@NotNull UUID uuid) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM `mutes` WHERE `uuid`=?");
            statement.setString(1,uuid.toString());
            statement.execute();
        }
        catch (SQLException e) {
            log.error("Error: " + e);
        }
    }

    /**
     * Checks if there is a mute in the database tied to the UUID, and returns the end date of the mute.
     * @param uuid Accepts a UUID.
     * @param column Accepts a string.
     * @return Returns information about the player's mute
     */
    public static @Nullable String infoMute(UUID uuid, String column) {
        try {
            ResultSet results = connection.createStatement().executeQuery("SELECT `EndDate` FROM `mutes` WHERE `uuid`='" + uuid + "';");

            if (results.isClosed()) {
                return "2019-01-01";
            }

            results.next();

            return results.getString(column);
        }
        catch (SQLException e) {
            log.error("Error: " + e);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Adds a mute associated with the Player's UUID.
     * @param target Accepts a Player object
     * @param mutedBy Accepts a Player object
     * @param reason Can be a maximum of 500 characters
     * @param startDate Accepts a string formatted like '2022-01-01'
     * @param endDate Accepts a string formatted like '2022-01-01'
     */
    public static void addMute(@NotNull Player target, @NotNull Player mutedBy, String reason, String startDate, String endDate) {
        String targetUUID = target.getUniqueId().toString();
        String mutedByUUID = mutedBy.getUniqueId().toString();

        try {
            String sql = "(UUID, USERNAME, REASON, MUTEDBY, MUTEDBYUUID, STARTDATE, ENDDATE) VALUES (?,?,?,?,?,?,?)";
            PreparedStatement insert = connection.prepareStatement("INSERT OR REPLACE INTO `mutes` " + sql);
            insert.setString(1, targetUUID);
            insert.setString(2, target.getName());
            insert.setString(3, reason);
            insert.setString(4, mutedBy.getName());
            insert.setString(5, mutedByUUID);
            insert.setString(6, startDate);
            insert.setString(7, endDate);
            insert.executeUpdate();

            SQLiteLogManager.addLog(targetUUID,target.getName(),mutedByUUID,mutedBy.getName(),reason,startDate,"MUTE");
        }
        catch (SQLException e) {
            log.error("Error: " + e);
        }
    }

    /**
     * Adds a mute associated with the Player's UUID.
     * @param target Accepts an OfflinePlayer object
     * @param mutedBy Accepts a Player object
     * @param reason Can be a maximum of 500 characters
     * @param startDate Accepts a string formatted like '2022-01-01'
     * @param endDate Accepts a string formatted like '2022-01-01'
     */
    public static void addMute(@NotNull OfflinePlayer target, @NotNull Player mutedBy, String reason, String startDate, String endDate) {
        String targetUUID = target.getUniqueId().toString();
        String mutedByUUID = mutedBy.getUniqueId().toString();
        try {
            String sql = "(UUID, USERNAME, REASON, MUTEDBY, MUTEDBYUUID, STARTDATE, ENDDATE) VALUES (?,?,?,?,?,?,?)";
            PreparedStatement insert = connection.prepareStatement("INSERT OR REPLACE INTO `mutes` " + sql);
            insert.setString(1, targetUUID);
            insert.setString(2, target.getName());
            insert.setString(3, reason);
            insert.setString(4, mutedBy.getName());
            insert.setString(5, mutedByUUID);
            insert.setString(6, startDate);
            insert.setString(7, endDate);
            insert.executeUpdate();

            SQLiteLogManager.addLog(targetUUID,target.getName(),mutedByUUID,mutedBy.getName(),reason,startDate,"MUTE");
        }
        catch (SQLException e) {
            log.error("Error: " + e);
        }
    }

    /**
     * Adds a mute associated with the Player's UUID.
     * @param targetUUID Accepts a UUID
     * @param targetUsername Accepts a string of maximum 16 characters
     * @param mutedBy Accepts a Player object
     * @param reason Can be a maximum of 500 characters
     * @param startDate Accepts a string formatted like '2022-01-01'
     * @param endDate Accepts a string formatted like '2022-01-01'
     */
    public static void addMute(@NotNull UUID targetUUID,@NotNull String targetUsername, @NotNull Player mutedBy, String reason, String startDate, String endDate) {
        String mutedByUUID = mutedBy.getUniqueId().toString();
        String targetUniqueUID = targetUUID.toString();
        try {
            String sql = "(UUID, USERNAME, REASON, MUTEDBY, MUTEDBYUUID, STARTDATE, ENDDATE) VALUES (?,?,?,?,?,?,?)";
            PreparedStatement insert = connection.prepareStatement("INSERT OR REPLACE INTO `mutes` " + sql);
            insert.setString(1, targetUniqueUID);
            insert.setString(2, targetUsername);
            insert.setString(3, reason);
            insert.setString(4, mutedBy.getName());
            insert.setString(5, mutedByUUID);
            insert.setString(6, startDate);
            insert.setString(7, endDate);
            insert.executeUpdate();

            SQLiteLogManager.addLog(targetUniqueUID,targetUsername,mutedBy.getName(),mutedByUUID,reason,startDate,"MUTE");
        }
        catch (SQLException e) {
            log.error("Error: " + e);
        }
    }


    /**
     * Adds a mute associated with the Player's UUID.
     * @param target Accepts a Player object
     * @param reason Can be a maximum of 500 characters
     * @param startDate Accepts a string formatted like '2022-01-01'
     * @param endDate Accepts a string formatted like '2022-01-01'
     */
    public static void addConsoleMute(@NotNull Player target, String reason, String startDate, String endDate) {
        UUID targetUniqueId = target.getUniqueId();
        try {
            String sql = "(UUID, USERNAME, REASON, MUTEDBY, MUTEDBYUUID, STARTDATE, ENDDATE) VALUES (?,?,?,?,?,?,?)";
            PreparedStatement insert = connection.prepareStatement("INSERT OR REPLACE INTO `mutes` " + sql);
            insert.setString(1, targetUniqueId.toString());
            insert.setString(2, target.getName());
            insert.setString(3, reason);
            insert.setString(4, console);
            insert.setString(5, console);
            insert.setString(6, startDate);
            insert.setString(7, endDate);
            insert.executeUpdate();

            SQLiteLogManager.addLog(target.getUniqueId().toString(),target.getName(),reason,startDate,"MUTE");
        }
        catch (SQLException e) {
            log.error("Error: " + e);

        }
    }

    /**
     * Adds a mute associated with the Player's UUID.
     * @param target Accepts an OfflinePlayer object
     * @param username Accepts a string of maximum 16 characters
     * @param reason Can be a maximum of 500 characters
     * @param startDate Accepts a string formatted like '2022-01-01'
     * @param endDate Accepts a string formatted like '2022-01-01'
     */
    public static void addConsoleMute(@NotNull OfflinePlayer target, String username, String reason, String startDate, String endDate) {
        try {
            String sql = "(UUID, USERNAME, REASON, MUTEDBY, MUTEDBYUUID, STARTDATE, ENDDATE) VALUES (?,?,?,?,?,?,?)";
            PreparedStatement insert = connection.prepareStatement("INSERT OR REPLACE INTO `mutes` " + sql);
            insert.setString(1, target.getUniqueId().toString());
            insert.setString(2, username);
            insert.setString(3, reason);
            insert.setString(4, console);
            insert.setString(5, console);
            insert.setString(6, startDate);
            insert.setString(7, endDate);
            insert.executeUpdate();

            SQLiteLogManager.addLog(target.getUniqueId().toString(),username,reason,startDate,"MUTE");
        }
        catch (SQLException e) {
            log.error("Error: " + e);
        }
    }
}
