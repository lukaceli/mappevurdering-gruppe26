package windows.profile;

import java.math.BigDecimal;
import java.math.RoundingMode;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import model.exchange.Exchange;
import model.player.Player;
import model.stock.Share;
import model.transaction.Transaction;
import windows.trade.sell.SellWindow;

/**
 * JavaFX view for the player profile screen.
 * Displays the player's portfolio, transaction history, and key financial statistics.
 * Supports selling individual shares and selling all shares at once.
 */
public class ProfileWindow {

  private final Player player;
  private final Exchange exchange;
  private final ProfileController controller;
  private final ObservableList<Share> sharesList;
  private final ObservableList<Transaction> transactionList;
  private final Runnable onBalanceUpdate;
  private final BorderPane root;
  private final StackPane stackRoot;
  private Label balanceLabel;
  private Label totalPercentageChange;
  private Label totalValueChange;
  private Label totalAccountValue;
  private Label balanceValue;
  private Label netWorthValue;
  private Label yieldValuePercentage;
  private Label weekNumber;
  private Label heroName;
  private Label badge;
  private Label totalValueChangeValue;

  /**
   * Constructs a {@code ProfileWindow} for the given player and exchange.
   *
   * @param player          the current player
   * @param exchange        the exchange used for sell transactions
   * @param onBalanceUpdate a callback to run when the player's balance changes
   */
  public ProfileWindow(Player player, Exchange exchange, Runnable onBalanceUpdate) {
    this.root = new BorderPane();
    this.player = player;
    this.exchange = exchange;
    this.controller = new ProfileController(player, exchange);
    this.sharesList = FXCollections.observableArrayList(controller.getShares());
    this.transactionList = FXCollections.observableArrayList(controller.getAllTransactions());
    this.onBalanceUpdate = onBalanceUpdate;

    VBox top = new VBox(buildHero(), buildInfoCards());
    root.setTop(top);
    root.setCenter(buildTabPane());
    this.stackRoot = new StackPane(root);
  }

  /**
   * Refreshes all displayed data to reflect the current player state.
   */
  public void refreshData() {
    sharesList.setAll(controller.getShares());
    transactionList.setAll(controller.getAllTransactions());
    totalPercentageChange.setText(controller.totalOverallPercentageChange() + " %");
    totalValueChange.setText(controller.totalOverallValueChange() + " $");
    balanceValue.setText(player.getBalance() + "$");
    netWorthValue.setText(player.getNetWorth() + "$");
    weekNumber.setText(String.valueOf(exchange.getWeek()));
    badge.setText("★ " + player.getStatus());

    BigDecimal yield = controller.totalOverallPercentageChange();
    yieldValuePercentage.setText(yield + "%");
    if (yield.compareTo(BigDecimal.ZERO) < 0) {
      yieldValuePercentage.setStyle(
              "-fx-text-fill: #dc2626; -fx-font-weight: bold; -fx-font-size: 20px;");
    } else {
      yieldValuePercentage.setStyle(
              "-fx-text-fill: #16a34a; -fx-font-weight: bold; -fx-font-size: 20px;");
    }

    BigDecimal valueChange = controller.totalOverallValueChange();
    totalValueChangeValue.setText(valueChange + "$");
    if (valueChange.compareTo(BigDecimal.ZERO) < 0) {
      totalValueChangeValue.setStyle(
              "-fx-text-fill: #dc2626; -fx-font-weight: bold; -fx-font-size: 20px;");
    } else {
      totalValueChangeValue.setStyle(
              "-fx-text-fill: #16a34a; -fx-font-weight: bold; -fx-font-size: 20px;");
    }
  }

  /**
   * Returns the root pane of this window.
   *
   * @return the {@link StackPane} root
   */
  public StackPane getRoot() {
    return stackRoot;
  }

  /**
   * Builds the hero section displaying the player's avatar, name and status badge.
   *
   * @return the hero {@link HBox}
   */
  public HBox buildHero() {
    Label avatar = new Label(player.getName().substring(0, 1).toUpperCase());
    heroName = new Label(player.getName());
    badge = new Label("★ " + player.getStatus());
    VBox nameBox = new VBox(5, heroName, badge);
    HBox hero = new HBox(20, avatar, nameBox);

    avatar.getStyleClass().add("avatar");
    heroName.getStyleClass().add("hero-name");
    badge.getStyleClass().add("hero-badge");
    hero.getStyleClass().add("hero-section");

    return hero;
  }

