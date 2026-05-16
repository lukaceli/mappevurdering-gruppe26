package model.exchange;

public interface ExchangeSubject {
  void addExchangeObserver(ExchangeObserver o);
  void notifyExchangeObservers();
}