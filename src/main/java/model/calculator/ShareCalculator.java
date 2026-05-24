package model.calculator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import model.player.Portfolio;
import model.stock.Share;

/**
 * Provides calculations for individual shares and portfolio-level value changes.
 */
public class ShareCalculator {

  private final Share share;

  /**
   * Constructs a {@code ShareCalculator} for the given share.
   *
   * @param share the share to calculate values for
   */
  public ShareCalculator(Share share) {
    this.share = share;
  }

  /**
   * Calculates the absolute value change of the share since purchase.
   * Computed as (current price - purchase price) * quantity.
   *
   * @return the absolute value change
   */
  public BigDecimal calculateValueChange() {
    BigDecimal currentPrice = share.stock().getCurrentPrice();
    BigDecimal purchasePrice = share.purchasePrice();
    return currentPrice.subtract(purchasePrice).multiply(share.quantity());
  }

  /**
   * Calculates the percentage change in value of the share since purchase.
   * Computed as ((current price - purchase price) / purchase price) * 100,
   * rounded to 2 decimal places using {@link RoundingMode#HALF_UP}.
   *
   * @return the percentage change
   */
  public BigDecimal calculatePercentageChange() {
    BigDecimal currentPrice = share.stock().getCurrentPrice();
    BigDecimal purchasePrice = share.purchasePrice();
    return currentPrice.subtract(purchasePrice)
            .divide(purchasePrice, 2, RoundingMode.HALF_UP)
            .multiply(new BigDecimal(100));
  }

  /**
   * Calculates the total absolute value change across all shares in a portfolio.
   *
   * @param portfolio the portfolio to calculate total value change for
   * @return the sum of value changes across all shares
   */
  public static BigDecimal calculateTotalValueChange(Portfolio portfolio) {
    BigDecimal total = BigDecimal.ZERO;
    for (Share share : portfolio.getShares()) {
      total = total.add(new ShareCalculator(share).calculateValueChange());
    }
    return total;
  }

  /**
   * Calculates the total percentage change of a portfolio relative to a starting balance.
   * Computed as (total value change / starting balance) * 100,
   * rounded to 2 decimal places using {@link RoundingMode#HALF_UP}.
   * Returns zero if {@code startingBalance} is zero.
   *
   * @param portfolio       the portfolio to evaluate
   * @param startingBalance the balance to compare against
   * @return the total percentage change, or zero if starting balance is zero
   */
  public static BigDecimal calculateTotalPercentageChange(Portfolio portfolio,
                                                          BigDecimal startingBalance) {
    BigDecimal totalValueChange = calculateTotalValueChange(portfolio);
    if (startingBalance.compareTo(BigDecimal.ZERO) == 0) {
      return BigDecimal.ZERO;
    }
    return totalValueChange.divide(startingBalance, 2, RoundingMode.HALF_UP)
            .multiply(new BigDecimal(100));
  }

  /**
   * Calculates the total current market value of a share position.
   * Computed as current stock price multiplied by quantity.
   *
   * @param share the share position to evaluate
   * @return the total market value
   */
  public static BigDecimal calculateTotalShareValue(Share share) {
    BigDecimal stockPrice = share.stock().getCurrentPrice();
    BigDecimal quantity = share.quantity();
    return stockPrice.multiply(quantity);
  }
}