package windows;

import javafx.geometry.Pos;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import model.appState.AppState;
import model.exchange.Exchange;
import model.exchange.ExchangeList;
import model.exchange.ExchangeObserver;
import model.exchange.StockObserver;
import model.stock.Stock;

import java.util.ArrayList;
import java.util.HashMap;

public class ExchangeWindow implements StockObserver, ExchangeObserver {
  private BorderPane borderPane;
  private StackPane root;
  private ExchangeController controller;
  private VBox stocksBox;
  private final Label stockNameLabel;
  private final Label stockPriceLabel;
  private final HashMap<String, Label> priceLabels;
  private LineChart stockChart;
  private ExchangeList exchangeList;
  private AppState appState;
  private ArrayList<HBox> stockBoxes;

  public ExchangeWindow(ExchangeList exchanges,  AppState appState) {
    this.appState = appState;
    root = new StackPane();
    exchangeList = exchanges;

    borderPane = new BorderPane();

    controller = new ExchangeController(exchanges, this, appState);
    stocksBox = new VBox(20);
    this.priceLabels = new HashMap<>();
    ArrayList<Stock> stocks = appState.getSelectedExchange().getStocks();

    stockBoxes = createStockRow(stocks);
    for (int i = 0; i < stockBoxes.size(); i++) {
      HBox row = stockBoxes.get(i);
      Stock stock = stocks.get(i);
      row.setCursor(javafx.scene.Cursor.HAND);

      row.setOnMouseClicked(e -> {
        appState.setSelectedStock(stock);
      });
    }

      stocksBox.getChildren().addAll(stockBoxes);
      stocksBox.setStyle("-fx-background-color: #878c8b; -fx-padding: 20; -fx-background-radius: 10;");
      stocksBox.setAlignment(Pos.CENTER);
      stocksBox.setMaxWidth(VBox.USE_PREF_SIZE);

      ScrollPane scrollPane = new ScrollPane(stocksBox);
      scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
      scrollPane.setMaxWidth(VBox.USE_PREF_SIZE);
      scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
      scrollPane.setMaxHeight((double) MainWindow.sceneHeight / 2);
      VBox exchangeBox = new VBox(20);
      HBox arrows = new HBox(10);
      Button leftBtn = new Button("←");
      Button rightBtn = new Button("→");

      leftBtn.setOnAction(e -> {
        controller.previousExchange();
      });
      rightBtn.setOnAction(e -> {
        controller.nextExchange();
      });
      arrows.getChildren().addAll(leftBtn, rightBtn);
      exchangeBox.getChildren().addAll(arrows, scrollPane);



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
    borderPane.setCenter(exchangeBox);
    root.getChildren().add(borderPane);

    exchangeList.getExchanges().getFirst().addStockObserver(this);
    appState.addExchangeObserver(this);
    appState.addStockObserver(this);
    for (Exchange exchange : exchanges.getExchanges()) {
      exchange.addStockObserver(this);
    }
    exchangeList.addExchangeObserver(this);

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


  public StackPane getRoot() {
    return root;
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
  public void onStockUpdate() {
    System.out.println("upddate");
    //handels selceted stock

    //handels Stock list
    for (Stock stock : exchangeList.getExchanges().getFirst().getStocks()) {
      Label label = priceLabels.get(stock.getSymbol());
      if (label != null) {
        label.setText(stock.getCurrentPrice().toString());
      }
    }

    stockChart.getData().clear();
    stockChart.getData().add(appState.getSelectedStock().getSeries());
    stockPriceLabel.setText(String.valueOf(appState.getSelectedStock().getCurrentPrice()));
    stockNameLabel.setText(appState.getSelectedStock().getName());
  }

  @Override
  public void onExchangeUpdate() {
    System.out.println("VICTOR");
    ArrayList<Stock> stocks = appState.getSelectedExchange().getStocks();
    stockBoxes = createStockRow(stocks);

    for (int i = 0; i < stockBoxes.size(); i++) {
      HBox row = stockBoxes.get(i);
      Stock stock = stocks.get(i);
      row.setCursor(javafx.scene.Cursor.HAND);
      row.setOnMouseClicked(e -> appState.setSelectedStock(stock));
    }

    stocksBox.getChildren().clear();
    stocksBox.getChildren().addAll(stockBoxes);
  }
}
