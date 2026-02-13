package calculator;

import model.TransactionCalculator;

import java.math.BigDecimal;

public class SaleCalculator implements TransactionCalculator {
  private BigDecimal purchasePrice;
  private BigDecimal salesPrice;
  private BigDecimal quantity;



  @Override
  public BigDecimal calculateGross() {
    return null;
  }

  @Override
  public BigDecimal calculateCommission() {
    return null;
  }

  @Override
  public BigDecimal calculateTax() {
    return null;
  }

  @Override
  public BigDecimal calculateTotal() {
    return null;
  }
}
