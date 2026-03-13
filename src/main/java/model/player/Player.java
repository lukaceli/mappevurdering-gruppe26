package model.player;

import java.math.BigDecimal;
import archive.TransactionArchive;
import execeptions.InsufficientBalanceException;
import model.calculator.SaleCalculator;
import model.stock.Share;

public class Player {

  private String name;
  private BigDecimal startingBalance;
  private BigDecimal balance;
  private Portfolio portfolio;
  private TransactionArchive transactionArchive;
  private String status;
  private Long userId;

  public Player(String name, BigDecimal startingBalance) {
    this.name = name;
    this.startingBalance = startingBalance;
    this.userId = userId;

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

  public Long getId() {
    return userId;
  }

  public TransactionArchive getTransactionArchive() {
    return transactionArchive;
  }

  public void withdrawMoney(BigDecimal amount) {
    if (balance.compareTo(amount) <= 0) {
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
      portfolioNetWorth.add(calculator.calculateTotal());
    }
    return portfolioNetWorth.add(getBalance());
  }

  public void setId(Long id) {
    if (this.userId != null) throw new IllegalStateException("ID already set");
    this.userId = id;
  }
}
