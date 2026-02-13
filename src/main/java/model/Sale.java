package model;

import calculator.TransactionCalculator;

public class Sale extends Transaction {

  public Sale(Share share, int week, TransactionCalculator calculator) {
    super(share, week, calculator);
  }



  @Override
  public void commit(Player player) {
    player.getPortfolio().removeShare(share);
  }
}
