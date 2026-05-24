package model.exchange;

/**
 * Subject interface for managing and notifying exchange observers.
 */
public interface ExchangeSubject {

  /**
   * Registers an observer to be notified of exchange updates.
   *
   * @param observer the observer to add
   */
  void addExchangeObserver(ExchangeObserver observer);

  /**
   * Notifies all registered observers of an exchange update.
   */
  void notifyExchangeObservers();
}