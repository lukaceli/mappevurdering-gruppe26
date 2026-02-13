package model.calculator;

import model.stock.Share;
import model.stock.Stock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PurchaseCalculatorTest {

  private PurchaseCalculator calculator;

  @BeforeEach
  void setUp() {
    BigDecimal purchasePrice = new BigDecimal("100");
    BigDecimal quantity = new BigDecimal("10");

    ArrayList<BigDecimal> salePrices = new ArrayList<>();
    salePrices.add(new BigDecimal("150"));

    Stock stock = new Stock("AAPL", "APPLE", salePrices);
    Share share = new Share(stock, quantity, purchasePrice);

    calculator = new PurchaseCalculator(share);
  }

  @Test
  void calculateGross_shouldReturnPurchasePriceTimesQuantity() {
    assertEquals(
            new BigDecimal("1000"),
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