  /**
   * Builds the info cards row showing balance, net worth, yield, value change, and week.
   *
   * @return the info cards {@link HBox}
   */
  public HBox buildInfoCards() {
    balanceValue = new Label(player.getBalance() + "$");
    Label balanceTitle = new Label("BALANCE");
    balanceTitle.getStyleClass().add("info-title");
    balanceValue.getStyleClass().add("info-value-green");
    VBox balanceCard = new VBox(5);
    balanceCard.getStyleClass().add("info-card");
    balanceCard.getChildren().addAll(balanceTitle, balanceValue);

    Label netWorthTitle = new Label("NET WORTH");
    netWorthValue = new Label(player.getNetWorth() + "$");
    netWorthTitle.getStyleClass().add("info-title");
    netWorthValue.getStyleClass().add("info-value-green");
    VBox netWorthCard = new VBox(5);
    netWorthCard.getStyleClass().add("info-card");
    netWorthCard.getChildren().addAll(netWorthTitle, netWorthValue);

    VBox yieldCard = new VBox(5);
    Label yieldTitle = new Label("YIELD");
    yieldValuePercentage = new Label(controller.totalOverallPercentageChange() + "%");
    yieldTitle.getStyleClass().add("info-title");
    yieldCard.getStyleClass().add("info-card");
    yieldCard.getChildren().addAll(yieldTitle, yieldValuePercentage);

    Label valueChangeTitle = new Label("VALUE CHANGE");
    totalValueChangeValue = new Label(controller.totalOverallValueChange() + "$");
    valueChangeTitle.getStyleClass().add("info-title");
    totalValueChangeValue.getStyleClass().add("info-value-green");
    VBox valueChangeCard = new VBox(5);
    valueChangeCard.getStyleClass().add("info-card");
    valueChangeCard.getChildren().addAll(valueChangeTitle, totalValueChangeValue);

    Label weekTitle = new Label("WEEK");
    weekNumber = new Label(String.valueOf(exchange.getWeek()));
    weekTitle.getStyleClass().add("info-title");
    weekNumber.getStyleClass().add("info-value-green");
    VBox weekCard = new VBox(5);
    weekCard.getStyleClass().add("info-card");
    weekCard.getChildren().addAll(weekTitle, weekNumber);

    HBox cards = new HBox(20, balanceCard, netWorthCard, yieldCard, valueChangeCard, weekCard);
    cards.getStyleClass().add("info-cards");
    return cards;
  }

  private HBox buildHeader() {
    Label playerName = new Label("Player name: " + player.getName());
    balanceLabel = new Label("Available balance: " + player.getBalance());
    Label status = new Label("Player status: " + player.getStatus());
    totalAccountValue = new Label("Total net worth: " + controller.totalAccountValue());
    return new HBox(50, playerName, balanceLabel, totalAccountValue, status);
  }

  private TabPane buildTabPane() {
    Tab portfolio = new Tab("Portfolio", buildPortfolioTab());
    Tab transactions = new Tab("Transactions", buildTransactionsTab());
    portfolio.setClosable(false);
    transactions.setClosable(false);
    TabPane profileTabs = new TabPane();
    profileTabs.getTabs().addAll(portfolio, transactions);
    return profileTabs;
  }

