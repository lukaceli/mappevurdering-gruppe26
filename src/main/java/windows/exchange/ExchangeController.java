package windows.exchange;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javafx.scene.layout.VBox;
import model.appstate.AppState;
import model.exchange.ExchangeList;
import model.player.Player;
import model.stock.Stock;
import windows.trade.buy.BuyWindow;
import windows.trade.sell.SellWindow;

/**
 * Controller for the exchange window, handling navigation between exchanges,
 * stock sorting and filtering, and creation of trade windows.
 */
public class ExchangeController {

  private final ExchangeList exchanges;
  private final AppState appState;
  private final Player player;
  private int currentExchangeIndex;

  /**
   * Constructs an {@code ExchangeController} and sets the initial selected exchange and stock.
   *
   * @param exchanges the list of available exchanges
   * @param appState  the shared application state
   * @param player    the current player
   */
  public ExchangeController(ExchangeList exchanges, AppState appState, Player player) {
    this.currentExchangeIndex = 0;
    this.exchanges = exchanges;
    this.appState = appState;
    this.player = player;
    appState.setSelectedExchange(exchanges.getExchanges().getFirst());
    appState.setSelectedStock(appState.getSelectedExchange().getStocks().getFirst());
  }

  /**
   * Returns the given list of stocks sorted by the specified criterion.
   *
   * Supported sort values are:
   *   Alphabetical: sorted by name ascending
   *   Price:        sorted by current price descending
   *   Biggest Gain: sorted by latest percentage change descending
   *
   * @param stocks the stocks to sort
   * @param sortBy the sort criterion
   * @return a sorted copy of the stock list
   */
  public List<Stock> getSortedStocks(List<Stock> stocks, String sortBy) {
    ArrayList<Stock> sorted = new ArrayList<>(stocks);
    switch (sortBy) {
      case "Alphabetical" -> sorted.sort(Comparator.comparing(Stock::getName));
      case "Price" -> sorted.sort(Comparator.comparing(Stock::getCurrentPrice).reversed());
      case "Biggest Gain" ->
              sorted.sort(Comparator.comparing(Stock::getLatestPercentageChange).reversed());
      default -> { }
    }
    return sorted;
  }

  /**
   * Creates and returns a buy window for the currently selected stock.
   *
   * @param window       the parent exchange window
   * @param onAfterTrade a callback to run after the trade is completed
   * @return the buy window as a {@link VBox}
   */
  public VBox getBuyWindow(ExchangeWindow window, Runnable onAfterTrade) {
    BuyWindow buyWindow = new BuyWindow(player);
    buyWindow.setOnAfterTrade(onAfterTrade);
    return buyWindow.create(appState.getSelectedStock(), window.getRoot(),
            appState.getSelectedExchange());
  }

  /**
   * Creates and returns a sell window for the currently selected stock.
   *
   * @param window       the parent exchange window
   * @param onAfterTrade a callback to run after the trade is completed
   * @return the sell window as a {@link VBox}
   */
  public VBox getSellWindow(ExchangeWindow window, Runnable onAfterTrade) {
    SellWindow sellWindow = new SellWindow(player);
    sellWindow.setOnAfterTrade(onAfterTrade);
    return sellWindow.create(appState.getSelectedStock(), window.getRoot(),
            appState.getSelectedExchange());
  }

  /**
   * Advances to the next exchange, wrapping around to the first if at the end.
   */
  public void nextExchange() {
    currentExchangeIndex++;
    if (currentExchangeIndex >= exchanges.getExchanges().size()) {
      currentExchangeIndex = 0;
    }
    appState.setSelectedExchange(exchanges.getExchanges().get(currentExchangeIndex));
  }

  /**
   * Goes back to the previous exchange, wrapping around to the last if at the beginning.
   */
  public void previousExchange() {
    currentExchangeIndex--;
    if (currentExchangeIndex < 0) {
      currentExchangeIndex = exchanges.getExchanges().size() - 1;
    }
    appState.setSelectedExchange(exchanges.getExchanges().get(currentExchangeIndex));
  }

  /**
   * Returns the top 5 gaining stocks on the currently selected exchange.
   *
   * @return a list of top gaining {@link Stock} objects
   */
  public List<Stock> getTopGainers() {
    return appState.getSelectedExchange().getGainers(5);
  }

  /**
   * Returns the top 5 losing stocks on the currently selected exchange.
   *
   * @return a list of top losing {@link Stock} objects
   */
  public List<Stock> getTopLosers() {
    return appState.getSelectedExchange().getLosers(5);
  }

  /**
   * Returns stocks from the current exchange filtered by search term and sorted by criterion.
   *
   * @param search the search term to filter by
   * @param sortBy the sort criterion
   * @return a filtered and sorted list of {@link Stock} objects
   */
  public List<Stock> getFilteredAndSortedStocks(String search, String sortBy) {
    List<Stock> filtered = appState.getSelectedExchange().findStocks(search);
    return getSortedStocks(filtered, sortBy);
  }
}