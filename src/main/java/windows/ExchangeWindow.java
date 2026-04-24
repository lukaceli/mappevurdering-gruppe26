package windows;

import javafx.geometry.Pos;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import model.exchange.Exchange;
import model.exchange.ExchangeObserver;
import model.stock.Stock;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

public class ExchangeWindow implements ExchangeObserver {
  private BorderPane borderPane;
  private StackPane root;
  private ExchangeController controller;
  private VBox stocksBox;
  private final Label stockNameLabel;
  private final Label stockPriceLabel;
  private final HashMap<String, Label> priceLabels;
  private LineChart stockChart;

  public ExchangeWindow(Exchange exchange) {
    root = new StackPane();
    exchange.addObserver(this);
    borderPane = new BorderPane();

    controller = new ExchangeController(exchange, this);
    stocksBox = new VBox(20);
    this.priceLabels = new HashMap<>();
    ArrayList<Stock> stocks = controller.getStocks();

    ArrayList<HBox> rows = createStockRow(stocks);
    for (int i = 0; i < rows.size(); i++) {
      HBox row = rows.get(i);
      Stock stock = stocks.get(i);
      row.setCursor(javafx.scene.Cursor.HAND);

      row.setOnMouseClicked(e -> {
        controller.onStockClick(stock);
      });
    }
      stocksBox.getChildren().addAll(rows);
      stocksBox.setStyle("-fx-background-color: #878c8b; -fx-padding: 20; -fx-background-radius: 10;");
      stocksBox.setAlignment(Pos.CENTER);
      stocksBox.setMaxWidth(VBox.USE_PREF_SIZE);

      ScrollPane scrollPane = new ScrollPane(stocksBox);
      scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
      scrollPane.setMaxWidth(VBox.USE_PREF_SIZE);
      scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
      scrollPane.setMaxHeight((double) MainWindow.sceneHeight / 2);


    VBox stockPopUp = new VBox(20);
    Button btnBuy = new Button("Buy");
    btnBuy.setOnAction(e -> {
      root.getChildren().add(controller.getBuyWindow(this));
    });
    Button btnSell = new Button("Sell");
    btnSell.setOnAction(e -> {
      root.getChildren().add(controller.getSellWindow(this));
    });
    HBox btnBock = new HBox(20);
    btnBock.setAlignment(Pos.CENTER);
    btnBock.getChildren().addAll(btnBuy, btnSell);


    stockPopUp.setStyle("-fx-background-color: #50d3b8; -fx-padding: 20; -fx-background-radius: 10;");

    stockNameLabel = new Label("No stock selected");
    stockPriceLabel = new Label("0");
    stockNameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
    stockPriceLabel.setFont(Font.font(20));

    stockPopUp.getChildren().addAll(stockNameLabel, stockPriceLabel, btnBock, createStockChart());
    borderPane.setRight(stockPopUp);
    borderPane.setCenter(scrollPane);
    root.getChildren().add(borderPane);


  }

  private ArrayList<HBox> createStockRow(ArrayList<Stock> stocks) {
    ArrayList<HBox> stockRow = new ArrayList<>();
    for (Stock stock : stocks) {

      Label name = new Label(stock.getName());
      Label symbol = new Label(stock.getSymbol());
      Label price = new Label(String.valueOf(stock.getCurrentPrice()));
      priceLabels.put(stock.getSymbol(), price);
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


  public StackPane root() {
    return root;
  }

  public void setStockName(String name) {
    stockNameLabel.setText(name);
  }
  public void setStockPrice(BigDecimal price) {
    stockPriceLabel.setText(String.valueOf(price));
  }

  public void setStockSeries(Series series) {
    stockChart.getData().clear();
    stockChart.getData().add(series);
  }

  public LineChart<Number, Number> createStockChart() {
    NumberAxis xAxis = new NumberAxis();
    NumberAxis yAxis = new NumberAxis();
    xAxis.setLabel("Weeks");
    yAxis.setLabel("Stock Price");
    yAxis.setForceZeroInRange(false);
    yAxis.setAutoRanging(true);
    stockChart = new LineChart<>(xAxis, yAxis);
    stockChart.setAnimated(false);
    stockChart.setTitle("Price history");
    stockChart.setLegendVisible(false);
    return stockChart;
  }




  @Override
  public void onExchangeUpdate(ArrayList<Stock> stocks) {
    //handels selceted stock
    stockPriceLabel.setText(String.valueOf(controller.getCurrentStock().getCurrentPrice()));

    //handels Stock list
    for (Stock stock : stocks) {
      Label label = priceLabels.get(stock.getSymbol());
      if (label != null) {
        label.setText(stock.getCurrentPrice().toString());
      }
    }

  }

}