  private BorderPane buildPortfolioTab() {

    Label totalPercentageChangeLabel = new Label("Total yield:");
    totalPercentageChange = new Label(
            String.valueOf(controller.totalOverallPercentageChange()));
    Label valueChangeLabel = new Label("Total value change:");
    totalValueChange = new Label(String.valueOf(controller.totalOverallValueChange()));
    HBox totalStats = new HBox(10, totalPercentageChangeLabel, totalPercentageChange,
            valueChangeLabel, totalValueChange);

    TableColumn<Share, String> shareCol = new TableColumn<>("Stock Name");
    shareCol.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().stock().getName()));

    TableColumn<Share, String> quantityCol = new TableColumn<>("Quantity");
    quantityCol.setCellValueFactory(cellData ->
            new SimpleStringProperty(String.valueOf(cellData.getValue().quantity())));

    TableColumn<Share, String> purchasePriceCol = new TableColumn<>("Purchase Price");
    purchasePriceCol.setCellValueFactory(cellData ->
            new SimpleStringProperty(String.valueOf(cellData.getValue().purchasePrice())));

    TableColumn<Share, String> currentPriceCol = new TableColumn<>("Current Price");
    currentPriceCol.setCellValueFactory(cellData ->
            new SimpleStringProperty(
                    String.valueOf(cellData.getValue().stock().getCurrentPrice())));

    // AI was used to assist with coloring the percentage change cell.
    TableColumn<Share, String> percentageChangeCol = new TableColumn<>("% Change");
    percentageChangeCol.setCellValueFactory(cellData ->
            new SimpleStringProperty(
                    controller.percentageChangePerShare(cellData.getValue()) + " %"));
    percentageChangeCol.setCellFactory(col -> new TableCell<>() {
      @Override
      protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
          setText(null);
          setStyle("");
        } else {
          setText(item);
          if (item.startsWith("-")) {
            setStyle("-fx-text-fill: #dc2626; -fx-font-weight: bold;");
          } else {
            setStyle("-fx-text-fill: #16a34a; -fx-font-weight: bold;");
          }
        }
      }
    });

    // AI was used to assist with coloring the value change cell.
    TableColumn<Share, String> valueChangeCol = new TableColumn<>("Value Change");
    valueChangeCol.setCellValueFactory(cellData ->
            new SimpleStringProperty(
                    controller.valueChangePerShare(cellData.getValue()) + " $"));
    valueChangeCol.setCellFactory(col -> new TableCell<>() {
      @Override
      protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
          setText(null);
          setStyle("");
        } else {
          setText(item);
          if (item.startsWith("-")) {
            setStyle("-fx-text-fill: #dc2626; -fx-font-weight: bold;");
          } else {
            setStyle("-fx-text-fill: #16a34a; -fx-font-weight: bold;");
          }
        }
      }
    });

    TableColumn<Share, String> totalShareValCol = new TableColumn<>("Total Share Value");
    totalShareValCol.setCellValueFactory(cellData ->
            new SimpleStringProperty(
                    controller.totalShareValue(cellData.getValue()) + " $"));

    TableView<Share> shares = new TableView<>();
    shares.getColumns().addAll(shareCol, quantityCol, purchasePriceCol, currentPriceCol,
            percentageChangeCol, valueChangeCol, totalShareValCol);

    Button sellBtn = new Button("Sell");
    sellBtn.setDisable(true);
    sellBtn.getStyleClass().add("sell-button");
    sellBtn.setOnAction(e -> {
      Share selected = shares.getSelectionModel().getSelectedItem();
      if (selected == null) {
        return;
      }
      SellWindow sellWindow = new SellWindow(player);
      VBox popup = sellWindow.createFromPortfolio(selected, stackRoot, exchange, () -> {
        onBalanceUpdate.run();
        refreshData();
      });
      stackRoot.getChildren().add(popup);
    });

    shares.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) ->
            sellBtn.setDisable(newValue == null));

    shares.setItems(sharesList);
    shares.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    shares.setMaxWidth(Double.MAX_VALUE);
    BorderPane portfolio = new BorderPane();
    portfolio.setCenter(shares);

    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);

    Button sellAllBtn = new Button("Sell all");
    sellAllBtn.getStyleClass().add("sell-all-button");
    sellAllBtn.setOnAction(e -> {
      if (controller.getShares().isEmpty()) {
        return;
      }
      BigDecimal proceeds = controller.totalSellAllProceeds()
              .setScale(2, RoundingMode.HALF_UP);

      Label title = new Label("Sell All Shares");
      title.getStyleClass().add("trade-title");

      Label info = new Label("You will receive: $" + proceeds);
      info.getStyleClass().add("trade-stats");

      Label question = new Label("Are you sure?");
      question.getStyleClass().add("trade-label");

      Button confirmBtn = new Button("Confirm");
      confirmBtn.getStyleClass().add("sell-button");

      Button cancelBtn = new Button("Cancel");
      cancelBtn.getStyleClass().add("trade-close");

      HBox buttons = new HBox(15, confirmBtn, cancelBtn);
      buttons.setAlignment(Pos.CENTER);

      VBox popup = new VBox(15, title, info, question, buttons);
      popup.setAlignment(Pos.CENTER);
      popup.setPadding(new Insets(30));
      popup.setMaxSize(400, 220);
      popup.getStyleClass().add("trade-popup");

      confirmBtn.setOnAction(ev -> {
        controller.sellAll();
        onBalanceUpdate.run();
        refreshData();
        stackRoot.getChildren().remove(popup);
      });
      cancelBtn.setOnAction(ev -> stackRoot.getChildren().remove(popup));

      stackRoot.getChildren().add(popup);
    });

    HBox footer = new HBox(10, sellBtn, spacer, sellAllBtn);
    footer.getStyleClass().add("portfolio-footer");
    portfolio.setBottom(footer);

    return portfolio;
  }

  private BorderPane buildTransactionsTab() {
    TextField searchBar = new TextField();
    ComboBox<String> filter = new ComboBox<>();
    filter.getItems().addAll("All", "Purchase", "Sale");
    filter.setValue("All");

    FilteredList<Transaction> filteredList = new FilteredList<>(transactionList, t -> true);

    Runnable updateList = () -> {
      String search = searchBar.getText().toLowerCase();
      String type = filter.getValue();
      filteredList.setPredicate(t -> {
        boolean matchesSearch = t.getShare().stock().getName().toLowerCase().contains(search);
        boolean matchesType = type.equals("All")
                || controller.getTransactionType(t).equals(type);
        return matchesSearch && matchesType;
      });
    };

    searchBar.textProperty().addListener((obs, oldValue, newValue) -> updateList.run());
    filter.valueProperty().addListener((obs, oldValue, newValue) -> updateList.run());

    TableColumn<Transaction, String> weekCol = new TableColumn<>("Week");
    weekCol.setCellValueFactory(cellData ->
            new SimpleStringProperty(String.valueOf(cellData.getValue().getWeek())));

    TableColumn<Transaction, String> stockCol = new TableColumn<>("Stock");
    stockCol.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getShare().stock().getName()));

    TableColumn<Transaction, String> quantityCol = new TableColumn<>("Quantity");
    quantityCol.setCellValueFactory(cellData ->
            new SimpleStringProperty(String.valueOf(cellData.getValue().getShare().quantity())));

    TableColumn<Transaction, String> transTypeCol = new TableColumn<>("Transaction Type");
    transTypeCol.setCellValueFactory(cellData ->
            new SimpleStringProperty(controller.getTransactionType(cellData.getValue())));

    TableColumn<Transaction, String> priceCol = new TableColumn<>("Gross Value");
    priceCol.setCellValueFactory(cellData ->
            new SimpleStringProperty(
                    String.valueOf(cellData.getValue().getCalculator().calculateGross())));

    TableColumn<Transaction, String> pricePerShareCol = new TableColumn<>("Price per Share");
    pricePerShareCol.setCellValueFactory(cellData -> {
      Transaction tx = cellData.getValue();
      BigDecimal gross = tx.getCalculator().calculateGross();
      BigDecimal qty = tx.getShare().quantity();
      BigDecimal pricePerShare = gross.divide(qty, 2, RoundingMode.HALF_UP);
      return new SimpleStringProperty(pricePerShare.toPlainString());
    });

    TableColumn<Transaction, String> totalAfterTxAndComCol =
            new TableColumn<>("Total Costs / Net Proceeds");
    totalAfterTxAndComCol.setCellValueFactory(cellData ->
            new SimpleStringProperty(
                    String.valueOf(cellData.getValue().getCalculator().calculateTotal())));
    TableView<Transaction> transactions = new TableView<>();
    transactions.getColumns().addAll(weekCol, stockCol, pricePerShareCol, quantityCol,
            transTypeCol, priceCol, totalAfterTxAndComCol);
    transactions.setItems(filteredList);
    transactions.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    transactions.setMaxWidth(Double.MAX_VALUE);
    BorderPane transaction = new BorderPane();
    transaction.setCenter(transactions);
    HBox listFilters = new HBox(10, searchBar, filter);
    transaction.setTop(listFilters);

    return transaction;
  }
}