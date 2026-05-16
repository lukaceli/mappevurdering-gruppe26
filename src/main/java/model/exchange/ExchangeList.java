package model.exchange;

import java.util.ArrayList;
import java.util.List;

public class ExchangeList implements ExchangeSubject {
  private final ArrayList<Exchange> exchanges;
  private final List<ExchangeObserver> observers = new ArrayList<>();
  public ExchangeList() {
    exchanges = new ArrayList<>();

  }

  public void addExchange(Exchange exchange) {
    exchanges.add(exchange);
  }

  public void clearExchanges() {
    exchanges.clear();
  }

  public List<Exchange> getExchanges() {
    return exchanges;
  }

  @Override
  public void addExchangeObserver(ExchangeObserver o) {
    observers.add(o);
  }

  @Override
  public void notifyExchangeObservers() {
    for (ExchangeObserver observer : observers) {
      observer.onExchangeUpdate();
    }
  }
}
