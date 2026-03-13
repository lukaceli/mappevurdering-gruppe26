package db.dao;

import db.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import model.stock.Stock;
import model.transaction.Sale;
import model.transaction.Transaction;

public class TransactionDao {

  public void insert(Transaction transaction, Stock stock) {
    String sql = "INSERT INTO transactions(stock, transaction_type, quantity, price)" +
        "VALUES(?, ?, ?, ?)";

    try (Connection conn = Database.getConnection()) {
      PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

      stmt.setString(1, stock.getSymbol());
      stmt.setString(2, saleOrPurchase(transaction));
      stmt.setBigDecimal(3, transaction.getShare().getQuantity());
      stmt.setBigDecimal(4, transaction.getShare().getPurchasePrice());
      stmt.executeUpdate();

      ResultSet keys = stmt.getGeneratedKeys();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private String saleOrPurchase(Transaction transaction) {
    if (transaction instanceof Sale) {
      return "Sale";
    }
    return "Purchase";
  }


}
