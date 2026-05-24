package model.transaction;

import static org.junit.jupiter.api.Assertions.*;

import exceptions.DoubleCommitException;
import java.math.BigDecimal;
import model.calculator.SaleCalculator;
import model.player.Player;
import model.stock.Share;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utility.TestFactory;

class SaleTest {
  private Player player;
  private Sale sale;
  private Share share;

  @BeforeEach
  void setUp() {
    player = TestFactory.createPlayer();
    share = TestFactory.createAppleShare();
    player.getPortfolio().addShare(share);
    sale = new Sale(share, 1, new SaleCalculator(share));
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
    assertThrows(DoubleCommitException.class, () -> sale.commit(player));
  }
}