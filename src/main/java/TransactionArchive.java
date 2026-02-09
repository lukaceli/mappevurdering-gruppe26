import java.util.List;

public class TransactionArchive {
  private final List<Transaction> transactions;

  public TransactionArchive() {}

  public boolean add(Transaction transaction) { return true; }

  public boolean isEmpty() { return true; }

  public List<Transaction> getTransactions(int week) { return null; }

  public List<Purchase> getPurchases(int week) { return null; }

  public List<Sale> getSales(int week) { return null; }

  public int countDistinctWeeks() { return 0;}

}
