public class Sale extends Transaction {

  public Sale(Share share, int week, TransactionCalculator calculator) {
    super(share, week, calculator);
  }

  public void commit(Player player) {}
}
