package model.transaction;

import exceptions.DoubleCommitException;
import exceptions.InsufficientBalanceException;
import java.math.BigDecimal;
import model.calculator.TransactionCalculator;
import model.player.Player;
import model.stock.Share;

/**
 * Represents a stock purchase transaction.
 * On commit, withdraws the total purchase cost from the player's balance
 * and adds the share to their portfolio.
 */
public class Purchase extends Transaction {

  /**
   * Constructs a {@code Purchase} transaction.
   *
   * @param share      the share being purchased
   * @param week       the week in which the purchase occurs
   * @param calculator the calculator used to determine the total cost
   */
  public Purchase(Share share, int week, TransactionCalculator calculator) {
    super(share, week, calculator);
  }

  /**
   * Commits the purchase by withdrawing funds and adding the share to the player's portfolio.
   *
   * @param player the player making the purchase
   * @throws InsufficientBalanceException if the player's balance is insufficient
   * @throws DoubleCommitException      if the transaction has already been committed
   */
  @Override
  public void commit(Player player) {
    if (commited) {
      throw new DoubleCommitException("Purchase is already committed.");
    }
    BigDecimal purchasePriceTotal = calculator.calculateTotal();
    if (player.getBalance().compareTo(purchasePriceTotal) < 0) {
      throw new InsufficientBalanceException("Not enough balance");
    }
    player.withdrawMoney(purchasePriceTotal);
    player.getPortfolio().addShare(share);
    commited = true;
  }
}