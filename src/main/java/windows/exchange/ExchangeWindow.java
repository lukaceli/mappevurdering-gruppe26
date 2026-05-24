package windows.exchange;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import model.appstate.AppState;
import model.exchange.Exchange;
import model.exchange.ExchangeList;
import model.exchange.ExchangeObserver;
import model.player.Player;
import model.stock.Stock;
import model.stock.StockObserver;
import windows.main.MainWindow;

/**
 * JavaFX view for the stock exchange screen.
 * Displays a list of stocks, top movers, a price chart for the selected stock,
 * and controls for navigating exchanges, sorting, searching, and trading.
 * Implements {@link StockObserver} and {@link ExchangeObserver} to react to state changes.
 */
public class ExchangeWindow implements StockObserver, ExchangeObserver {

  private final StackPane root;
  private final ExchangeController controller;
  private final VBox stockListBox;
  private final Label stockNameLabel;
  private final Label stockPriceLabel;
  private final HashMap<String, Label> priceLabels;
  private final AppState appState;
  private final VBox gainersBox;
  private final VBox losersBox;
  private final Label allTimeHighLabel;
  private final Label allTimeLowLabel;
  private final TextField searchField;
  private final Label exchangeTitle;
  private final List<Stock> stocks;
  private LineChart<Number, Number> stockChart;
  private List<HBox> stockRows;
  private String currentSort = "Alphabetical";

