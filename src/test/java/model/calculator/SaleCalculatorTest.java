package model.calculator;

import model.appstate.Difficulty;
import model.stock.Share;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utility.TestFactory;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

  @Test
  void calculateCommission_shouldReturnZeroOnEasy() {
    Difficulty.setDifficulty(Difficulty.EASY);
    SaleCalculator easyCalculator = new SaleCalculator(share);
    // 1000.00 * 0 = 0.00
    assertEquals(new BigDecimal("0.00"), easyCalculator.calculateCommission().setScale(2));
  }

  @Test
  void calculateCommission_shouldReturn1PercentOnHard() {
    Difficulty.setDifficulty(Difficulty.HARD);
    SaleCalculator hardCalculator = new SaleCalculator(share);
    // 1000.00 * 0.015 = 15.00
    assertEquals(new BigDecimal("15.00"), hardCalculator.calculateCommission().setScale(2));
  }

  @Test
  void calculateTax_shouldReturn10PercentOnEasy() {
    Difficulty.setDifficulty(Difficulty.EASY);
    SaleCalculator easyCalculator = new SaleCalculator(share);
    // Profit = 1000.00 - 900.00 - 0.00 = 100.00, tax = 100.00 * 0.10 = 10.00
    assertEquals(new BigDecimal("10.00"), easyCalculator.calculateTax().setScale(2));
  }

  @Test
  void calculateTax_shouldReturn40PercentOnHard() {
    Difficulty.setDifficulty(Difficulty.HARD);
    SaleCalculator hardCalculator = new SaleCalculator(share);
    // Profit = 1000.00 - 900.00 - 15.00 = 85.00, tax = 85.00 * 0.40 = 34.00
    assertEquals(new BigDecimal("34.00"), hardCalculator.calculateTax().setScale(2));
  }

  @Test
  void calculateTax_shouldReturnZeroWhenNoProfit() {
    Share breakEvenShare = new Share(
            TestFactory.getAppleStock(),
            new BigDecimal("10"),
            TestFactory.getAppleStock().getCurrentPrice()
    );
    SaleCalculator breakEvenCalculator = new SaleCalculator(breakEvenShare);
    // Purchase price = sale price → no profit → tax = 0
    assertEquals(new BigDecimal("0.00"), breakEvenCalculator.calculateTax().setScale(2));
  }

  @Test
  void constructor_shouldThrowWhenShareIsNull() {
    assertThrows(NullPointerException.class, () -> new SaleCalculator(null));
  }
}