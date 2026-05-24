package model.transaction;

import static org.junit.jupiter.api.Assertions.*;

import execeptions.DobbleCommitException;
import java.math.BigDecimal;
import model.calculator.PurchaseCalculator;
import model.calculator.SaleCalculator;
import model.player.Player;
import model.stock.Share;
import model.stock.Stock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utility.TestFactory;

class SaleTest {
  private Player player;
  private Sale sale;
  private Share share;
  private SaleCalculator calculator;
  private Stock stock;

  @BeforeEach
  void setUp() {
    player = TestFactory.createPlayer();
    stock = TestFactory.getAppleStock();
    share = TestFactory.createAppleShare();
    sale = new Sale(share, 1, new PurchaseCalculator(share));
    calculator = new SaleCalculator(share);
  }

  @Test
  void commit_addsMoneyToPlayer() {
    BigDecimal expected = player.getBalance().add(sale.getCalculator().calculateTotal());
    sale.commit(player);
    assertEquals(expected, player.getBalance());
  }


  @Test
  void commit_removesShareFromPortfolio() {
    sale.commit(player);
    assertEquals(0, player.getPortfolio().getShares().size());
  }

  @Test
  void commit_throwsWhenAlreadyCommitted() {
    sale.commit(player);
    assertThrows(DobbleCommitException.class, () -> sale.commit(player));
  }
}