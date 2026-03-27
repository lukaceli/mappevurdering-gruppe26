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

public class ExchangeController implements ExchangeObserver {
  private final Exchange exchange;
  private final ArrayList<Label> priceLabels = new ArrayList<>();
  private Stock currentStock;
  private Label currentStockPrice;
  private Label currentStockName;
  private final XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();
  private ExchangeWindow window;

  public ExchangeController(Exchange exchange,  ExchangeWindow window) {
    this.exchange = exchange;
    currentStock = exchange.getStocks().getFirst();
    exchange.addObserver(this);
    this.window = window;
  }


  public void setCurrentStock(Stock currentStock) {
    this.currentStock = currentStock;
  }


  public LineChart<Number, Number> createStockChart() {
    LineChart<Number, Number> stockChart;
    ArrayList<Integer> weeks = new ArrayList<>();
    for (int i = 1; i <= currentStock.getPriceHistory().size(); i++) {
      weeks.add(i);
    }
    NumberAxis xAxis = new NumberAxis();
    NumberAxis yAxis = new NumberAxis();
    xAxis.setLabel("Weeks");
    yAxis.setLabel("Stock Price");
    yAxis.setForceZeroInRange(false);
    yAxis.setAutoRanging(true);
    stockChart = new LineChart<>(xAxis, yAxis);
    stockChart.setAnimated(false);
    stockChart.setTitle("Price history");
    for (int i = 0; i < weeks.size(); i++) {
      series.getData().add(new XYChart.Data<>(i, currentStock.getPriceHistory().get(i)));
    }
    stockChart.setLegendVisible(false);
    series.setName("Wewe");
    stockChart.getData().add(series);
    return stockChart;
  }

  public void updateChart() {
    series.getData().clear();
    for (int i = 0; i < currentStock.getPriceHistory().size(); i++) {
      series.getData().add(new XYChart.Data<>(i, currentStock.getPriceHistory().get(i)));
    }
  }

  public void updateView() {
    window.setStockName(currentStock.getName());
    window.setStockPrice(currentStock.getCurrentPrice());
  }

  public void onStockClick(Stock stock) {
    setCurrentStock(stock);
    updateChart();
    updateView();

  }

  public ArrayList<Stock> getStocks() {
    return exchange.getStocks();
  }

  @Override
  public void onExchangeUpdate(ArrayList<Stock> stocks) {
    updateChart();
  }
}
