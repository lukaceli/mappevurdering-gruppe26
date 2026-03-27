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
  private final XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();

  public ExchangeController(Exchange exchange) {
    this.exchange = exchange;
    currentStock = exchange.getStocks().getFirst();
    exchange.addObserver(this);
  }

  public VBox getStockNames() {
    VBox names = new VBox(20);
    for (String name : exchange.getStockNames()) {
      System.out.println(name);
      Label nameLabel = new Label(name);
      names.getChildren().add(nameLabel);
    }
    return names;
  }

  public ArrayList<HBox> createStockRow() {
    ArrayList<HBox> stockRow = new ArrayList<>();
    for (Stock stock : exchange.getStocks()) {

      Label name = new Label(stock.getName());
      Label symbol = new Label(stock.getSymbol());
      Label price = new Label(String.valueOf(stock.getCurrentPrice()));
      priceLabels.add(price);
      HBox row = new HBox(20, name, symbol, price);

      row.setStyle("""
        -fx-border-color: black;
        -fx-border-width: 1;
        -fx-padding: 10;
      """);

      stockRow.add(row);
    }
    return stockRow;
  }

  public void setCurrentStock(Stock currentStock) {
    this.currentStock = currentStock;
  }

  public void updatePrices() {
    for (int i = 0; i < exchange.getStocks().size(); i++) {
      Stock stock = exchange.getStocks().get(i);
      priceLabels.get(i).setText(String.valueOf(stock.getCurrentPrice()));
    }
  }

  public Stock getCurrentStock() {
    return currentStock;
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

  @Override
  public void onExchangeUpdate() {
    updatePrices();
    updateChart();
  }
}
