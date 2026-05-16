package model.calculator;

import model.appState.Difficulty;
import model.stock.Share;
import model.stock.Stock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utility.TestFactory;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SaleCalculatorTest {

  private SaleCalculator calculator;
  private Share share;

  @BeforeEach
  void setUp() {
    share = TestFactory.createShare();
    calculator = new SaleCalculator(share);
  }

  @Test
  void calculateGross_shouldReturnPurchasePriceTimesQuantity() {
    assertEquals(
            new BigDecimal("1000.00"),
            calculator.calculateGross()
    );
  }

  @Test
  void calculateCommission_shouldReturn1PercentOfGross() {
    assertEquals(
            new BigDecimal("10.00"),
            calculator.calculateCommission().setScale(2)
    );
  }

  @Test
  void calculateTax_shouldReturn30PercentOfProfit() {
    //gross sale 1002 - gross purchase 100 = 500 - comission = 485 * 30% = 145.5
    assertEquals(
            new BigDecimal("0.00"),
            calculator.calculateTax().setScale(2)
    );
  }

  @Test
  void calculateTotal_shouldReturnGrossMinusCommisionMinusTax() {
    assertEquals(new BigDecimal("990.00"),  calculator.calculateTotal().setScale(2));
  }
}