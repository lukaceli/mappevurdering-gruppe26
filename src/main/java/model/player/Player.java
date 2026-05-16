package model.player;

import java.math.BigDecimal;
import execeptions.InvalidPlayerName;
import model.transaction.TransactionArchive;
import execeptions.InsufficientBalanceException;
import model.calculator.SaleCalculator;
import model.stock.Share;

public class Player {

  private final String name;
  private final BigDecimal startingBalance;
  private BigDecimal balance;
  private final Portfolio portfolio;
  private final TransactionArchive transactionArchive;
  private String status;


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


    portfolio = new Portfolio();
    transactionArchive = new TransactionArchive();
    balance = startingBalance;
    status = "Novice";


  }


  public String getName() {
    return name;
  }

  public BigDecimal getStartingBalance() {
    return startingBalance;
  }

  public BigDecimal getBalance() {
    return balance;
  }

  public Portfolio getPortfolio() {
    return portfolio;
  }

  public String getStatus() {
    return status;
  }

  public TransactionArchive getTransactionArchive() {
    return transactionArchive;
  }

  public void withdrawMoney(BigDecimal amount) {
    if (balance.compareTo(amount) < 0) {
      throw new InsufficientBalanceException("Balance is insufficient");
    }
    balance = balance.subtract(amount);
  }

  public void addMoney(BigDecimal amount) {
    balance = balance.add(amount);
  }

  public void setStatus(String status) {
    this.status = status;
  }

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

  public BigDecimal getNetWorth() {
    BigDecimal portfolioNetWorth = balance;
    for (Share share : portfolio.getShares()) {
      SaleCalculator calculator = new SaleCalculator(share);
      portfolioNetWorth =portfolioNetWorth.add(calculator.calculateTotal());
    }
    return portfolioNetWorth;
  }

}
