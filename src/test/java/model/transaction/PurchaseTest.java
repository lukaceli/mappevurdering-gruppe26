package model.transaction;

import execeptions.InsufficientBalanceException;
import model.calculator.PurchaseCalculator;
import model.player.Player;
import model.stock.Share;
import model.stock.Stock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PurchaseTest {
  Player player;
  Purchase purchase;
  Share appleShare;
  PurchaseCalculator calculator;
  Stock stock;

  @BeforeEach
  void setUp() {
    player = new Player("Test", new BigDecimal("10000"));
    ArrayList<BigDecimal> stocks = new ArrayList<>();
    stocks.add(new BigDecimal("100"));
    stock = new Stock("APPL", "Apple", stocks);
    appleShare = new Share(stock, new BigDecimal("1"), stock.getCurrentPrice());
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

}