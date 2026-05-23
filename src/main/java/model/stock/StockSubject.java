package model.stock;

/**
 * Subject interface for managing and notifying stock observers.
 */
public interface StockSubject {

  /**
   * Registers an observer to be notified of stock updates.
   *
   * @param observer the observer to add
   */
  void addStockObserver(StockObserver observer);

  /**
   * Removes a previously registered observer.
   *
   * @param observer the observer to remove
   */
  void removeStockObserver(StockObserver observer);

  /**
   * Notifies all registered observers of a stock update.
   */
  void notifyStockObservers();
}