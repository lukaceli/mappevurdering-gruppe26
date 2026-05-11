package model.appState;

import model.exchange.*;
import model.stock.Stock;

import java.util.ArrayList;
import java.util.List;

public class AppState implements StockSubject, ExchangeSubject {
  private final List<StockObserver> stockObservers = new ArrayList<>();
  private final List<ExchangeObserver> exchangeObservers = new ArrayList<>();
  private Exchange selectedExchange;
  private Stock selectedStock;

  public void setSelectedExchange(Exchange exchange) {
    this.selectedExchange = exchange;
    this.selectedStock = exchange.getStocks().getFirst();
     notifyExchangeObservers();
  }

  public void setSelectedStock(Stock stock) {
    this.selectedStock = stock;
    System.out.println("Selected stock: " + stock.getSymbol());
    notifyStockObservers();
  }

  public Exchange getSelectedExchange() { return selectedExchange; }
  public Stock getSelectedStock() {
    System.out.println("Selected stock fra get: " + selectedStock.getSymbol());
    return selectedStock; }

  @Override
  public void addStockObserver(StockObserver observer) { stockObservers.add(observer); }
  @Override
  public void removeStockObserver(StockObserver observer) { stockObservers.remove(observer); }
  @Override
  public void notifyStockObservers() {
    for (StockObserver o : stockObservers) o.onStockUpdate();
  }

  @Override
  public void addExchangeObserver(ExchangeObserver o) {
    exchangeObservers.add(o);
  }

  @Override
  public void removeExchangeObserver(ExchangeObserver o) {
    exchangeObservers.remove(o);
  }

  @Override
  public void notifyExchangeObservers() {
    for (ExchangeObserver o : exchangeObservers) {
      o.onExchangeUpdate();
    }
  }

}