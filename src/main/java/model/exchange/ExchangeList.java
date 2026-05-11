package model.exchange;

import java.util.ArrayList;
import java.util.List;

public class ExchangeList implements Subject {
  private ArrayList<Exchange> exchanges;
  private List<Observer> observers = new ArrayList<>();
  public ExchangeList() {
    exchanges = new ArrayList<>();

  }

  public void addExchange(Exchange exchange) {
    exchanges.add(exchange);
  }

  public ArrayList<Exchange> getExchanges() {
    return exchanges;
  }

  @Override
  public void addObserver(Observer o) {
    observers.add(o);
  }

  @Override
  public void removeObserver(Observer o) {
    observers.remove(o);
  }

  @Override
  public void notifyObservers() {
    for (Observer observer : observers) {
      observer.onUpdate();
    }
  }
}
