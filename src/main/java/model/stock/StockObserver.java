package model.stock;

/**
 * Observer interface for receiving notifications when a stock is updated.
 */
public interface StockObserver {

  /**
   * Called when a stock's price or state has been updated.
   */
  void onStockUpdate();
}