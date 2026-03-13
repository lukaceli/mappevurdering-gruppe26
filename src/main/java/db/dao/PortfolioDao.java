package db.dao;

import db.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import model.player.Portfolio;
import model.stock.Share;
import model.transaction.Transaction;

public class PortfolioDao {

  public void insert(Transaction transaction) {
    String sql = "INSERT INTO stocksPerUser(purchase_price, current_price, quantity)" +
        "VALUES(?, ?, ?)";

    try (Connection conn = Database.getConnection()) {
      PreparedStatement stmt = conn.prepareStatement(sql,
          Statement.RETURN_GENERATED_KEYS);

      stmt.setBigDecimal(1, transaction.getShare().getPurchasePrice());
      stmt.setBigDecimal(2, transaction.getShare().getStock().getCurrentPrice());
      stmt.setBigDecimal(3, transaction.getShare().getQuantity());
      stmt.executeUpdate();

      ResultSet key = stmt.getGeneratedKeys();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
