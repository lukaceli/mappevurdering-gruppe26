package model.calculator;

import model.stock.Share;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utility.TestFactory;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SaleCalculatorTest {

  private SaleCalculator calculator;
  private Share share;

  @BeforeEach
  void setUp() {
    // Gross sale: 1000.00, gross purchase: 900.00
    share = TestFactory.createAppleShare();
    calculator = new SaleCalculator(share);
  }
  @Test
  void calculateGross_shouldReturnPurchasePriceTimesQuantity() {
    // 100.00 * 10 = 1000.00
    assertEquals(new BigDecimal("1000.00"), calculator.calculateGross());
  }

  @Test
  void calculateCommission_shouldReturn1PercentOfGross() {
    // 1000.00 * 0.01 = 10.00
    assertEquals(new BigDecimal("10.00"), calculator.calculateCommission().setScale(2));
  }

  @Test
  void calculateTax_shouldReturn30PercentOfProfit() {
    // Profit after commission = 1000.00 - 900.00 - 10.00 = 90.00
    // Tax = 90.00 * 0.30 = 27.00
    assertEquals(new BigDecimal("27.00"), calculator.calculateTax().setScale(2));
  }

  @Test
  void calculateTotal_shouldReturnGrossMinusCommissionMinusTax() {
    // 1000.00 - 10.00 - 27.00 = 963.00
    assertEquals(new BigDecimal("963.00"), calculator.calculateTotal().setScale(2));
  }
}