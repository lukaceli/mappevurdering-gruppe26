package model.calculator;

import model.appState.Difficulty;
import model.stock.Share;

import java.math.BigDecimal;

public class SaleCalculator implements TransactionCalculator {
  private final BigDecimal purchasePrice;
  private final BigDecimal salesPrice;
  private final BigDecimal quantity;
  protected final BigDecimal easyCommission = BigDecimal.ZERO;
  protected final BigDecimal normalCommission = new BigDecimal("0.01");
  protected final BigDecimal hardComission = new BigDecimal("0.015");
  protected final BigDecimal easyTax = new BigDecimal("0.1");
  protected final BigDecimal normalTax= new BigDecimal("0.3");
  protected final BigDecimal hardTax = new BigDecimal("0.4");


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
      case EASY -> easyCommission;
      case NORMAL -> normalCommission;
      case HARD -> hardComission;
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
      case EASY -> easyTax;
      case NORMAL -> normalTax;
      case HARD -> hardTax;
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
