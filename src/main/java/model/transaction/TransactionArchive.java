package model.transaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores committed transactions for a player.
 */
public class TransactionArchive {

  private final List<Transaction> transactions;

  /**
   * Constructs an empty {@code TransactionArchive}.
   */
  public TransactionArchive() {
    this.transactions = new ArrayList<>();
  }

  /**
   * Adds a transaction to the archive if it has been committed.
   *
   * @param transaction the transaction to add
   * @return {@code true} if the transaction was added, {@code false} if not committed
   */
  public boolean add(Transaction transaction) {
    if (transaction.isCommitted()) {
      transactions.add(transaction);
      return true;
    }
    return false;
  }

  /**
   * Returns whether the archive contains no transactions.
   *
   * @return {@code true} if the archive is empty, {@code false} otherwise
   */
  public boolean isEmpty() {
    return transactions.isEmpty();
  }

  /**
   * Returns a copy of all transactions in the archive.
   *
   * @return a list of all {@link Transaction} objects
   */
  public List<Transaction> getAll() {
    return new ArrayList<>(transactions);
  }

  /**
   * Returns the number of distinct weeks in which transactions occurred.
   *
   * @return the count of distinct weeks
   */
  public int countDistinctWeeks() {
    return (int) transactions.stream()
            .map(Transaction::getWeek)
            .distinct()
            .count();
  }
}