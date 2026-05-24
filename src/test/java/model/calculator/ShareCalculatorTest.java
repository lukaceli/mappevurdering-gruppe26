package model.calculator;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import model.player.Portfolio;
import model.stock.Share;
import model.stock.Stock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utility.TestFactory;

class ShareCalculatorTest {
  private ShareCalculator calculator;
  private Share share;
  private Share share2;
  private Portfolio portfolio;

  @BeforeEach
  void setUp() {
    share = TestFactory.createAppleShare();
    share2 = TestFactory.createGoogleShare();
    calculator = new ShareCalculator(share);
    portfolio = new Portfolio();

  }

  @Test
  void calculateValueChange_returnsCorrectChange() {
    assertEquals(new BigDecimal("100.00"), calculator.calculateValueChange());
  }

  @Test
  void calculatePercentageChange_returnsCorrectPercentage() {
    assertEquals(new BigDecimal("11.00"), calculator.calculatePercentageChange());
  }

  @Test
  void calculateTotalValueChange_returnsCorrectValue() {
    portfolio.addShare(share);
    portfolio.addShare(share2);
    assertEquals(new BigDecimal("303.00"), calculator.calculateTotalValueChange(portfolio));
  }

  @Test
  void calculateTotalValueChange_emptyPortfolio_returnsZero() {
    assertEquals(new BigDecimal("0"), calculator.calculateTotalValueChange(portfolio));
  }

  @Test
  void calculateValueChange_stockDropped_returnsNegativeValue() {
    Stock stock = TestFactory.getAppleStock();
    Share fallenShare = new Share(stock, new BigDecimal("10"), stock.getHighestPrice());
    ShareCalculator calc = new ShareCalculator(fallenShare);
    assertTrue(calc.calculateValueChange().compareTo(BigDecimal.ZERO) < 0);
  }

  @Test
  void calculateTotalPercentageChange_returnsCorrectePercentage() {
    portfolio.addShare(share);
    portfolio.addShare(share2);
    assertEquals(new BigDecimal("10.00"), calculator.calculateTotalPercentageChange(portfolio,
        new BigDecimal("2905.00")));
  }

  @Test
  void calculateTotalPercentageChange_zeroStartingBalance_returnsZero() {
    portfolio.addShare(share);
    assertEquals(BigDecimal.ZERO,
        ShareCalculator.calculateTotalPercentageChange(portfolio, BigDecimal.ZERO));
  }

  @Test
  void calculateTotalShareValue_returnsCorrectValue() {
    assertEquals(new BigDecimal("1000.00"), calculator.calculateTotalShareValue(share));
  }
}