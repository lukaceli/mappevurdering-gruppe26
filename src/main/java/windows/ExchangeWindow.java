package windows;

import javafx.geometry.Pos;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.*;
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
import model.player.Player;
import model.stock.Stock;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

public class ExchangeWindow implements StockObserver, ExchangeObserver {
  private BorderPane borderPane;
  private StackPane root;
  private ExchangeController controller;
  private VBox stockListBox;
  private final Label stockNameLabel;
  private final Label stockPriceLabel;
  private final HashMap<String, Label> priceLabels;
  private LineChart stockChart;
  private ExchangeList exchangeList;
  private AppState appState;
  private ArrayList<HBox> stockRows;
  private VBox gainersBox;
  private VBox losersBox;
  private Label allTimeHighLabel;
  private Label allTimeLowLabel;
  private String currentSort = "Alfabetisk";
  private TextField searchField;
  private Label exchangeTitle;

  public ExchangeWindow(ExchangeList exchanges, AppState appState, Player player) {
    this.appState = appState;
    root = new StackPane();
    exchangeList = exchanges;

    borderPane = new BorderPane();

    controller = new ExchangeController(exchanges, appState, player);
    stockListBox = new VBox(20);
    this.priceLabels = new HashMap<>();

    searchField = new TextField();
    searchField.setPromptText("Enter stock name");
    searchField.setPrefColumnCount(10);
    searchField.textProperty().addListener((obs, oldValue, newValue) -> updateStockList());

    ArrayList<Stock> stocks = appState.getSelectedExchange().getStocks();
    stockRows = createStockRow(stocks);

    stockListBox.getChildren().addAll(stockRows);
    stockListBox.setStyle("-fx-background-color: #878c8b; -fx-padding: 20; -fx-background-radius: 10;");
    stockListBox.setAlignment(Pos.CENTER);
    stockListBox.setMaxWidth(VBox.USE_PREF_SIZE);

    ScrollPane scrollPane = new ScrollPane(stockListBox);
    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    scrollPane.setMaxWidth(VBox.USE_PREF_SIZE);
    scrollPane.setMaxHeight((double) MainWindow.sceneHeight / 2);

    gainersBox = new VBox(10);
    gainersBox.setStyle("-fx-background-color: #5cb85c; -fx-padding: 20; -fx-background-radius: 10;");
    gainersBox.setMaxWidth(VBox.USE_PREF_SIZE);
    losersBox = new VBox(10);
    losersBox.setStyle("-fx-background-color: #d32424; -fx-padding: 20; -fx-background-radius: 10;");
    losersBox.setMaxWidth(VBox.USE_PREF_SIZE);
    updateGainers();
    updateLosers();

    HBox topMoversBox = new HBox(30);
    topMoversBox.getChildren().addAll(gainersBox, losersBox);

    HBox navigationBox = new HBox(10);
    Button leftBtn = new Button("←");
    Button rightBtn = new Button("→");
    leftBtn.setOnAction(e -> controller.previousExchange());
    rightBtn.setOnAction(e -> controller.nextExchange());

    ComboBox<String> sortBox = new ComboBox<>();
    sortBox.getItems().addAll("Alfabetisk", "Pris", "Størst økning");
    sortBox.setValue("Alfabetisk");
    sortBox.setOnAction(e -> {
      currentSort = sortBox.getValue();
      updateStockList();
    });
    navigationBox.getChildren().addAll(leftBtn, rightBtn, sortBox, searchField);

    exchangeTitle = new Label(appState.getSelectedExchange().getName());
    exchangeTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));

    HBox listHeader = new HBox(20);
    Label nameHeader = new Label("Name");
    Label symbolHeader = new Label("Symbol");
    Label priceHeader = new Label("Price");
    nameHeader.setFont(Font.font("Arial", FontWeight.BOLD, 14));
    symbolHeader.setFont(Font.font("Arial", FontWeight.BOLD, 14));
    priceHeader.setFont(Font.font("Arial", FontWeight.BOLD, 14));
    listHeader.getChildren().addAll(nameHeader, symbolHeader, priceHeader);

    VBox leftPanelBox = new VBox(20);
    leftPanelBox.getChildren().addAll(exchangeTitle, navigationBox, listHeader, scrollPane, topMoversBox);

    VBox stockDetailBox = new VBox(20);
    stockDetailBox.setStyle("-fx-background-color: #50d3b8; -fx-padding: 20; -fx-background-radius: 10;");

    Button btnBuy = new Button("Buy");
    btnBuy.setOnAction(e -> root.getChildren().add(controller.getBuyWindow(this)));
    Button btnSell = new Button("Sell");
    btnSell.setOnAction(e -> root.getChildren().add(controller.getSellWindow(this)));
    HBox tradeButtonsBox = new HBox(20);
    tradeButtonsBox.setAlignment(Pos.CENTER);
    tradeButtonsBox.getChildren().addAll(btnBuy, btnSell);

    stockNameLabel = new Label("No stock selected");
    stockPriceLabel = new Label("0");
    stockNameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
    stockPriceLabel.setFont(Font.font(20));
    allTimeHighLabel = new Label("All time high: " + appState.getSelectedStock().getHighestPrice());
    allTimeLowLabel = new Label("All time low: " + appState.getSelectedStock().getLowestPrice());
    allTimeHighLabel.setFont(Font.font(20));
    allTimeLowLabel.setFont(Font.font(20));

    stockDetailBox.getChildren().addAll(stockNameLabel, stockPriceLabel, tradeButtonsBox, createStockChart(), allTimeHighLabel, allTimeLowLabel);

    borderPane.setRight(stockDetailBox);
    borderPane.setCenter(leftPanelBox);
    root.getChildren().add(borderPane);

    updateStockList();

    exchangeList.getExchanges().getFirst().addStockObserver(this);
    appState.addExchangeObserver(this);
    appState.addStockObserver(this);
    for (Exchange exchange : exchanges.getExchanges()) {
      exchange.addStockObserver(this);
    }
    exchangeList.addExchangeObserver(this);
  }

  private ArrayList<HBox> createStockRow(ArrayList<Stock> stocks) {
    ArrayList<HBox> rows = new ArrayList<>();
    for (Stock stock : stocks) {
      Label name = new Label(stock.getName());
      Label symbol = new Label(stock.getSymbol());
      Label price = new Label(String.valueOf(stock.getCurrentPrice()));
      priceLabels.put(stock.getSymbol(), price);
      HBox row = new HBox(20, name, symbol, price);
      rows.add(row);
      row.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-padding: 10;");
    }
    return rows;
  }

  private void updateRowColors() {
    ArrayList<Stock> stocks = appState.getSelectedExchange().getStocks();
    for (int i = 0; i < stockRows.size(); i++) {
      Stock stock = stocks.get(i);
      HBox row = stockRows.get(i);
      if (stock.getLatestPercentageChange().compareTo(BigDecimal.ZERO) < 0) {
        row.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-padding: 10; -fx-background-color: #c63434;");
      } else if (stock.getLatestPercentageChange().compareTo(BigDecimal.ZERO) > 0) {
        row.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-padding: 10; -fx-background-color: #58c358;");
      }
    }
  }

  private void updateStockList() {
    String search = searchField.getText().toLowerCase();
    ArrayList<Stock> sorted = controller.getFilteredAndSortedStocks(search, currentSort);
    stockRows = createStockRow(sorted);
    for (int i = 0; i < stockRows.size(); i++) {
      HBox row = stockRows.get(i);
      Stock stock = sorted.get(i);
      row.setCursor(javafx.scene.Cursor.HAND);
      row.setOnMouseClicked(e -> appState.setSelectedStock(stock));
    }
    stockListBox.getChildren().clear();
    if (stockRows.isEmpty()) {
      Label noResults = new Label("No stocks found");
      noResults.setFont(Font.font(16));
      stockListBox.getChildren().add(noResults);
    } else {
      stockListBox.getChildren().setAll(stockRows);
    }
  }

  private void updateGainers() {
    gainersBox.getChildren().clear();
    Label title = new Label("Top Gainers");
    title.setFont(Font.font("Arial", FontWeight.BOLD, 16));
    gainersBox.getChildren().add(title);

    ArrayList<Stock> gainers = controller.getTopGainers();
    if (gainers.isEmpty()) {
      gainersBox.getChildren().add(new Label("No gainers right now"));
      return;
    }
    for (Stock stock : gainers) {
      Label name = new Label(stock.getName());
      Label change = new Label("+" + stock.getLatestPercentageChange().toPlainString() + "%");
      change.setStyle("-fx-text-fill: darkgreen; -fx-font-weight: bold;");
      HBox row = new HBox(20, name, change);
      row.setStyle("""
        -fx-border-color: darkgreen;
        -fx-border-width: 1;
        -fx-padding: 8;
      """);
      gainersBox.getChildren().add(row);
    }
  }

  private void updateLosers() {
    losersBox.getChildren().clear();
    Label title = new Label("Top Losers");
    title.setFont(Font.font("Arial", FontWeight.BOLD, 16));
    losersBox.getChildren().add(title);

    ArrayList<Stock> losers = controller.getTopLosers();
    if (losers.isEmpty()) {
      losersBox.getChildren().add(new Label("No losers right now"));
      return;
    }
    for (Stock stock : losers) {
      Label name = new Label(stock.getName());
      Label change = new Label(stock.getLatestPercentageChange().toPlainString() + "%");
      change.setStyle("-fx-text-fill: #610000; -fx-font-weight: bold;");
      HBox row = new HBox(20, name, change);
      row.setStyle("""
        -fx-border-color: #c32525;
        -fx-border-width: 1;
        -fx-padding: 8;
      """);
      losersBox.getChildren().add(row);
    }
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
    for (Stock stock : appState.getSelectedExchange().getStocks()) {
      Label label = priceLabels.get(stock.getSymbol());
      if (label != null) {
        label.setText(stock.getCurrentPrice().toString());
      }
    }

    stockChart.getData().clear();
    stockChart.getData().add(appState.getSelectedStock().getSeries());
    stockPriceLabel.setText(String.valueOf(appState.getSelectedStock().getCurrentPrice()));
    stockNameLabel.setText(appState.getSelectedStock().getName());
    allTimeHighLabel.setText("All time high: " + appState.getSelectedStock().getHighestPrice());
    allTimeLowLabel.setText("All time low: " + appState.getSelectedStock().getLowestPrice());

    updateGainers();
    updateLosers();
    updateRowColors();

  }

  @Override
  public void onExchangeUpdate() {
    exchangeTitle.setText(appState.getSelectedExchange().getName());
    updateStockList();
    updateGainers();
    updateLosers();
  }
}