package calculator;

import java.math.BigDecimal;

public class PurchaseCalculator implements TransactionCalculator {
  private BigDecimal purchasePrice;
  private BigDecimal quantity;

  public PurchaseCalculator(Share share) {
  }

  @Override
  public BigDecimal calculateGross() { return null; }

  @Override
  public BigDecimal calculateCommission() { return null; }

  @Override
  public BigDecimal calculateTax() { return null; }

  @Override
  public BigDecimal calculateTotal() { return null; }

}
