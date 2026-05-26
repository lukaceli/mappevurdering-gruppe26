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

  @Test
  void commit_archivesTransaction() {
    sale.commit(player);
    assertFalse(player.getTransactionArchive().isEmpty());
  }

  @Test
  void commitPartial_addsMoneyToPlayer() {
    Share toSell = new Share(share.stock(), new BigDecimal("4"), share.purchasePrice());
    Sale partialSale = new Sale(toSell, 1, new SaleCalculator(toSell));
    BigDecimal expected = player.getBalance().add(partialSale.getCalculator().calculateTotal());
    partialSale.commitPartial(player, share);
    assertEquals(expected, player.getBalance());
  }

  @Test
  void commitPartial_returnsRemainingShare() {
    Share toSell = new Share(share.stock(), new BigDecimal("4"), share.purchasePrice());
    Sale partialSale = new Sale(toSell, 1, new SaleCalculator(toSell));
    Share remaining = partialSale.commitPartial(player, share);
    assertEquals(new BigDecimal("6"), remaining.quantity());
  }

  @Test
  void commitPartial_leavesRemainingInPortfolio() {
    Share toSell = new Share(share.stock(), new BigDecimal("4"), share.purchasePrice());
    Sale partialSale = new Sale(toSell, 1, new SaleCalculator(toSell));
    partialSale.commitPartial(player, share);
    assertEquals(1, player.getPortfolio().getShares().size());
    assertEquals(new BigDecimal("6"), player.getPortfolio().getShares().getFirst().quantity());
  }

  @Test
  void commitPartial_archivesTransaction() {
    Share toSell = new Share(share.stock(), new BigDecimal("4"), share.purchasePrice());
    Sale partialSale = new Sale(toSell, 1, new SaleCalculator(toSell));
    partialSale.commitPartial(player, share);
    assertFalse(player.getTransactionArchive().isEmpty());
  }

  @Test
  void commitPartial_throwsWhenAlreadyCommitted() {
    Share toSell = new Share(share.stock(), new BigDecimal("4"), share.purchasePrice());
    Sale partialSale = new Sale(toSell, 1, new SaleCalculator(toSell));
    partialSale.commitPartial(player, share);
    assertThrows(DoubleCommitException.class, () -> partialSale.commitPartial(player, share));
  }
}