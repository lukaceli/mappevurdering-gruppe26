public class Purchase extends Transaction{
  private Share share;
  private int week;
  private boolean comitted;


  public Purchase(Share share, int week, TransactionCalculator calculator) {
    super(share, week, calculator);
  }

  public void commit(Player player) {}

}
