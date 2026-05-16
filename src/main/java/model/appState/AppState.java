package model.appState;

import model.exchange.*;
import model.player.Player;
import model.player.PlayerObserver;
import model.player.PlayerSubject;
import model.stock.Stock;
import model.stock.StockObserver;
import model.stock.StockSubject;

import java.util.ArrayList;
import java.util.List;

public class AppState implements StockSubject, ExchangeSubject, PlayerSubject {
  private final List<StockObserver> stockObservers = new ArrayList<>();
  private final List<ExchangeObserver> exchangeObservers = new ArrayList<>();
  private final List<PlayerObserver> playerObservers = new ArrayList<>();
  private Exchange selectedExchange;
  private Stock selectedStock;
  private Player selectedPlayer;

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

  public void setSelectedPlayer(Player player) {
    this.selectedPlayer = player;
    notifyPlayerObservers();


  }

  public Player getSelectedPlayer() {
    return selectedPlayer;
  }

  public Exchange getSelectedExchange() { return selectedExchange; }
  public Stock getSelectedStock() {
    return selectedStock; }

  @Override
  public void addStockObserver(StockObserver observer) { stockObservers.add(observer); }
  @Override
  public void removeStockObserver(StockObserver observer) { stockObservers.remove(observer); }

  @Override
  public void addPlayerObserver(PlayerObserver o) {
    playerObservers.add(o);
  }



  @Override
  public void notifyPlayerObservers() {
    for (PlayerObserver observer : playerObservers) {
      observer.gameStart();
    }
  }

  @Override
  public void notifyStockObservers() {
    for (StockObserver o : stockObservers) o.onStockUpdate();
  }

  @Override
  public void addExchangeObserver(ExchangeObserver o) {
    exchangeObservers.add(o);
  }

  @Override
  public void notifyExchangeObservers() {
    for (ExchangeObserver o : exchangeObservers) {
      o.onExchangeUpdate();
    }
  }

}