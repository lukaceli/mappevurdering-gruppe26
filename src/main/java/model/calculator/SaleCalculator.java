package model.calculator;

import java.math.BigDecimal;
import model.appstate.Difficulty;
import model.stock.Share;

/**
 * Calculates the total proceeds from selling shares, including commission and capital gains tax.
 * Tax is only applied on profit (gross sale minus gross purchase minus commission).
 * If the sale results in a loss, no tax is charged.
 */
public class SaleCalculator implements TransactionCalculator {

  protected final BigDecimal easyCommission = BigDecimal.ZERO;
  protected final BigDecimal normalCommission = new BigDecimal("0.01");
  protected final BigDecimal hardCommission = new BigDecimal("0.015");
  protected final BigDecimal easyTax = new BigDecimal("0.1");
  protected final BigDecimal normalTax = new BigDecimal("0.3");
  protected final BigDecimal hardTax = new BigDecimal("0.4");
  private final BigDecimal purchasePrice;
  private final BigDecimal salesPrice;
  private final BigDecimal quantity;

  /**
   * Constructs a {@code SaleCalculator} from the given share.
   *
   * @param share the share to calculate sale proceeds for
   * @throws NullPointerException if {@code share} is null
   */
  public SaleCalculator(Share share) {
    if (share == null) {
      throw new NullPointerException("Share cannot be null");
    }
    this.purchasePrice = share.getPurchasePrice();
    this.salesPrice = share.getStock().getCurrentPrice();
    this.quantity = share.getQuantity();
  }

  /**
   * Calculates the gross proceeds as sales price multiplied by quantity.
   *
   * @return the gross sale amount
   */
  @Override
  public BigDecimal calculateGross() {
    return salesPrice.multiply(quantity);
  }

  /**
   * Calculates the commission based on the current difficulty level.
   * Commission rates are:
   *   EASY:   0%
   *   NORMAL: 1%
   *   HARD:   1.5%
   *
   * @return the commission amount
   */
  @Override
  public BigDecimal calculateCommission() {
    Difficulty difficulty = Difficulty.getDifficulty();
    BigDecimal commissionRate = switch (difficulty) {
      case EASY -> easyCommission;
      case NORMAL -> normalCommission;
      case HARD -> hardCommission;
    };
    return calculateGross().multiply(commissionRate);
  }

  /**
   * Calculates the capital gains tax on profit after commission.
   * Tax rates are:
   *   EASY:   10%
   *   NORMAL: 30%
   *   HARD:   40%
   * Returns zero if the sale results in a loss.
   *
   * @return the tax amount
   */
  @Override
  public BigDecimal calculateTax() {
    BigDecimal profit = salesPrice.multiply(quantity)
            .subtract(purchasePrice.multiply(quantity))
            .subtract(calculateCommission());
    if (profit.compareTo(BigDecimal.ZERO) < 0) {
      return BigDecimal.ZERO;
    }
    Difficulty difficulty = Difficulty.getDifficulty();
    BigDecimal taxRate = switch (difficulty) {
      case EASY -> easyTax;
      case NORMAL -> normalTax;
      case HARD -> hardTax;
    };
    return profit.multiply(taxRate);
  }

  /**
   * Calculates the total proceeds as gross minus commission minus tax.
   *
   * @return the net proceeds from the sale
   */
  @Override
  public BigDecimal calculateTotal() {
    return calculateGross().subtract(calculateCommission()).subtract(calculateTax());
  }
}