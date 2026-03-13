package db.dao;

import db.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import model.player.Player;

public class UserDao {
  private Player player;

  public void insert(Player player) {
    String sql = "INSERT INTO user(id, user_name, e_mail, passwordHash) " +
        "VALUES(?, ?, ?, ?)";

    try (Connection conn = Database.getConnection()) {
      PreparedStatement stmt = conn.prepareStatement(sql,
          Statement.RETURN_GENERATED_KEYS);

      stmt.setLong(1, player.getId());
      stmt.setString(2, player.getName());
      //Denne må endres til passordHash når vi har laget det.
      stmt.setString(3, player.getStatus());
      stmt.executeUpdate();

      ResultSet keys = stmt.getGeneratedKeys();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