  /**
   * Constructs an {@code ExchangeWindow} and initialises all UI components.
   *
   * @param exchanges    the list of available exchanges
   * @param appState     the shared application state
   * @param player       the current player
   * @param onAdvance    a callback to run when the week is advanced
   * @param onAfterTrade a callback to run after a trade is completed
   */
  public ExchangeWindow(ExchangeList exchanges, AppState appState, Player player,
                        Runnable onAdvance, Runnable onAfterTrade) {
    this.appState = appState;
    root = new StackPane();
    BorderPane borderPane = new BorderPane();
    borderPane.getStyleClass().add("exchange-page");
    controller = new ExchangeController(exchanges, appState, player);
    stockListBox = new VBox(20);
    this.priceLabels = new HashMap<>();

    searchField = new TextField();
    searchField.setPromptText("Enter stock name");
    searchField.setPrefColumnCount(10);
    searchField.textProperty().addListener((obs, oldValue, newValue) -> updateStockList());
    searchField.getStyleClass().add("form-input");

    stocks = appState.getSelectedExchange().getStocks();
    stockRows = createStockRow(stocks);
    stockListBox.getChildren().addAll(stockRows);
    stockListBox.getStyleClass().add("stock-list-box");
    stockListBox.setAlignment(Pos.CENTER);

    ScrollPane scrollPane = new ScrollPane(stockListBox);
    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    scrollPane.setMaxHeight((double) MainWindow.SCENE_HEIGHT / 2);
    scrollPane.setPrefWidth(500);
    scrollPane.setMaxWidth(500);
    scrollPane.setMinWidth(500);

    gainersBox = new VBox(10);
    gainersBox.getStyleClass().add("gainers-box");
    gainersBox.setPrefWidth(400);
    gainersBox.setMinWidth(400);
    gainersBox.setMaxWidth(400);

    losersBox = new VBox(10);
    losersBox.getStyleClass().add("losers-box");
    losersBox.setPrefWidth(400);
    losersBox.setMinWidth(400);
    losersBox.setMaxWidth(400);
    losersBox.setMaxWidth(VBox.USE_PREF_SIZE);
    updateGainers();
    updateLosers();

    HBox topMoversBox = new HBox(30);
    topMoversBox.getChildren().addAll(gainersBox, losersBox);

    Button leftBtn = new Button("<");
    Button rightBtn = new Button(">");
    leftBtn.setOnAction(e -> controller.previousExchange());
    rightBtn.setOnAction(e -> controller.nextExchange());
    leftBtn.getStyleClass().add("exchange-nav-btn");
    rightBtn.getStyleClass().add("exchange-nav-btn");

    Button advanceBtn = new Button("Advance week >");
    advanceBtn.getStyleClass().add("advance-button");
    advanceBtn.setOnAction(e -> onAdvance.run());

    ComboBox<String> sortBox = new ComboBox<>();
    sortBox.getItems().addAll("Alphabetical", "Price", "Biggest Gain");
    sortBox.setValue("Alphabetical");
    sortBox.setOnAction(e -> {
      currentSort = sortBox.getValue();
      updateStockList();
    });
    sortBox.getStyleClass().add("exchange-sort");

    HBox navigationBox = new HBox(10);
    navigationBox.getChildren().addAll(leftBtn, rightBtn, sortBox, searchField);

    exchangeTitle = new Label(appState.getSelectedExchange().getName());
    exchangeTitle.getStyleClass().add("exchange-title");

    Label nameHeader = new Label("Name");
    Label symbolHeader = new Label("Symbol");
    Label priceHeader = new Label("Price");
    nameHeader.getStyleClass().add("list-header");
    symbolHeader.getStyleClass().add("list-header");
    priceHeader.getStyleClass().add("list-header");

    HBox listHeader = new HBox(20);
    listHeader.getChildren().addAll(nameHeader, symbolHeader, priceHeader);

    VBox leftPanelBox = new VBox(20);
    leftPanelBox.getChildren().addAll(exchangeTitle, navigationBox, listHeader,
            scrollPane, topMoversBox, advanceBtn);
    leftPanelBox.getStyleClass().add("exchange-left-panel");

    Button btnBuy = new Button("Buy");
    btnBuy.setOnAction(e -> root.getChildren().add(controller.getBuyWindow(this, onAfterTrade)));
    btnBuy.getStyleClass().add("advance-button");

    HBox tradeButtonsBox = new HBox(20);
    tradeButtonsBox.setAlignment(Pos.CENTER);
    tradeButtonsBox.getChildren().add(btnBuy);

    stockNameLabel = new Label("No stock selected");
    stockPriceLabel = new Label("0");
    allTimeHighLabel = new Label(
            "All time high: " + appState.getSelectedStock().getHighestPrice());
    allTimeLowLabel = new Label(
            "All time low: " + appState.getSelectedStock().getLowestPrice());
    stockNameLabel.getStyleClass().add("stock-detail-name");
    stockPriceLabel.getStyleClass().add("stock-detail-price");
    allTimeHighLabel.getStyleClass().add("stock-detail-stat");
    allTimeLowLabel.getStyleClass().add("stock-detail-stat");

    VBox stockDetailBox = new VBox(20);
    stockDetailBox.getStyleClass().add("stock-detail-box");
    stockDetailBox.getChildren().addAll(stockNameLabel, stockPriceLabel,
            tradeButtonsBox, createStockChart(), allTimeHighLabel, allTimeLowLabel);

    borderPane.setRight(stockDetailBox);
    borderPane.setCenter(leftPanelBox);
    root.getChildren().add(borderPane);

    updateStockList();

    exchanges.getExchanges().getFirst().addStockObserver(this);
    appState.addExchangeObserver(this);
    appState.addStockObserver(this);
    for (Exchange exchange : exchanges.getExchanges()) {
      exchange.addStockObserver(this);
    }
    exchanges.addExchangeObserver(this);
  }

  /**
   * Returns the root pane of this window.
   *
   * @return the {@link StackPane} root
   */
  public StackPane getRoot() {
    return root;
  }

