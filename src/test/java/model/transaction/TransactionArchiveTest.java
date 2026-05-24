package model.transaction;

import static org.junit.jupiter.api.Assertions.*;

import model.calculator.TransactionCalculator;
import model.player.Player;
import model.stock.Share;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utility.TestFactory;

class TransactionArchiveTest {
  private TransactionArchive transactionArchive;
  private Transaction transaction;
  private Transaction transaction2;
  private Transaction transaction3;
  private Share share;
  private Share share2;
  private TransactionCalculator calculator;

  @BeforeEach
  void setUp() {
    transactionArchive = new TransactionArchive();
    share = TestFactory.createAppleShare();
    share2 = TestFactory.createGoogleShare();
    transaction = TransactionFactory.createPurchase(share, 1);
    transaction2 = TransactionFactory.createPurchase(share2, 2);
    transaction3 = TransactionFactory.createSale(share2, 3);
    Player player = TestFactory.createPlayer();
    transaction.commit(player);
    transaction2.commit(player);
    transaction3.commit(player);

  }

  @Test
  void addMethods_addsTransaction() {
    assertTrue(transactionArchive.add(transaction));
  }

  @Test
  void add_uncommittedTransaction_returnsFalse() {
    Transaction uncommitted = TransactionFactory.createPurchase(share, 1);
    assertFalse(transactionArchive.add(uncommitted));
  }

  @Test
  void isEmpty_newArchive_returnsTrue() {
    assertTrue(transactionArchive.isEmpty());
  }

  @Test
  void isEmpty_afterAdd_returnsFalse() {
    transactionArchive.add(transaction);
    assertFalse(transactionArchive.isEmpty());
  }

  @Test
  void getTransactions_returnsTransactionsForCorrectWeek() {
    transactionArchive.add(transaction);
    transactionArchive.add(transaction2);
    assertEquals(1, transactionArchive.getTransactions(1).size());
  }

  @Test
  void getPurchases_returnsOnlyPurchases() {
    transactionArchive.add(transaction);
    transactionArchive.add(transaction3);
    assertEquals(1, transactionArchive.getPurchases(1).size());
  }

  @Test
  void getSales_returnsOnlySales() {
    transactionArchive.add(transaction2);
    transactionArchive.add(transaction3);
    assertEquals(1, transactionArchive.getSales(3).size());
  }

  @Test
  void countDistinctWeeks_returnsCorrectCount() {
    transactionArchive.add(transaction);
    transactionArchive.add(transaction2);
    transactionArchive.add(transaction3);
    assertEquals(3, transactionArchive.countDistinctWeeks());
  }
}