package windows;

import java.math.BigDecimal;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import model.exchange.Exchange;
import model.player.Player;
import model.stock.Share;
import model.transaction.Transaction;
import javafx.scene.control.TableCell;


public class PortefolioWindow {
  private BorderPane root;
  private Player player;
  private Exchange exchange;
  private PortefolioController controller;
  private ObservableList<Share> sharesList;
  private ObservableList<Transaction> transactionList;
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
  private Runnable onBalanceUpdate;
  private Label totalValueChangeValue;



  public PortefolioWindow(Player player, Exchange exchange, Runnable onBalanceUpdate) {
    this.root = new BorderPane();
    this.player = player;
    this.exchange = exchange;
    this.controller = new PortefolioController(player, exchange);
    this.sharesList = FXCollections.observableArrayList(controller.getShares());
    this.transactionList = FXCollections.observableArrayList(controller.getAllTransactions());
    this.onBalanceUpdate = onBalanceUpdate;


    root.setTop(buildHeader());
    VBox top = new VBox(buildHero(), buildInfoCards());
    root.setTop(top);
    root.setCenter(buildTabPane());

  }

  private HBox buildHeader() {
    Label playerName = new Label("Player name: " + player.getName());
    balanceLabel = new Label("Available money: "+ player.getBalance());
    Label status = new Label("Player Status: " + player.getStatus());
    totalAccountValue = new Label("Total Account Value: " + controller.totalAccountValue());

    HBox header = new HBox(50, playerName, balanceLabel, totalAccountValue, status);
    return header;
  }

  private TabPane buildTabPane() {
    TabPane profileTabs = new TabPane();
    Tab portfolio = new Tab("Portfolio", buildPortfolioTab());
    Tab transactions = new Tab("Transactions", buildTransactionsTab());

    portfolio.setClosable(false);
    transactions.setClosable(false);

    profileTabs.getTabs().addAll(portfolio, transactions);

    return profileTabs;
  }

