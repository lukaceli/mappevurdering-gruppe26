package model.appState;

import model.exchange.*;
import model.player.Player;
import model.stock.Stock;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AppState implements StockSubject, ExchangeSubject, PlayerSubject {
  private final List<StockObserver> stockObservers = new ArrayList<>();
  private final List<ExchangeObserver> exchangeObservers = new ArrayList<>();
  private final List<PlayerObserver> playerObservers = new ArrayList<>();
  private Exchange selectedExchange;
  private Stock selectedStock;
  private Player selectedPlayer;
  private String difficulty;

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

  public void setSelectedPlayer(Player player) throws IOException {
    this.selectedPlayer = player;
    notifyPlayerObservers();


  }

  public Player getSelectedPlayer() {
    return selectedPlayer;
  }

  public void setDifficulty(String difficulty) {
    this.difficulty = difficulty;
  }

  public String getDifficulty() {
    return difficulty;
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
  public void addPlayerObserver(PlayerObserver o) {
    playerObservers.add(o);
  }

  @Override
  public void removePlayerObserver(PlayerObserver o) {
    playerObservers.remove(o);
  }

  @Override
  public void notifyPlayerObservers() throws IOException {
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