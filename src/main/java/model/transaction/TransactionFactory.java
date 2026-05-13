package model.transaction;

import model.calculator.PurchaseCalculator;
import model.calculator.SaleCalculator;
import model.stock.Share;

public class TransactionFactory {
  public static Purchase createPurchase(Share share, int week) {
    return new Purchase(share, week, new PurchaseCalculator(share));
  }

  public static Transaction createSale(Share share, int week) {
    return new Sale(share, week, new SaleCalculator(share));
  }
}
