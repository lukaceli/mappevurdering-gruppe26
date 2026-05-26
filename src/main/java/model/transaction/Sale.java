package model.transaction;

import exceptions.DoubleCommitException;
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
   * Commits the sale by adding proceeds to the player's balance,
   * removing the share from their portfolio, and archiving the transaction.
   *
   * @param player the player making the sale
   * @throws DoubleCommitException if the transaction has already been committed
   */
  @Override
  public void commit(Player player) {
    if (commited) {
      throw new DoubleCommitException("Sale is already committed.");
    }
    player.addMoney(calculator.calculateTotal());
    player.getPortfolio().removeShare(share);
    player.getTransactionArchive().add(this);
    commited = true;
  }

  /**
   * Commits a partial sale by splitting the portfolio position, adding proceeds to the
   * player's balance, and archiving the transaction.
   *
   * @param player         the player making the sale
   * @param portfolioShare the full share position held before the partial sale
   * @return the remaining share position after the sale
   * @throws DoubleCommitException if the transaction has already been committed
   */
  public Share commitPartial(Player player, Share portfolioShare) {
    if (commited) {
      throw new DoubleCommitException("Sale is already committed.");
    }
    Share remaining = player.getPortfolio().removePartialShare(portfolioShare, share.quantity());
    player.addMoney(calculator.calculateTotal());
    player.getTransactionArchive().add(this);
    commited = true;
    return remaining;
  }
}