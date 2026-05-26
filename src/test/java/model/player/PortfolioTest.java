package model.player;

import static org.junit.jupiter.api.Assertions.*;

import model.stock.Share;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utility.TestFactory;

import java.math.BigDecimal;

class PortfolioTest {
  private Portfolio portfolio;
  private Share share;
  private Share share2;

  @BeforeEach
  void setUp() {
    portfolio = new Portfolio();
    share = TestFactory.createAppleShare();
    share2 = TestFactory.createGoogleShare();
  }

  @Test
  void addShare_checksIfShareIsAdded() {
    portfolio.addShare(share);
    portfolio.addShare(share2);
    assertEquals(2, portfolio.getShares().size());
  }

  @Test
  void removeShare_checksIfShareIsRemoved() {
    portfolio.addShare(share);
    portfolio.addShare(share2);
    portfolio.removeShare(share);
    assertEquals(1, portfolio.getShares().size());
  }

  @Test
  void ifPortfolioIsEmpty() {
    assertEquals(0, portfolio.getShares().size());
  }

  @Test
  void removeShare_throwsIfPortfolioIsEmpty() {
    assertThrows(IllegalArgumentException.class, () -> portfolio.removeShare(share));
  }

  @Test
  void addShare_ifShareIsNull() {
    assertThrows(NullPointerException.class, () -> portfolio.addShare(null));
  }

  @Test
  void removeShare_removesCorrectShare() {
    portfolio.addShare(share);
    portfolio.addShare(share2);
    portfolio.removeShare(share);
    assertTrue(portfolio.getShares().contains(share2));
    assertFalse(portfolio.getShares().contains(share));
  }

  @Test
  void removePartialShare_returnsShareWithCorrectRemainingQuantity() {
    portfolio.addShare(share);
    BigDecimal sellQty = new BigDecimal("5");
    Share remaining = portfolio.removePartialShare(share, sellQty);
    assertEquals(share.quantity().subtract(sellQty), remaining.quantity());
  }

  @Test
  void removePartialShare_replacesOriginalShareInPortfolio() {
    portfolio.addShare(share);
    BigDecimal sellQty = new BigDecimal("5");
    Share remaining = portfolio.removePartialShare(share, sellQty);
    assertFalse(portfolio.getShares().contains(share));
    assertTrue(portfolio.getShares().contains(remaining));
  }

  @Test
  void removePartialShare_keepsSamePurchasePrice() {
    portfolio.addShare(share);
    BigDecimal sellQty = new BigDecimal("5");
    Share remaining = portfolio.removePartialShare(share, sellQty);
    assertEquals(share.purchasePrice(), remaining.purchasePrice());
  }
}