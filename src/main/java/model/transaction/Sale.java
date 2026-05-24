package model.transaction;

import execeptions.DobbleCommitException;
import model.calculator.TransactionCalculator;
import model.player.Player;
import model.stock.Share;

/**
 * Represents a stock sale transaction.
 * On commit, adds the sale proceeds to the player's balance
 * and removes the share from their portfolio.
 */
public class Sale extends Transaction {

  /**
   * Constructs a {@code Sale} transaction.
   *
   * @param share      the share being sold
   * @param week       the week in which the sale occurs
   * @param calculator the calculator used to determine the total proceeds
   */
  public Sale(Share share, int week, TransactionCalculator calculator) {
    super(share, week, calculator);
  }

  /**
   * Commits the sale and add money to the player's balance
   * and removing the share from their portfolio.
   *
   * @param player the player making the sale
   * @throws DobbleCommitException if the transaction has already been committed
   */
  @Override
  public void commit(Player player) {
    if (commited) {
      throw new DobbleCommitException("Sale is already committed.");
    }
    player.addMoney(calculator.calculateTotal());
    player.getPortfolio().removeShare(share);
    commited = true;
  }
}