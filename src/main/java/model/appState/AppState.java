package model.appState;

import model.exchange.Exchange;
import model.exchange.Observer;
import model.exchange.Subject;
import model.stock.Stock;

import java.util.ArrayList;
import java.util.List;

public class AppState implements Subject {
  private final List<Observer> observers = new ArrayList<>();
  private Exchange selectedExchange;
  private Stock selectedStock;

  public void setSelectedExchange(Exchange exchange) {
    this.selectedExchange = exchange;
    this.selectedStock = null;  // nullstill aksje når børs byttes
    notifyObservers();
  }

  public void setSelectedStock(Stock stock) {
    this.selectedStock = stock;
    System.out.println("Selected stock: " + stock.getSymbol());
    notifyObservers();
  }

  public Exchange getSelectedExchange() { return selectedExchange; }
  public Stock getSelectedStock() { return selectedStock; }

  @Override
  public void addObserver(Observer observer) { observers.add(observer); }
  @Override
  public void removeObserver(Observer observer) { observers.remove(observer); }
  @Override
  public void notifyObservers() {
    for (Observer o : observers) o.onUpdate();
  }
}