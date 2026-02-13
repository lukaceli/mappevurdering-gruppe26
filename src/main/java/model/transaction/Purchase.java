package model.transaction;

import model.calculator.TransactionCalculator;
import model.player.Player;
import model.stock.Share;

import java.math.BigDecimal;

public class Purchase extends Transaction {
  private Share share;
  private int week;
  private boolean committed;


  public Purchase(Share share, int week, TransactionCalculator calculator) {
    super(share, week, calculator);
  }

  @Override
  public void commit(Player player) {
    if (!committed) {
      BigDecimal purchasePriceTotal = calculator.calculateTotal();
      if (player.getBalance().compareTo(purchasePriceTotal) >= 0) {
        player.withdrawMoney(purchasePriceTotal);
        player.getPortfolio().addShare(share);
        committed = true;
        return;
      }
    }
    throw new RuntimeException("Purchase is already committed.");
  }

}
