import java.util.ArrayList;
import java.util.List;
import model.Purchase;
import model.Sale;
import model.Transaction;

public class TransactionArchive {
  private final List<Transaction> transactions;

  public TransactionArchive() {
    this.transactions = new ArrayList<>();
  }

  public boolean add(Transaction transaction) { return true; }

  public boolean isEmpty() { return true; }

  public List<Transaction> getTransactions(int week) { return null; }

  public List<Purchase> getPurchases(int week) { return null; }

  public List<Sale> getSales(int week) { return null; }

  public int countDistinctWeeks() { return 0;}

}
