package me.gamersclub.lobby.storage;

import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SQLiteLogManager extends SQLiteStorageManager {

    //adds log to database mod logs with moderator and target as people involved
    public static void addLog(String targetUUID,String targetUser,String modUUID,String modUser, String reason, String punishDate, String punishType) {
        try {

            String sql = "(UUID, Username, Reason, IssuedBy, IssuedByUUID, DateActionTaken, TypeOfPunishment) VALUES (?,?,?,?,?,?,?)";
            PreparedStatement insert = connection.prepareStatement("INSERT OR REPLACE INTO `mod_logs` " + sql);
            insert.setString(1, targetUUID);
            insert.setString(2, targetUser);
            insert.setString(3, reason);
            insert.setString(4, modUser);
            insert.setString(5, modUUID);
            insert.setString(6, punishDate);
            insert.setString(7, punishType);
            insert.executeUpdate();
        }
        catch (SQLException e) {
            log.warn("Error: " + e);
        }
    }

    public static void addLog(String targetUUID,String targetUser,String reason, String punishDate, String punishType) {
        try {

            String sql = "(UUID, Username, Reason, IssuedBy, IssuedByUUID, DateActionTaken, TypeOfPunishment) VALUES (?,?,?,?,?,?,?)";
            PreparedStatement insert = connection.prepareStatement("INSERT OR REPLACE INTO `mod_logs` " + sql);
            insert.setString(1, targetUUID);
            insert.setString(2, targetUser);
            insert.setString(3, reason);
            insert.setString(4, console);
            insert.setString(5, console);
            insert.setString(6, punishDate);
            insert.setString(7, punishType);
            insert.executeUpdate();
        }
        catch (SQLException e) {
            log.warn("Error: " + e);
        }
    }

    public static String infoLog(@NotNull UUID uuid, String column) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT `UUID`,`ISSUEDBYUUID`,`DATEACTIONTAKEN`,`TYPEOFPUNISHMENT` FROM `mod_logs` WHERE `uuid`='" + uuid + "';");
            ResultSet results = statement.executeQuery();
            results.next();
            // UUID, Username, Reason, IssuedBy, IssuedByUUID, DateActionTaken, TypeOfPunishment
            return results.getString(column);
        }
        catch (SQLException e) {
            log.warn("Error: " + e);
        }
        return null;
    }
}
