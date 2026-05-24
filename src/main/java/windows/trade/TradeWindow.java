package windows.trade;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import model.exchange.Exchange;
import model.player.Player;
import model.stock.Stock;

import java.math.BigDecimal;

public abstract class TradeWindow {
  protected Label commission;
  protected Label tax;
  protected Label total;
  protected Label amountError;
  protected Label confirmation;
  protected Label amount;
  protected Label balance;
  private Player player;
  private TextField amountField;
  private Runnable onAfterTrade;

  protected TradeWindow(Player player) {
    this.player = player;
  }

  public void setOnAfterTrade(Runnable onAfterTrade) {
    this.onAfterTrade = onAfterTrade;
  }

  public void notifyAfterTrade() {
    if (onAfterTrade != null) onAfterTrade.run();
  }

  protected abstract String getActionLabel();
  protected TextField getAmountField() {
    return amountField;
  }

  protected abstract String getActionButtonStyle();
  protected abstract String getActionButtonText();
  protected abstract void onAmountClicked(String text);
  protected abstract void onMaxBtnClicked();
  protected abstract void onActionClicked();

  public VBox create(Stock stock, StackPane parent, Exchange exchange) {
    VBox popupBox = buildPopupBox();
    String stockName = stock.getName();
    BigDecimal price = stock.getCurrentPrice();

    balance = new Label("");
    balance.getStyleClass().add("trade-balance");
    StackPane.setAlignment(balance, Pos.TOP_RIGHT);
    StackPane.setMargin(balance, new Insets(10));

    StackPane contentWrapper = new StackPane();
    VBox mainContent = new VBox(15);
    mainContent.setAlignment(Pos.CENTER);

    Label title = new Label(getActionLabel() + stockName);
    title.getStyleClass().add("trade-title");


    HBox amountBox = new HBox(20);
    amountBox.setAlignment(Pos.CENTER);

    amountField = new TextField();
    amountField.setPromptText("Enter amount");
    amountField.getStyleClass().add("form-input");


    Label statsLabel = new Label("Price per stock: " + price);
    statsLabel.getStyleClass().add("trade-stats");


    commission = new Label("Fees");
    tax = new Label("Tax");
    total = new Label("Total");
    commission.setStyle("-fx-text-fill: #ecf0f1; -fx-font-weight: bold;");
    tax.setStyle("-fx-text-fill: #ecf0f1; -fx-font-weight: bold;");
    total.getStyleClass().add("trade-total");


    amountError = new Label("");
    amountError.getStyleClass().add("form-error");


    confirmation = new Label("");

    amount = new Label("Amount: ");
    amount.getStyleClass().add("trade-label");


    initController(stock, exchange, player);

    Button amountButton = new Button("Select");
    amountButton.getStyleClass().add("nav-button");
    amountButton.setOnAction(e -> onAmountClicked(amountField.getText()));

    Button maxBtn = new Button("Max");
    maxBtn.getStyleClass().add("nav-button");
    maxBtn.setOnAction(e -> onMaxBtnClicked());

    amountBox.getChildren().addAll(amountField, amountButton, maxBtn);


    Button actionButton = new Button(getActionButtonText());
    actionButton.setStyle(getActionButtonStyle());
    actionButton.setPrefWidth(200);
    actionButton.setOnAction(e -> onActionClicked());

    Button closeBtn = new Button("Close");
    closeBtn.getStyleClass().add("trade-close");
    closeBtn.setOnAction(e -> parent.getChildren().remove(popupBox));

    mainContent.getChildren().addAll(
            title, amountBox, amountError, amount, statsLabel,
            commission, tax, total, actionButton, confirmation, closeBtn
    );

    contentWrapper.getChildren().addAll(mainContent, balance);
    popupBox.getChildren().add(contentWrapper);
    StackPane.setAlignment(popupBox, Pos.CENTER);

    return popupBox;
  }


  protected abstract void initController(Stock stock, Exchange exchange, Player player);

  private static VBox buildPopupBox() {
    VBox popupBox = new VBox();
    popupBox.setAlignment(Pos.CENTER);
    popupBox.setPadding(new Insets(20));
    popupBox.setMaxSize(800, 500);
    popupBox.getStyleClass().add("trade-popup");

    return popupBox;
  }



  public void commisionSetPrice(String price) {
    commission.setText("Commission: " + price);
  }

  public void setTaxPrice(String price) {
    tax.setText("Tax: " + price);
  }

  public void setAmountErrorMessage(String message) {
    amountError.setText(message);
  }

  public void setConfirmationErrorMessage(String message) {
    confirmation.getStyleClass().removeAll("confirm-success");
    confirmation.getStyleClass().add("confirm-error");
    confirmation.setText(message);
  }

  public void setConfirmationSuccessMessage(String message) {
    confirmation.getStyleClass().removeAll("confirm-error");
    confirmation.getStyleClass().add("confirm-success");
    confirmation.setText(message);
  }

  public void setTotalPrice(String price) {
    total.setText("Total: " + price);
  }

  public void setAmount(String text) {
    amount.setText("Amount: " + text);
  }

  public void setBalance(String text) {
    balance.setText("Balance: " + text);
  }
}