package model.player;

import execeptions.InsufficientBalanceException;
import execeptions.InvalidPlayerName;
import java.math.BigDecimal;
import model.calculator.SaleCalculator;
import model.stock.Share;
import model.transaction.TransactionArchive;

/**
 * Represents a player in the game, holding a balance, portfolio, and transaction history.
 * A player's status is updated based on net worth relative to their starting balance
 * and the current week.
 */
public class Player {

  private final String name;
  private final BigDecimal startingBalance;
  private final Portfolio portfolio;
  private final TransactionArchive transactionArchive;
  private BigDecimal balance;
  private String status;

  /**
   * Constructs a new {@code Player} with the given name and starting balance.
   *
   * @param name            the player's name, must be between 2 and 15 characters
   * @param startingBalance the player's starting balance, must be greater than zero
   * @throws InvalidPlayerName        if the name is shorter than 2 or longer than 15 characters
   * @throws IllegalArgumentException if the starting balance is zero or negative
   */
  public Player(String name, BigDecimal startingBalance) {
    if (name.length() < 2) {
      throw new InvalidPlayerName("Player name must have at least 2 characters");
    }
    if (name.length() > 15) {
      throw new InvalidPlayerName("Player name cannot have more than 15 characters");
    }
    if (startingBalance.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Start balance must be greater than zero");
    }
    this.name = name;
    this.startingBalance = startingBalance;
    this.portfolio = new Portfolio();
    this.transactionArchive = new TransactionArchive();
    this.balance = startingBalance;
    this.status = "Novice";
  }

  /**
   * Returns the player's name.
   *
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the player's starting balance.
   *
   * @return the starting balance
   */
  public BigDecimal getStartingBalance() {
    return startingBalance;
  }

  /**
   * Returns the player's current cash balance.
   *
   * @return the current balance
   */
  public BigDecimal getBalance() {
    return balance;
  }

  /**
   * Returns the player's portfolio.
   *
   * @return the {@link Portfolio}
   */
  public Portfolio getPortfolio() {
    return portfolio;
  }

  /**
   * Returns the player's current status.
   *
   * @return the status string
   */
  public String getStatus() {
    return status;
  }

  /**
   * Returns the player's transaction archive.
   *
   * @return the {@link TransactionArchive}
   */
  public TransactionArchive getTransactionArchive() {
    return transactionArchive;
  }

  /**
   * Withdraws the given amount from the player's balance.
   *
   * @param amount the amount to withdraw
   * @throws InsufficientBalanceException if the balance is less than the amount
   */
  public void withdrawMoney(BigDecimal amount) {
    if (balance.compareTo(amount) < 0) {
      throw new InsufficientBalanceException("Balance is insufficient");
    }
    balance = balance.subtract(amount);
  }

  /**
   * Adds the given amount to the player's balance.
   *
   * @param amount the amount to add
   */
  public void addMoney(BigDecimal amount) {
    balance = balance.add(amount);
  }

  /**
   * Sets the player's status directly.
   *
   * @param status the new status string
   */
  public void setStatus(String status) {
    this.status = status;
  }

  /**
   * Updates the player's status based on net worth and the current week.
   * Status thresholds:
   *   Speculator: week >= 20 and net worth >= 200% of starting balance
   *   Investor:   week >= 10 and net worth >= 120% of starting balance
   *   Novice:     all other cases
   *
   * @param week the current week number
   */
  public void updateStatusIfNeeded(int week) {
    BigDecimal investorRequirement = startingBalance.multiply(new BigDecimal("1.2"));
    BigDecimal speculatorRequirement = startingBalance.multiply(new BigDecimal("2"));
    if (week >= 20 && getNetWorth().compareTo(speculatorRequirement) >= 0) {
      setStatus("Speculator");
    } else if (week >= 10 && getNetWorth().compareTo(investorRequirement) >= 0) {
      setStatus("Investor");
    } else {
      setStatus("Novice");
    }
  }

  /**
   * Calculates the player's total net worth as cash balance plus the sale value of all shares.
   *
   * @return the total net worth
   */
  public BigDecimal getNetWorth() {
    BigDecimal netWorth = balance;
    for (Share share : portfolio.getShares()) {
      netWorth = netWorth.add(new SaleCalculator(share).calculateTotal());
    }
    return netWorth;
  }
}