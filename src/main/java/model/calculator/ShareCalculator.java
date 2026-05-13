package model.calculator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import model.player.Player;
import model.player.Portfolio;
import model.stock.Share;

public class ShareCalculator {
  private final Share share;




  public ShareCalculator(Share share) {
    this.share = share;
  }

  public BigDecimal calculateValueChange() {
    BigDecimal currentPrice = share.getStock().getCurrentPrice();
    BigDecimal purchasePrice = share.getPurchasePrice();

    return currentPrice.subtract(purchasePrice).multiply(share.getQuantity());
  }

  public BigDecimal calculatePercentageChange() {
    BigDecimal currentPrice = share.getStock().getCurrentPrice();
    BigDecimal purchasePrice = share.getPurchasePrice();

    return currentPrice.subtract(purchasePrice).divide(purchasePrice, 2, RoundingMode.HALF_UP).
        multiply(new BigDecimal(100));
  }

  public static BigDecimal calculateTotalValueChange(Portfolio portfolio) {
    BigDecimal total = BigDecimal.ZERO;
    for (Share share : portfolio.getShares()) {
      total = total.add(new ShareCalculator(share).calculateValueChange());
    }
    return total;
  }

  public static BigDecimal calculateTotalPercentageChange(Portfolio portfolio,
                                                          BigDecimal startingBalance) {
    BigDecimal totalValueChange = calculateTotalValueChange(portfolio);
    if (startingBalance.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO;
    return totalValueChange.divide(startingBalance, 2, RoundingMode.HALF_UP)
        .multiply(new BigDecimal(100));
  }

  public static BigDecimal calculateTotalShareValue(Share share) {
    BigDecimal stockPrice = share.getStock().getCurrentPrice();
    BigDecimal quantity = share.getQuantity();
    return stockPrice.multiply(quantity);
  }
}
