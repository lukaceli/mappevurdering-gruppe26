package db;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
  public static final String URL = "jdbc:sqlite:aksjespill.db";

  static {
    initializeDatabase();
  }

  public static Connection getConnection() throws SQLException {
    Connection conn = DriverManager.getConnection(URL);
    try (Statement stmt = conn.createStatement()) {
      stmt.execute("PRAGMA foreign_keys = ON;");
    }
    return conn;
  }

  private static void initializeDatabase() {
    try (Connection conn = DriverManager.getConnection(URL);
         Statement stmt = conn.createStatement()) {

      InputStream is = Database.class.getClassLoader().getResourceAsStream("schema.sql");
      String sql = new String(is.readAllBytes(), StandardCharsets.UTF_8);

      for (String statement : sql.split(";")) {
        String trimmed = statement.trim();
        if (!trimmed.isEmpty()) {
          stmt.execute(trimmed);
        }
      }

    } catch (SQLException | IOException e) {
      throw new RuntimeException("Kunne ikke initialisere databasen", e);
    }
  }
}
