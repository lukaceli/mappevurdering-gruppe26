package windows.exchange;


import javafx.scene.layout.VBox;
import model.appstate.AppState;
import model.exchange.ExchangeList;
import model.player.Player;
import model.stock.Stock;
import windows.trade.buy.BuyWindow;
import windows.trade.sell.SellWindow;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ExchangeController {
  private final ExchangeList exchanges;
  private int currentExchangeIndex;
  private final AppState appState;
  private final Player player;


  public ExchangeController(ExchangeList exchanges, AppState appState, Player player) {
    this.currentExchangeIndex = 0;
    this.exchanges = exchanges;
    this.appState = appState;
    appState.setSelectedExchange(exchanges.getExchanges().getFirst());
    appState.setSelectedStock(appState.getSelectedExchange().getStocks().getFirst());
    this.player = player;
  }

  public List<Stock> getSortedStocks(List<Stock> stocks, String sortBy) {
    ArrayList<Stock> sorted = new ArrayList<>(stocks);
    switch (sortBy) {
      case "Alfabetical" -> sorted.sort(Comparator.comparing(Stock::getName));
      case "Price"       -> sorted.sort(Comparator.comparing(Stock::getCurrentPrice).reversed());
      case "Biggest Gain" -> sorted.sort(Comparator.comparing(Stock::getLatestPercentageChange).reversed());
    }
    return sorted;
  }

  public VBox getBuyWindow(ExchangeWindow window, Runnable onAfterTrade) {
    BuyWindow buyWindow = new BuyWindow(player);
    buyWindow.setOnAfterTrade(onAfterTrade);
    return buyWindow.create(appState.getSelectedStock(), window.getRoot(), appState.getSelectedExchange());
  }

  public VBox getSellWindow(ExchangeWindow window, Runnable onAfterTrade) {
    SellWindow sellWindow = new SellWindow(player);
    sellWindow.setOnAfterTrade(onAfterTrade);
    return sellWindow.create(appState.getSelectedStock(), window.getRoot(), appState.getSelectedExchange());
  }

  public void nextExchange() {
    currentExchangeIndex += 1;
    if (currentExchangeIndex >= exchanges.getExchanges().size()) {
      currentExchangeIndex = 0;
    }
    appState.setSelectedExchange(exchanges.getExchanges().get(currentExchangeIndex));
  }

  public void previousExchange() {
    currentExchangeIndex -= 1;
    if (currentExchangeIndex < 0) {
      currentExchangeIndex = exchanges.getExchanges().size() - 1;
    }
    appState.setSelectedExchange(exchanges.getExchanges().get(currentExchangeIndex));
  }

  public List<Stock> getTopGainers() {
    return appState.getSelectedExchange().getGainers(5);
  }

  public List<Stock> getTopLosers() {
    return appState.getSelectedExchange().getLosers(5);
  }

  public List<Stock> getFilteredAndSortedStocks(String search, String sortBy) {
    List<Stock> filtered = appState.getSelectedExchange().findStocks(search);
    return getSortedStocks(filtered, sortBy);
  }
}

