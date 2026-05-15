package archive;

import java.util.ArrayList;
import java.util.List;
import model.player.Player;
import model.stock.Share;
import model.transaction.Purchase;
import model.transaction.Sale;
import model.transaction.Transaction;

public class TransactionArchive {
  private final List<Transaction> transactions;


  public TransactionArchive() {
    this.transactions = new ArrayList<>();

  }

  public boolean add(Transaction transaction) {
    if (transaction.isCommitted()) {
      transactions.add(transaction);
      return true;
    }
    return false;
  }

  public boolean isEmpty() {
    if (transactions.isEmpty()) {
      return true;
    }
    return false;
  }

  public List<Transaction> getTransactions(int week) {
    List<Transaction> transactionsByWeek = new ArrayList<>();

    for (Transaction transAc : transactions) {
      if (transAc.getWeek() == week) {
        transactionsByWeek.add(transAc);
      }
    }
    return transactionsByWeek;
  }

  public List<Purchase> getPurchases(int week) {
    List<Purchase> purchaseList = new ArrayList<>();

    for (Transaction transaction : transactions) {
      if (transaction.getWeek() == week && transaction instanceof Purchase) {
        purchaseList.add((Purchase) transaction);
      }
    }
    return purchaseList;
  }

  public List<Sale> getSales(int week) {
    List<Sale> salesList = new ArrayList<>();
    for (Transaction transaction : transactions) {
      if (transaction.getWeek() == week && transaction instanceof Sale) {
        salesList.add((Sale) transaction);
      }
    }
    return salesList;
  }



  public List<Transaction> getAll() {
    return new ArrayList<>(transactions);
  }
  //ci test kommentar4
  public int countDistinctWeeks() {
    return (int) transactions.stream()
        .map(Transaction::getWeek)
        .distinct()
        .count();
  }

}
