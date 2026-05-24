package model.transaction;

import static org.junit.jupiter.api.Assertions.*;

import model.stock.Share;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utility.TestFactory;

class TransactionFactoryTest {
  private Share share;

  @BeforeEach
  void setUp() {
    share = TestFactory.createAppleShare();
  }

  @Test
  void createPurchase_returnsInstanceOfPurchase() {
    Transaction transaction = TransactionFactory.createPurchase(share, 1);
    assertInstanceOf(Purchase.class, transaction);
  }

  @Test
  void createSale_returnsInstanceOfSale() {
    Transaction transaction = TransactionFactory.createSale(share, 1);
    assertInstanceOf(Sale.class, transaction);
  }

  @Test
  void createPurchase_hasCorrectWeek() {
    Transaction transaction = TransactionFactory.createPurchase(share, 5);
    assertEquals(5, transaction.getWeek());
  }

  @Test
  void createSale_hasCorrectWeek() {
    Transaction transaction = TransactionFactory.createSale(share, 3);
    assertEquals(3, transaction.getWeek());
  }

  @Test
  void createPurchase_nullShare_throwsException() {
    assertThrows(NullPointerException.class, () -> TransactionFactory.createPurchase(null, 1));
  }

  @Test
  void createSale_nullShare_throwsException() {
    assertThrows(NullPointerException.class, () -> TransactionFactory.createSale(null, 1));
  }
}