package model.exchange;

import model.stock.Stock;

import java.util.ArrayList;

public interface ExchangeObserver {
  void onExchangeUpdate(ArrayList<Stock> stocks);
}
