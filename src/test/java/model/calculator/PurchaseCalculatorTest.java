package model.calculator;

import model.stock.Share;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utility.TestFactory;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PurchaseCalculatorTest {

  private PurchaseCalculator calculator;
  private Share share;

  @BeforeEach
  void setUp() {
    share = TestFactory.createAppleShare();
    calculator = new PurchaseCalculator(share);
  }

  @Test
  void calculateGross_shouldReturnPurchasePriceTimesQuantity() {
    // 90.00 * 10 = 900.00
    assertEquals(new BigDecimal("900.00"), calculator.calculateGross());
  }

  @Test
  void calculateCommission_shouldReturnHalfPercentOfGross() {
    // 900.00 * 0.005 = 4.50
    assertEquals(new BigDecimal("4.50"), calculator.calculateCommission().setScale(2));
  }

  @Test
  void calculateTax_shouldReturnZero() {
    // Ingen skatt ved kjøp
    assertEquals(BigDecimal.ZERO, calculator.calculateTax());
  }

  @Test
  void calculateTotal_shouldReturnGrossPlusCommission() {
    // 900.00 + 4.50 = 904.50
    assertEquals(new BigDecimal("904.50"), calculator.calculateTotal().setScale(2));
  }
}
