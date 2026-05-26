package model.transaction;

import model.calculator.PurchaseCalculator;
import model.calculator.SaleCalculator;
import model.stock.Share;

/**
 * Factory class for creating {@link Transaction} instances.
 */
public class TransactionFactory {

  private TransactionFactory() {
  }

  /**
   * Creates a new uncommitted {@link Purchase} transaction.
   *
   * @param share the share to purchase
   * @param week  the week in which the purchase occurs
   * @return a new {@link Purchase} transaction
   */
  public static Purchase createPurchase(Share share, int week) {
    return new Purchase(share, week, new PurchaseCalculator(share));
  }

  /**
   * Creates a new uncommitted {@link Sale} transaction.
   *
   * @param share the share to sell
   * @param week  the week in which the sale occurs
   * @return a new {@link Sale} transaction
   */
  public static Sale createSale(Share share, int week) {
    return new Sale(share, week, new SaleCalculator(share));
  }
}