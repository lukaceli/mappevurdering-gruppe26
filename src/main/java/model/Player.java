package model;

import java.math.BigDecimal;
import archive.TransactionArchive;

public class Player {

  private String name;
  private  BigDecimal startingBalance;
  private  BigDecimal balance;
  private Portfolio portfolio;
  private TransactionArchive transactionArchive;
  public Player(String name, BigDecimal startingBalance) {
    this.name = name;
    this.startingBalance = startingBalance;
    portfolio = new Portfolio();
    transactionArchive = new TransactionArchive();
    balance = startingBalance;


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

  public TransactionArchive getTransactionArchive() {
    return transactionArchive;
  }

  public void withdrawMoney(BigDecimal amount) {
    balance = balance.subtract(amount);
  }
}
