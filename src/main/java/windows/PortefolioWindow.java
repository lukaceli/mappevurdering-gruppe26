package windows;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.exchange.Exchange;
import model.player.Player;
import model.stock.Share;
import model.transaction.Transaction;

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

  public PortefolioWindow(Player player, Exchange exchange) {
    this.root = new BorderPane();
    this.player = player;
    this.exchange = exchange;
    this.controller = new PortefolioController(player, exchange);
    this.sharesList = FXCollections.observableArrayList(controller.getShares());
    this.transactionList = FXCollections.observableArrayList(controller.getAllTransactions());

    root.setTop(buildHeader());
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
        totalPortfolioPercentageChange()));
    Label valueChangeLabel = new Label("Total value change:");
    totalValueChange = new Label(String.valueOf(controller.totalValueChange()));
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

    TableColumn<Share, String> percentageChangeCol = new TableColumn<>("% Change");
    percentageChangeCol.setCellValueFactory(cellData ->
        new SimpleStringProperty(String.valueOf(controller.percentageChangePerShare(cellData.getValue()) + " %")));

    TableColumn<Share, String> valueChangeCol = new TableColumn<>("Value Change");
    valueChangeCol.setCellValueFactory(cellData ->
        new SimpleStringProperty(String.valueOf(controller.valueChangePerShare(cellData.getValue()) + " $")));

    TableColumn<Share, String> totalShareValCol = new TableColumn<>("Total Share Value");
    totalShareValCol.setCellValueFactory(cellData ->
        new SimpleStringProperty(String.valueOf(controller.totalShareValue(cellData.getValue()) + " $")));

    shares.getColumns().addAll(shareCol, quantityCol, purchasePriceCol, currentPriceCol,
        percentageChangeCol, valueChangeCol, totalShareValCol);

    Button sellBtn = new Button("Sell");
    sellBtn.setOnAction(e -> {
      Share selected = shares.getSelectionModel().getSelectedItem();
      controller.sellShare(selected);
      refreshData();
    });

    sellBtn.setDisable(true);

    shares.getSelectionModel().selectedItemProperty().addListener((obs,
                                                                   oldValue, newValue) -> {
      sellBtn.setDisable(newValue == null);
    });

    shares.setItems(sharesList);

    portfolio.setCenter(shares);
    VBox bottom = new VBox(10, sellBtn, totalStats);
    portfolio.setBottom(bottom);

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

    transaction.setCenter(transactions);
    transaction.setTop(listFilters);


    return transaction;
  }

  public void refreshData() {
    sharesList.setAll(controller.getShares());
    transactionList.setAll(controller.getAllTransactions());
    balanceLabel.setText(String.valueOf("Availible balance: " + player.getBalance() + " $"));
    totalPercentageChange.setText(String.valueOf(controller.totalPortfolioPercentageChange() + " %"));
    totalValueChange.setText(String.valueOf(controller.totalValueChange() + " $"));
    totalAccountValue.setText(String.valueOf("Total Networth: " + controller.totalAccountValue() + " $"));
  }

  public BorderPane getRoot() { return root; }
}
