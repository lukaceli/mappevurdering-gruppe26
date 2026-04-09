package windows;

import javafx.scene.layout.VBox;
import model.exchange.Exchange;
import model.stock.Stock;

import java.util.ArrayList;

public class ExchangeController {
  private final Exchange exchange;
  private Stock currentStock;

  private ExchangeWindow window;

  public ExchangeController(Exchange exchange,  ExchangeWindow window) {
    this.exchange = exchange;
    currentStock = exchange.getStocks().getFirst();
    this.window = window;
  }


  public void setCurrentStock(Stock currentStock) {
    this.currentStock = currentStock;
  }

  public void updateView() {
    window.setStockName(currentStock.getName());
    window.setStockPrice(currentStock.getCurrentPrice());
    window.setStockSeries(currentStock.getSeries());
  }

  public void onStockClick(Stock stock) {
    if (stock.equals(currentStock))
      return;
    setCurrentStock(stock);
    updateView();

  }

  public ArrayList<Stock> getStocks() {
    return exchange.getStocks();
  }


  public Stock getCurrentStock() {
    return currentStock;
  }

  public VBox getBuyWindow(ExchangeWindow window) {
    return BuyWindow.create(currentStock, window.root());
  }
}
