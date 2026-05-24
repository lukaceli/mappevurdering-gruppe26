package model.calculator;

import java.math.BigDecimal;
import model.appstate.Difficulty;
import model.stock.Share;

/**
 * Calculates the total cost of purchasing shares, including commission based on difficulty.
 * Tax is not applied on purchases and always returns zero.
 */
public class PurchaseCalculator implements TransactionCalculator {

  protected final BigDecimal easyCommission = BigDecimal.ZERO;
  protected final BigDecimal normalCommission = new BigDecimal("0.005");
  protected final BigDecimal hardCommission = new BigDecimal("0.01");
  private final BigDecimal purchasePrice;
  private final BigDecimal quantity;

  /**
   * Constructs a {@code PurchaseCalculator} from the given share.
   *
   * @param share the share to calculate purchase cost for
   * @throws NullPointerException if {@code share} is null
   */
  public PurchaseCalculator(Share share) {
    if (share == null) {
      throw new NullPointerException("Share cannot be null");
    }
    this.purchasePrice = share.purchasePrice();
    this.quantity = share.quantity();
  }

  /**
   * Calculates the gross cost as purchase price multiplied by quantity.
   *
   * @return the gross cost
   */
  @Override
  public BigDecimal calculateGross() {
    return purchasePrice.multiply(quantity);
  }

  /**
   * Calculates the commission based on the current difficulty level.
   * Commission rates are:
   *   EASY:   0%
   *   NORMAL: 0.5%
   *   HARD:   1%
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
   * Returns zero as purchases are not subject to tax.
   *
   * @return {@link BigDecimal#ZERO}
   */
  @Override
  public BigDecimal calculateTax() {
    return BigDecimal.ZERO;
  }

  /**
   * Calculates the total purchase cost as gross plus commission plus tax.
   *
   * @return the total cost
   */
  @Override
  public BigDecimal calculateTotal() {
    return calculateGross().add(calculateCommission()).add(calculateTax());
  }
}