  private BorderPane buildPortfolioTab() {
    BorderPane portfolio = new BorderPane();
    TableView<Share> shares = new TableView();
    Label totalPercentageChangeLabel = new Label("Total yield:");
    totalPercentageChange = new Label(String.valueOf(controller.
        totalOverallPercentageChange()));
    Label valueChangeLabel = new Label("Total value change:");
    totalValueChange = new Label(String.valueOf(controller.totalOverallValueChange()));
    HBox totalStats = new HBox(10, totalPercentageChangeLabel, totalPercentageChange,
        valueChangeLabel, totalValueChange);


    TableColumn<Share, String> shareCol = new TableColumn<>("Stock Name");
    shareCol.setCellValueFactory(cellData ->
        new SimpleStringProperty(cellData.getValue().getStock().getName()));

    TableColumn<Share, String> quantityCol = new TableColumn<>("Quantity");
    quantityCol.setCellValueFactory(cellData ->
        new SimpleStringProperty(String.valueOf(cellData.getValue().getQuantity())));

    TableColumn<Share, String> purchasePriceCol = new TableColumn<>("Purchase price");
    purchasePriceCol.setCellValueFactory(cellData ->
        new SimpleStringProperty(String.valueOf(cellData.getValue().getPurchasePrice())));

    TableColumn<Share, String> currentPriceCol = new TableColumn<>("Current price");
    currentPriceCol.setCellValueFactory(cellData ->
        new SimpleStringProperty(String.valueOf(cellData.getValue().getStock().getCurrentPrice())));

    // Her er det brukt AI til å få hjelp til å endre farge på prosentendring.
    TableColumn<Share, String> percentageChangeCol = new TableColumn<>("% Change");
    percentageChangeCol.setCellValueFactory(cellData ->
        new SimpleStringProperty(String.valueOf(controller.percentageChangePerShare(cellData.getValue()) + " %")));
    percentageChangeCol.setCellFactory(col -> new TableCell<Share, String>() {
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

    //Samme her
    TableColumn<Share, String> valueChangeCol = new TableColumn<>("Value Change");
    valueChangeCol.setCellValueFactory(cellData ->
        new SimpleStringProperty(String.valueOf(controller.valueChangePerShare(cellData.getValue()) + " $")));
    valueChangeCol.setCellFactory(col -> new TableCell<Share, String>() {
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
        new SimpleStringProperty(String.valueOf(controller.totalShareValue(cellData.getValue()) + " $")));

    shares.getColumns().addAll(shareCol, quantityCol, purchasePriceCol, currentPriceCol,
        percentageChangeCol, valueChangeCol, totalShareValCol);

    Button sellBtn = new Button("Sell");
    sellBtn.setOnAction(e -> {
      Share selected = shares.getSelectionModel().getSelectedItem();
      controller.sellShare(selected);
      onBalanceUpdate.run();
      refreshData();
    });

    sellBtn.setDisable(true);

    shares.getSelectionModel().selectedItemProperty().addListener((obs,
                                                                   oldValue, newValue) -> {
      sellBtn.setDisable(newValue == null);
    });
    sellBtn.getStyleClass().add("sell-button");

    shares.setItems(sharesList);
    shares.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    shares.setMaxWidth(Double.MAX_VALUE);

    portfolio.setCenter(shares);
    Region spacer = new Region();
    HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);


    Button sellAllBtn = new Button("Sell all");
    sellAllBtn.getStyleClass().add("sell-all-button");
    sellAllBtn.setOnAction(e -> {
      controller.sellAll();
      onBalanceUpdate.run();
      refreshData();
    });

    HBox footer = new HBox(10, sellBtn, spacer, sellAllBtn);
    footer.getStyleClass().add("portfolio-footer");

    portfolio.setBottom(footer);

    return portfolio;
  }

  private BorderPane buildTransactionsTab() {
    BorderPane transaction = new BorderPane();
    TableView<Transaction> transactions = new TableView<>();

    TextField searchBar = new TextField();
    ComboBox<String> filter = new ComboBox();
    filter.getItems().addAll("All", "Purchase", "Sale");
    filter.setValue("All");

    FilteredList<Transaction> filteredList = new FilteredList(transactionList, t -> true);

    Runnable updateList = () -> {
      String search = searchBar.getText().toLowerCase();
      String type = filter.getValue();

      filteredList.setPredicate(t -> {
        boolean matchesSearch = t.getShare().getStock().getName().toLowerCase().contains(search);
        boolean matchesType = type.equals("All") || controller.getTransactionType(t).equals(type);
        return matchesSearch && matchesType;
      });
    };

    searchBar.textProperty().addListener((obs, oldValue,
                                          newValue) -> updateList.run());
    filter.valueProperty().addListener((obs, oldValue,
                                        newValue) -> updateList.run());

    HBox listFilters = new HBox(10, searchBar, filter);


    TableColumn<Transaction, String> stockCol = new TableColumn<>("Stock");
    stockCol.setCellValueFactory(cellData ->
        new SimpleStringProperty(String.valueOf(cellData.getValue().getShare().
            getStock().getName())));

    TableColumn<Transaction, String> quantityCol = new TableColumn<>("Quantity");
    quantityCol.setCellValueFactory(cellData ->
        new SimpleStringProperty(String.valueOf(cellData.getValue().getShare().getQuantity())));

    TableColumn<Transaction, String> transTypeCol = new TableColumn<>("Transaction type");
    transTypeCol.setCellValueFactory(cellData ->
        new SimpleStringProperty(controller.getTransactionType(cellData.getValue())));


    TableColumn<Transaction, String> priceCol = new TableColumn<>("Gross price per share");
    priceCol.setCellValueFactory(cellData ->
        new SimpleStringProperty(String.valueOf(cellData.getValue().getCalculator().
            calculateGross())));


    transactions.getColumns().addAll(stockCol, quantityCol, transTypeCol, priceCol);
    transactions.setItems(filteredList);
    transactions.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    transactions.setMaxWidth(Double.MAX_VALUE);

    transaction.setCenter(transactions);
    transaction.setTop(listFilters);


    return transaction;
  }

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

  public HBox buildInfoCards() {
    VBox balanceCard = new VBox(5);
    Label balanceTitle = new Label("BALANCE");
    balanceValue = new Label(player.getBalance() + "$");

    balanceTitle.getStyleClass().add("info-title");
    balanceValue.getStyleClass().add("info-value-green");
    balanceCard.getStyleClass().add("info-card");

    balanceCard.getChildren().addAll(balanceTitle, balanceValue);

    VBox netWorthCard = new VBox(5);
    Label netWorthTitle = new Label("NET WORTH");
    netWorthValue = new Label(player.getNetWorth() + "$");

    netWorthTitle.getStyleClass().add("info-title");
    netWorthValue.getStyleClass().add("info-value-green");
    netWorthCard.getStyleClass().add("info-card");

    VBox yieldCard = new VBox(5);
    Label yieldTitle = new Label("YIELD");
    yieldValuePercentage = new Label(controller.totalOverallPercentageChange() + "%");

    yieldTitle.getStyleClass().add("info-title");

    yieldCard.getStyleClass().add("info-card");

    VBox valueChangeCard = new VBox(5);
    Label valueChangeTitle = new Label("VALUE CHANGE");
    totalValueChangeValue = new Label(controller.totalOverallValueChange() + "$");

    valueChangeTitle.getStyleClass().add("info-title");
    totalValueChangeValue.getStyleClass().add("info-value-green");
    valueChangeCard.getStyleClass().add("info-card");
    valueChangeCard.getChildren().addAll(valueChangeTitle, totalValueChangeValue);

    VBox weekCard = new VBox(5);
    Label weekTitle = new Label("WEEK");
    weekNumber = new Label(String.valueOf(exchange.getWeek()));

    weekTitle.getStyleClass().add("info-title");
    weekNumber.getStyleClass().add("info-value-green");
    weekCard.getStyleClass().add("info-card");

    netWorthCard.getChildren().addAll(netWorthTitle, netWorthValue);
    yieldCard.getChildren().addAll(yieldTitle, yieldValuePercentage);
    weekCard.getChildren().addAll(weekTitle, weekNumber);

    HBox cards = new HBox(20, balanceCard, netWorthCard, yieldCard, valueChangeCard, weekCard);
    cards.getStyleClass().add("info-cards");
    return cards;
  }

  public void refreshData() {
    sharesList.setAll(controller.getShares());
    transactionList.setAll(controller.getAllTransactions());
    balanceLabel.setText(String.valueOf("Availible balance: " + player.getBalance() + " $"));
    totalPercentageChange.setText(String.valueOf(controller.totalOverallPercentageChange() + " %"));
    totalValueChange.setText(String.valueOf(controller.totalOverallValueChange() + " $"));
    totalAccountValue.setText(String.valueOf("Total Networth: " + controller.totalAccountValue() + " $"));
    balanceValue.setText(player.getBalance() + "$");
    netWorthValue.setText(player.getNetWorth() + "$");
    BigDecimal yield = controller.totalOverallPercentageChange();
    yieldValuePercentage.setText(yield + "%");
    if (yield.compareTo(BigDecimal.ZERO) < 0) {
      yieldValuePercentage.setStyle("-fx-text-fill: #dc2626; -fx-font-weight: bold; -fx-font-size: 20px;");
    } else {
      yieldValuePercentage.setStyle("-fx-text-fill: #16a34a; -fx-font-weight: bold; -fx-font-size: 20px;");
    }
    weekNumber.setText(String.valueOf(exchange.getWeek()));
    badge.setText("★ " + player.getStatus());
    BigDecimal totalChange = controller.totalOverallValueChange();

    BigDecimal valueChange = controller.totalOverallValueChange();
    totalValueChangeValue.setText(valueChange + "$");
    if (valueChange.compareTo(BigDecimal.ZERO) < 0) {
      totalValueChangeValue.setStyle("-fx-text-fill: #dc2626; -fx-font-weight: bold; -fx-font-size: 20px;");
    } else {
      totalValueChangeValue.setStyle("-fx-text-fill: #16a34a; -fx-font-weight: bold; -fx-font-size: 20px;");
    }
  }

  public BorderPane getRoot() { return root; }
}
