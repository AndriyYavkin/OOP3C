package service;

import java.sql.*;

/**
 * Database utility class for managing SQLite connection and schema.
 */

public class Database {
    private static final String DB_URL = "jdbc:sqlite:gems.db";

    static {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON"); 
            
            stmt.execute("CREATE TABLE IF NOT EXISTS Gems (" +
                    "Id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "Type TEXT, Name TEXT, WeightCarats REAL, PricePerCarat REAL, Transparency INTEGER)");
            stmt.execute("CREATE TABLE IF NOT EXISTS Necklaces (" +
                    "Id INTEGER PRIMARY KEY AUTOINCREMENT, Name TEXT UNIQUE)");
            stmt.execute("CREATE TABLE IF NOT EXISTS NecklaceGems (" +
                    "NecklaceId INTEGER, GemId INTEGER, " +
                    "FOREIGN KEY (NecklaceId) REFERENCES Necklaces(Id), " +
                    "FOREIGN KEY (GemId) REFERENCES Gems(Id))");
        } catch (SQLException e) {
            throw new RuntimeException("DB init failed: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }
}