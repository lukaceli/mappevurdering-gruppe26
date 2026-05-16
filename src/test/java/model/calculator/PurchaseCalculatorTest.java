package model.calculator;

import model.stock.Share;
import model.stock.Stock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utility.TestFactory;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PurchaseCalculatorTest {

  private PurchaseCalculator calculator;
  private Share share;

  @BeforeEach
  void setUp() {
    share = TestFactory.createShare();
    calculator = new PurchaseCalculator(share);
  }

  @Test
  void calculateGross_shouldReturnPurchasePriceTimesQuantity() {
    assertEquals(new BigDecimal("1000.00")
            ,
            calculator.calculateGross()
    );
  }

  @Test
  void calculateCommission_shouldReturnHalfPercentOfGross() {
    assertEquals(
            new BigDecimal("5.00"),
            calculator.calculateCommission().setScale(2)
    );
  }

  @Test
  void calculateTax_shouldReturnZero() {
    assertEquals(
            BigDecimal.ZERO,
            calculator.calculateTax()
    );
  }

  @Test
  void calculateTotal_shouldReturnGrossPlusCommission() {
    assertEquals(
            new BigDecimal("1005.00"),
            calculator.calculateTotal().setScale(2)
    );
  }
}
