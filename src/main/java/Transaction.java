public abstract class Transaction {
  private Share share;
  private int week;
  private TransactionCalculator calculator;
  private boolean commited;

  protected Transaction(Share share, int week, TransactionCalculator calculator) {}

  public Share getShare() { return share; }

  public int getWeek() { return week; }

  public TransactionCalculator getCalculator() { return calculator; }

  public boolean isCommmitted() {}

  public void commit(Player player) {}
}
