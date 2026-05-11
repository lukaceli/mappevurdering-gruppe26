package model.exchange;

public interface ExchangeSubject {
  void addExchangeObserver(ExchangeObserver o);
  void removeExchangeObserver(ExchangeObserver o);
  void notifyExchangeObservers();
}