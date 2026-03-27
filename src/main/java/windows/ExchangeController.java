package windows;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.exchange.Exchange;
import model.exchange.ExchangeObserver;
import model.stock.Stock;

import java.util.ArrayList;

public class ExchangeController {
  private final Exchange exchange;
  private final ArrayList<Label> priceLabels = new ArrayList<>();
  private Stock currentStock;
  private Label currentStockPrice;
  private Label currentStockName;
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
    window.updateChart();
  }

  public void onStockClick(Stock stock) {
    setCurrentStock(stock);
    updateView();

  }

  public ArrayList<Stock> getStocks() {
    return exchange.getStocks();
  }


  public  Stock getCurrentStock() {
    return currentStock;
  }
}
