package model.calculator;

import model.stock.Share;
import model.stock.Stock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SaleCalculatorTest {

  private SaleCalculator calculator;

  @BeforeEach
  void setUp() {
    BigDecimal purchasePrice = new BigDecimal("100");
    BigDecimal salePrice = new BigDecimal("150");
    BigDecimal quantity = new BigDecimal("10");
    ArrayList<BigDecimal> salePrices = new ArrayList<>();
    salePrices.add(salePrice);

    Stock stock = new Stock("AAPL", "APPLE", salePrices);
    Share share = new Share(stock, quantity, purchasePrice);
    calculator = new SaleCalculator(share);
  }

  @Test
  void calculateGross_shouldReturnPurchasePriceTimesQuantity() {
    assertEquals(
            new BigDecimal("1500"),
            calculator.calculateGross()
    );
  }

  @Test
  void calculateCommission_shouldReturn1PercentOfGross() {
    assertEquals(
            new BigDecimal("15.00"),
            calculator.calculateCommission().setScale(2)
    );
  }

  @Test
  void calculateTax_shouldReturn30PercentOfProfit() {
    //gross sale 1500 - gross purchase 100 = 500 - comission = 485 * 30% = 145.5
    assertEquals(
            new BigDecimal("145.50"),
            calculator.calculateTax().setScale(2)
    );
  }

  @Test
  void calculateTotal_shouldReturnGrossMinusCommisionMinusTax() {
    assertEquals(new BigDecimal("1339.50"),  calculator.calculateTotal().setScale(2));
  }
}