package model.calculator;

import model.appState.Difficulty;
import model.stock.Share;
import java.math.BigDecimal;
import java.math.BigInteger;

public class PurchaseCalculator implements TransactionCalculator {
  private BigDecimal purchasePrice;
  private BigDecimal quantity;

  public PurchaseCalculator(Share share) {
    this.purchasePrice = share.getPurchasePrice();
    this.quantity = share.getQuantity();
  }

  @Override
  public BigDecimal calculateGross() {
    return purchasePrice.multiply(quantity);
  }

  @Override
  public BigDecimal calculateCommission() {
    BigDecimal commissionRate;
    Difficulty difficulty = Difficulty.getDifficulty();
    commissionRate = switch (difficulty) {
      case EASY -> BigDecimal.ZERO;
      case NORMAL -> new BigDecimal("0.005");
      case HARD -> new BigDecimal("0.01");
    };
    return calculateGross().multiply(commissionRate);
  }

  @Override
  public BigDecimal calculateTax() { return BigDecimal.ZERO; }

  @Override
  public BigDecimal calculateTotal() {
    return calculateGross().add(calculateCommission()).add(calculateTax());
  }


}


