package model;

public class Purchase extends Transaction{
  private Share share;
  private int week;
  private boolean comitted;


  public Purchase(Share share, int week, TransactionCalculator calculator) {
    super(share, week, calculator);
  }

  @Override
  public void commit(Player player) {
    //player.calculator.calculateTotal();
    player.getPortfolio().addShare(share);
  }

}
