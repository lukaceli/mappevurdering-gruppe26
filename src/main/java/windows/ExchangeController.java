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
  private Exchange currentExchange;
  private int currentExchangeIndex;
  private AppState appState;

  private ExchangeWindow window;

  public ExchangeController(ExchangeList exchanges, ExchangeWindow window, AppState appState) {
    this.currentExchangeIndex = 0;
    this.exchanges = exchanges;
    this.currentExchange = exchanges.getExchanges().getFirst();
    this.appState = appState;
    appState.setSelectedStock(currentExchange.getStocks().getFirst());
    this.window = window;
  }

  public ArrayList<Stock> getStocks() {
    return currentExchange.getStocks();
  }


  public VBox getBuyWindow(ExchangeWindow window) {
    BuyWindow buyWindow = new BuyWindow();
    return buyWindow.create(appState.getSelectedStock(), window.getRoot(), currentExchange);
  }

  public VBox getSellWindow(ExchangeWindow window) {
    SellWindow sellWindow = new SellWindow();
    return sellWindow.create(appState.getSelectedStock(), window.getRoot(), currentExchange);
  }

  public void nextExchange() {
    currentExchangeIndex += 1;
    currentExchange = exchanges.getExchanges().get(currentExchangeIndex);
  }
}
