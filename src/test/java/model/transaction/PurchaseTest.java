package model.transaction;

import execeptions.DobbleCommitException;
import execeptions.InsufficientBalanceException;
import model.calculator.PurchaseCalculator;
import model.player.Player;
import model.stock.Share;
import model.stock.Stock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utility.TestFactory;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PurchaseTest {
  Player player;
  Purchase purchase;
  Share appleShare;
  PurchaseCalculator calculator;
  Stock stock;

  @BeforeEach
  void setUp() {
    player = TestFactory.createPlayer();
    stock = TestFactory.getAppleStock();
    appleShare = TestFactory.createAppleShare();
    purchase = new Purchase(appleShare, 1, new PurchaseCalculator(appleShare));
    calculator = new PurchaseCalculator(appleShare);
  }

  @Test
    void commitRemovesBalance() {
    assertEquals(player.getStartingBalance(), player.getBalance());
    purchase.commit(player);
    assertEquals(player.getStartingBalance().subtract(calculator.calculateTotal()), player.getBalance());
  }

  @Test
    void committhrowserrorwheninnsufficientbalance() {
    Share share = new Share(stock, new BigDecimal("1000"), stock.getCurrentPrice());
    Purchase bigPurchase =  new Purchase(share, 1, new PurchaseCalculator(share));
    assertThrows(InsufficientBalanceException.class, () -> bigPurchase.commit(player));
    }
  @Test
  void commitAddsShareToPortfolio() {
    purchase.commit(player);
    assertEquals(1, player.getPortfolio().getShares().size());
  }

  @Test
  void commitThrowsWhenAlreadyCommitted() {
    purchase.commit(player);
    assertThrows(DobbleCommitException.class, () -> purchase.commit(player));
  }
}