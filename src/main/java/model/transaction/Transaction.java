package model.transaction;

import model.calculator.TransactionCalculator;
import model.player.Player;
import model.stock.Share;

/**
 * Abstract base class for stock transactions such as purchases and sales.
 * Subclasses must implement {@link #commit(Player)} to define the specific
 * transaction behaviour.
 */
public abstract class Transaction {

  protected Share share;
  protected int week;
  protected TransactionCalculator calculator;
  protected boolean commited;

  /**
   * Constructs a {@code Transaction} with the given share, week and calculator.
   *
   * @param share      the share involved in the transaction
   * @param week       the week in which the transaction occurs
   * @param calculator the calculator used to determine costs or proceeds
   */
  protected Transaction(Share share, int week, TransactionCalculator calculator) {
    this.share = share;
    this.week = week;
    this.calculator = calculator;
    this.commited = false;
  }

  /**
   * Returns the share involved in the transaction.
   *
   * @return the {@link Share}
   */
  public Share getShare() {
    return share;
  }

  /**
   * Returns the week in which the transaction occurred.
   *
   * @return the week number
   */
  public int getWeek() {
    return week;
  }

  /**
   * Returns the calculator used for this transaction.
   *
   * @return the {@link TransactionCalculator}
   */
  public TransactionCalculator getCalculator() {
    return calculator;
  }

  /**
   * Returns whether this transaction has been committed.
   *
   * @return {@code true} if committed, {@code false} otherwise
   */
  public boolean isCommitted() {
    return commited;
  }

  /**
   * Commits the transaction for the given player.
   *
   * @param player the player executing the transaction
   */
  public abstract void commit(Player player);
}