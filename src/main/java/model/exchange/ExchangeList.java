package model.exchange;

import java.util.ArrayList;
import java.util.List;

/**
 * Maintains a list of available exchanges and notifies observers when the list changes.
 */
public class ExchangeList implements ExchangeSubject {

  private final ArrayList<Exchange> exchanges;
  private final List<ExchangeObserver> observers = new ArrayList<>();

  /**
   * Constructs an empty {@code ExchangeList}.
   */
  public ExchangeList() {
    exchanges = new ArrayList<>();
  }

  /**
   * Adds an exchange to the list.
   *
   * @param exchange the exchange to add
   */
  public void addExchange(Exchange exchange) {
    exchanges.add(exchange);
  }

  /**
   * Removes all exchanges from the list.
   */
  public void clearExchanges() {
    exchanges.clear();
  }

  /**
   * Returns all exchanges in the list.
   *
   * @return a list of all {@link Exchange} objects
   */
  public List<Exchange> getExchanges() {
    return exchanges;
  }

  @Override
  public void addExchangeObserver(ExchangeObserver observer) {
    observers.add(observer);
  }

  @Override
  public void notifyExchangeObservers() {
    for (ExchangeObserver observer : observers) {
      observer.onExchangeUpdate();
    }
  }
}