package model.appstate;

import java.util.ArrayList;
import java.util.List;
import model.exchange.Exchange;
import model.exchange.ExchangeObserver;
import model.exchange.ExchangeSubject;
import model.player.Player;
import model.player.PlayerObserver;
import model.player.PlayerSubject;
import model.stock.Stock;
import model.stock.StockObserver;
import model.stock.StockSubject;

/**
 * Central application state holding the currently selected exchange, stock and player.
 * Implements {@link StockSubject}, {@link ExchangeSubject} and {@link PlayerSubject}
 * to notify registered observers when the state changes.
 */
public class AppState implements StockSubject, ExchangeSubject, PlayerSubject {

  private final List<StockObserver> stockObservers = new ArrayList<>();
  private final List<ExchangeObserver> exchangeObservers = new ArrayList<>();
  private final List<PlayerObserver> playerObservers = new ArrayList<>();
  private Exchange selectedExchange;
  private Stock selectedStock;
  private Player selectedPlayer;

  /**
   * Sets the selected exchange and updates the selected stock to the first stock
   * in the exchange. Notifies all exchange observers.
   *
   * @param exchange the exchange to select
   */
  public void setSelectedExchange(Exchange exchange) {
    this.selectedExchange = exchange;
    this.selectedStock = exchange.getStocks().getFirst();
    notifyExchangeObservers();
  }

  /**
   * Sets the selected stock and notifies all stock observers.
   *
   * @param stock the stock to select
   */
  public void setSelectedStock(Stock stock) {
    this.selectedStock = stock;
    notifyStockObservers();
  }

  /**
   * Sets the selected player and notifies all player observers.
   *
   * @param player the player to select
   */
  public void setSelectedPlayer(Player player) {
    this.selectedPlayer = player;
    notifyPlayerObservers();
  }

  /**
   * Returns the currently selected player.
   *
   * @return the selected {@link Player}
   */
  public Player getSelectedPlayer() {
    return selectedPlayer;
  }

  /**
   * Returns the currently selected exchange.
   *
   * @return the selected {@link Exchange}
   */
  public Exchange getSelectedExchange() {
    return selectedExchange;
  }

  /**
   * Returns the currently selected stock.
   *
   * @return the selected {@link Stock}
   */
  public Stock getSelectedStock() {
    return selectedStock;
  }

  @Override
  public void addStockObserver(StockObserver observer) {
    stockObservers.add(observer);
  }

  @Override
  public void removeStockObserver(StockObserver observer) {
    stockObservers.remove(observer);
  }

  @Override
  public void addPlayerObserver(PlayerObserver observer) {
    playerObservers.add(observer);
  }

  @Override
  public void notifyPlayerObservers() {
    for (PlayerObserver observer : playerObservers) {
      observer.gameStart();
    }
  }

  @Override
  public void notifyStockObservers() {
    for (StockObserver observer : stockObservers) {
      observer.onStockUpdate();
    }
  }

  @Override
  public void addExchangeObserver(ExchangeObserver observer) {
    exchangeObservers.add(observer);
  }

  @Override
  public void notifyExchangeObservers() {
    for (ExchangeObserver observer : exchangeObservers) {
      observer.onExchangeUpdate();
    }
  }
}