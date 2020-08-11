package me.m0dex.funquiz.database;

import me.m0dex.funquiz.FunQuiz;
import me.m0dex.funquiz.cache.PlayerData;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.Map;
import java.util.UUID;

public class Database {

    private String HOST, DATABASE, USERNAME, PASSWORD;
    private int PORT;

    private Connection connection;

    private FunQuiz instance;

    public Database(FunQuiz _instance, String host, int port, String database, String username, String password) {

        instance = _instance;

        HOST = host;
        PORT = port;
        DATABASE = database;
        USERNAME = username;
        PASSWORD = password;
    }

    public Database(FunQuiz _instance) {
        instance = _instance;
    }

    public PlayerData getPlayerData(UUID uuid) {

        try {
            PreparedStatement ps = getConnection().prepareStatement("SELECT * FROM PlayerData WHERE UUID=?;");
            ps.setString(1, uuid.toString());
            ResultSet result = ps.executeQuery();
            if (!result.next())
                return new PlayerData();
            return new PlayerData(
                        result.getInt(2),
                        result.getInt(3)
            );
        } catch (SQLException ex) {
            if(instance.getSettings().debug)
                ex.printStackTrace();
        }

        return new PlayerData();
    }

    public void savePlayerData(UUID uuid, PlayerData playerData) {

        try {
            if(instance.getSettings().useSQLite) {

                PreparedStatement ps = getConnection().prepareStatement("INSERT OR REPLACE INTO PlayerData " +
                        "(" +
                        "UUID," +
                        "AnsweredRight," +
                        "AnsweredWrong" +
                        ")" +
                        " VALUES " +
                        "(" +
                        "?," +
                        "?," +
                        "?" +
                        ");");

                ps.setString(1, uuid.toString());
                ps.setInt(2, playerData.getAnsRight());
                ps.setInt(3, playerData.getAnsWrong());
                ps.execute();
            } else {

                PreparedStatement ps = getConnection().prepareStatement("INSERT INTO PlayerData " +
                        "(" +
                        "UUID," +
                        "AnsweredRight," +
                        "AnsweredWrong" +
                        ")" +
                        " VALUES " +
                        "(" +
                        "?," +
                        "?," +
                        "?" +
                        ")" +
                        " ON DUPLICATE KEY UPDATE " +
                        "AnsweredRight = ?," +
                        "AnsweredWrong = ?" +
                        ";");

                ps.setString(1, uuid.toString());
                ps.setInt(2, playerData.getAnsRight());
                ps.setInt(3, playerData.getAnsWrong());
                ps.setInt(4, playerData.getAnsRight());
                ps.setInt(5, playerData.getAnsWrong());
                ps.execute();
            }
        } catch (SQLException ex) {
            if(instance.getSettings().debug)
                ex.printStackTrace();
        }
    }

    public void savePlayerCache(Map<UUID, PlayerData> cache) {
        try {
            getConnection().setAutoCommit(false);

            for(Map.Entry<UUID, PlayerData> entry : cache.entrySet()) {
                savePlayerData(entry.getKey(), entry.getValue());
            }

            getConnection().commit();
            getConnection().setAutoCommit(true);

        } catch (SQLException ex) {
            instance.getLogger().severe("Couldn't save user data to the database. Rolling back (this means the data won't be saved!)...");
            if(instance.getSettings().debug)
                ex.printStackTrace();
            try {
                getConnection().rollback();
            } catch (SQLException e) {
                instance.getLogger().severe("An error happened during rollback...");
                if(instance.getSettings().debug)
                    e.printStackTrace();
            }
        }
    }

    public void openConnection(boolean useSQLite) {
        try {
            if(useSQLite) {

                if (!isConnected()) {

                    File dbFile = new File(instance.getDataFolder(), "player_data.db");
                    if (!dbFile.exists()) {
                        dbFile.createNewFile();
                    }

                    Class.forName("org.sqlite.JDBC");
                    DriverManager.registerDriver(new org.sqlite.JDBC());
                    connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile);
                }
            } else {

                if (!isConnected()) {
                    Class.forName("com.mysql.jdbc.Driver");
                    DriverManager.registerDriver(new com.mysql.jdbc.Driver());
                    connection = DriverManager.getConnection("jdbc:mysql://" + this.HOST + ":" + this.PORT + "/" + this.DATABASE, this.USERNAME, this.PASSWORD);
                }
            }

            PreparedStatement playerDataTable = connection.prepareStatement("CREATE TABLE IF NOT EXISTS PlayerData " +
                    "(" +
                    "UUID char(36) NOT NULL," +
                    "AnsweredRight int(32) NOT NULL," +
                    "AnsweredWrong int(32) NOT NULL," +
                    "PRIMARY KEY (UUID)" +
                    ");");

            playerDataTable.execute();

        } catch (SQLException | ClassNotFoundException | IOException e) {
            instance.getLogger().severe("Couldn't create a connection to the database! Check your config.yml...");
            if(instance.getSettings().debug)
                e.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            if (isConnected())
                connection.close();
        } catch (SQLException e) {
            if(instance.getSettings().debug)
                e.printStackTrace();
        }
    }

    private synchronized boolean isConnected() {
        try {
            return (connection != null && !connection.isClosed());
        } catch (SQLException ex) {
            if(instance.getSettings().debug)
                ex.printStackTrace();
        }

        return false;
    }

    private synchronized Connection getConnection() {
        return connection;
    }
}
