package model.transaction;

import model.calculator.TransactionCalculator;
import model.player.Player;
import model.stock.Share;

public class Purchase extends Transaction {
  private Share share;
  private int week;
  private boolean comitted;


  public Purchase(Share share, int week, TransactionCalculator calculator) {
    super(share, week, calculator);
  }

  @Override
  public void commit(Player player) {
    //player.model.calculator.calculateTotal();
    player.getPortfolio().addShare(share);
  }

}
