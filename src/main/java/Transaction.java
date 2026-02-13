public abstract class Transaction {
  protected Share share;
  protected int week;
  protected TransactionCalculator calculator;
  protected boolean commited;

  protected Transaction(Share share, int week, TransactionCalculator calculator) {
    this.share = share;
    this.week = week;
    this.calculator = calculator;
  }

  public Share getShare() { return share; }

  public int getWeek() { return week; }

  public TransactionCalculator getCalculator() { return calculator; }

  public boolean isCommmitted() {return true;}

  public abstract void commit(Player player);
}
