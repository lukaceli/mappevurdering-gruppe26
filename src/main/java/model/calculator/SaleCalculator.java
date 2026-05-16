package model.calculator;

import model.appState.Difficulty;
import model.stock.Share;

import java.math.BigDecimal;

public class SaleCalculator implements TransactionCalculator {
  private final BigDecimal purchasePrice;
  private final BigDecimal salesPrice;
  private final BigDecimal quantity;


  public SaleCalculator(Share share) {
    if (share == null) {
      throw new NullPointerException("Share cannot be null");
    }
    this.purchasePrice = share.getPurchasePrice();
    this.salesPrice = share.getStock().getCurrentPrice();
    this.quantity = share.getQuantity();
  }

  @Override
  public BigDecimal calculateGross() {
    return salesPrice.multiply(quantity);
  }

  @Override
  public BigDecimal calculateCommission() {
    BigDecimal commissionRate;
    Difficulty difficulty = Difficulty.getDifficulty();
    commissionRate = switch (difficulty) {
      case EASY -> BigDecimal.ZERO;
      case NORMAL -> new BigDecimal("0.01");
      case HARD -> new BigDecimal("0.02");
    };
    return calculateGross().multiply(commissionRate);
  }

  @Override
  public BigDecimal calculateTax() {
    BigDecimal grossSale = salesPrice.multiply(quantity);
    BigDecimal grossPurchase = purchasePrice.multiply(quantity);

    BigDecimal profit = grossSale
            .subtract(grossPurchase)
            .subtract(calculateCommission());
    BigDecimal taxRate;
    Difficulty difficulty = Difficulty.getDifficulty();
    taxRate = switch (difficulty) {
      case EASY -> new BigDecimal("0.1");
      case NORMAL -> new BigDecimal("0.3");
      case HARD -> new BigDecimal("0.4");
    };
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
