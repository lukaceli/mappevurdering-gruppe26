package windows;

import javafx.scene.layout.VBox;
import model.appState.AppState;
import model.exchange.Exchange;
import model.exchange.ExchangeList;
import model.stock.Stock;
import windows.trade.BuyWindow;
import windows.trade.SellWindow;

import java.util.ArrayList;

public class ExchangeController {
  private final ExchangeList exchanges;
  private int currentExchangeIndex;
  private AppState appState;

  private ExchangeWindow window;

  public ExchangeController(ExchangeList exchanges, ExchangeWindow window, AppState appState) {
    this.currentExchangeIndex = 0;
    this.exchanges = exchanges;
    this.appState = appState;
    appState.setSelectedExchange(exchanges.getExchanges().getFirst());
    appState.setSelectedStock(appState.getSelectedExchange().getStocks().getFirst());
    this.window = window;
  }

  public VBox getBuyWindow(ExchangeWindow window) {
    BuyWindow buyWindow = new BuyWindow();
    return buyWindow.create(appState.getSelectedStock(), window.getRoot(), appState.getSelectedExchange());
  }

  public VBox getSellWindow(ExchangeWindow window) {
    SellWindow sellWindow = new SellWindow();
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
}

