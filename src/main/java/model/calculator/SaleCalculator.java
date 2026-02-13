package model.calculator;

import model.stock.Share;

import java.math.BigDecimal;

public class SaleCalculator implements TransactionCalculator {
  private BigDecimal purchasePrice;
  private BigDecimal salesPrice;
  private BigDecimal quantity;

  public SaleCalculator(Share share) {
    this.purchasePrice = share.getPurchasePrice();
    this.salesPrice = share.getStock().getSalePrice();
    this.quantity = share.getQuantity();
  }

  @Override
  public BigDecimal calculateGross() {
    return salesPrice.multiply(quantity);
  }

  @Override
  public BigDecimal calculateCommission() {
    BigDecimal commissionRate = new BigDecimal("0.01");
    return calculateGross().multiply(commissionRate);
  }

  @Override
  public BigDecimal calculateTax() {
    BigDecimal grossSale = salesPrice.multiply(quantity);
    BigDecimal grossPurchase = purchasePrice.multiply(quantity);

    BigDecimal profit = grossSale
            .subtract(grossPurchase)
            .subtract(calculateCommission());
    BigDecimal taxRate = new BigDecimal("0.3");
    if (profit.compareTo(BigDecimal.ZERO) < 0) {
      return BigDecimal.ZERO;
    } else {
      return profit.multiply(taxRate);
    }
  }

  @Override
  public BigDecimal calculateTotal() {
    return calculateGross().subtract(calculateCommission()).subtract(calculateTax());
  }
}
