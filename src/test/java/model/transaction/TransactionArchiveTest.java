package model.transaction;

import static org.junit.jupiter.api.Assertions.*;

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
  void getAll_returnsTransactionsForCorrectWeek() {
    transactionArchive.add(transaction);
    transactionArchive.add(transaction2);
    long count = transactionArchive.getAll().stream()
        .filter(t -> t.getWeek() == 1).count();
    assertEquals(1, count);
  }

  @Test
  void getAll_returnsOnlyPurchases() {
    transactionArchive.add(transaction);
    transactionArchive.add(transaction3);
    long count = transactionArchive.getAll().stream()
        .filter(t -> t instanceof Purchase).count();
    assertEquals(1, count);
  }

  @Test
  void getAll_returnsOnlySales() {
    transactionArchive.add(transaction2);
    transactionArchive.add(transaction3);
    long count = transactionArchive.getAll().stream()
        .filter(t -> t instanceof Sale).count();
    assertEquals(1, count);
  }

  @Test
  void countDistinctWeeks_returnsCorrectCount() {
    transactionArchive.add(transaction);
    transactionArchive.add(transaction2);
    transactionArchive.add(transaction3);
    assertEquals(3, transactionArchive.countDistinctWeeks());
  }
}