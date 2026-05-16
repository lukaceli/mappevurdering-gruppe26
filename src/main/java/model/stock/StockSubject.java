package model.stock;

public interface StockSubject {
  void addStockObserver(StockObserver o);
  void removeStockObserver(StockObserver o);
  void notifyStockObservers();
}