  /**
   * Creates and returns a {@link LineChart} for displaying stock price history.
   *
   * @return the configured stock chart
   */
  public LineChart<Number, Number> createStockChart() {
    NumberAxis xaxis = new NumberAxis();
    NumberAxis yaxis = new NumberAxis();
    xaxis.setLabel("Weeks");
    yaxis.setLabel("Stock Price");
    yaxis.setForceZeroInRange(false);
    yaxis.setAutoRanging(true);
    stockChart = new LineChart<>(xaxis, yaxis);
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
    stockChart.getData().add(appState.getSelectedStock().getCopiedSeries());
    stockPriceLabel.setText(String.valueOf(appState.getSelectedStock().getCurrentPrice()));
    stockNameLabel.setText(appState.getSelectedStock().getName());
    allTimeHighLabel.setText(
            "All time high: " + appState.getSelectedStock().getHighestPrice());
    allTimeLowLabel.setText(
            "All time low: " + appState.getSelectedStock().getLowestPrice());
    updateGainers();
    updateLosers();
    updateStockList();
  }

  @Override
  public void onExchangeUpdate() {
    exchangeTitle.setText(appState.getSelectedExchange().getName());
    updateStockList();
    updateGainers();
    updateLosers();
  }

  private List<HBox> createStockRow(List<Stock> stocks) {
    ArrayList<HBox> rows = new ArrayList<>();
    for (Stock stock : stocks) {
      Label name = new Label(stock.getName());
      Label symbol = new Label(stock.getSymbol());
      Label price = new Label(String.valueOf(stock.getCurrentPrice()));
      Label change = new Label(stock.getLatestPercentageChange().toPlainString() + "%");
      priceLabels.put(stock.getSymbol(), price);
      HBox row = new HBox(20, name, symbol, price, change);
      rows.add(row);
    }
    return rows;
  }

  private void updateRowColors(List<Stock> sorted) {
    for (int i = 0; i < Math.min(stockRows.size(), sorted.size()); i++) {
      Stock stock = sorted.get(i);
      HBox row = stockRows.get(i);
      row.getStyleClass().removeAll("stock-row-positive", "stock-row-negative",
              "stock-row-neutral");
      if (stock.getLatestPercentageChange().compareTo(BigDecimal.ZERO) < 0) {
        row.getStyleClass().add("stock-row-negative");
      } else if (stock.getLatestPercentageChange().compareTo(BigDecimal.ZERO) > 0) {
        row.getStyleClass().add("stock-row-positive");
      } else {
        row.getStyleClass().add("stock-row-neutral");
      }
    }
  }

  private void updateStockList() {
    String search = searchField.getText().toLowerCase();
    List<Stock> sorted = controller.getFilteredAndSortedStocks(search, currentSort);
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
    updateRowColors(sorted);
  }

  private void updateGainers() {
    gainersBox.getChildren().clear();
    Label title = new Label("Top Gainers");
    title.setFont(Font.font("Arial", FontWeight.BOLD, 16));
    gainersBox.getChildren().add(title);
    List<Stock> gainers = controller.getTopGainers();
    if (gainers.isEmpty()) {
      gainersBox.getChildren().add(new Label("No gainers right now"));
      return;
    }
    for (Stock stock : gainers) {
      Label name = new Label(stock.getName());
      Label change = new Label("+" + stock.getLatestPercentageChange().toPlainString() + "%");
      change.getStyleClass().add("gainer-change");
      HBox row = new HBox(20, name, change);
      row.getStyleClass().add("gainer-row");
      gainersBox.getChildren().add(row);
    }
  }

  private void updateLosers() {
    losersBox.getChildren().clear();
    Label title = new Label("Top Losers");
    title.setFont(Font.font("Arial", FontWeight.BOLD, 16));
    losersBox.getChildren().add(title);
    List<Stock> losers = controller.getTopLosers();
    if (losers.isEmpty()) {
      losersBox.getChildren().add(new Label("No losers right now"));
      return;
    }
    for (Stock stock : losers) {
      Label name = new Label(stock.getName());
      Label change = new Label(stock.getLatestPercentageChange().toPlainString() + "%");
      change.getStyleClass().add("loser-change");
      HBox row = new HBox(20, name, change);
      row.getStyleClass().add("loser-row");
      losersBox.getChildren().add(row);
    }
  }
}