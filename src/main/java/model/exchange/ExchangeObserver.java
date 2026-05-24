package model.exchange;

/**
 * Observer interface for receiving notifications when the selected exchange changes.
 */
public interface ExchangeObserver {

  /**
   * Called when the selected exchange has been updated.
   */
  void onExchangeUpdate();
}