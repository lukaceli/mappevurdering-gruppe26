package model.player;

import static org.junit.jupiter.api.Assertions.*;

import model.stock.Share;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utility.TestFactory;

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
    assertThrows(NullPointerException.class, () -> portfolio.removeShare(share));
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
}