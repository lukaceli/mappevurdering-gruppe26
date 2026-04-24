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
import model.stock.Stock;

import java.math.BigDecimal;

public abstract class TradeWindow {

  protected Label commission;
  protected Label total;
  protected Label amountError;
  protected Label confirmation;
  protected Label amount;
  protected Label balance;

  // Override to provide the window title buy/sell
  protected abstract String getActionLabel();

  // Override to provide the action button text buy/sell
  protected abstract String getActionButtonStyle();
  protected abstract String getActionButtonText();

  // Called when the amount button is clicked
  protected abstract void onAmountClicked(String text);

  // Called when the action button buy/sell is clicked
  protected abstract void onActionClicked();

  public VBox create(Stock stock, StackPane parent, Exchange exchange) {
    VBox popupBox = buildPopupBox();
    String stockName = stock.getName();
    BigDecimal price = stock.getCurrentPrice();

    balance = new Label("");
    balance.setStyle("-fx-text-fill: #f1c40f; -fx-font-weight: bold; -fx-font-size: 20px;");
    StackPane.setAlignment(balance, Pos.TOP_RIGHT);
    StackPane.setMargin(balance, new Insets(10));

    StackPane contentWrapper = new StackPane();
    VBox mainContent = new VBox(15);
    mainContent.setAlignment(Pos.CENTER);

    Label title = new Label(getActionLabel() + stockName);
    title.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 22px;");

    HBox amountBox = new HBox(20);
    amountBox.setAlignment(Pos.CENTER);

    TextField amountField = new TextField();
    amountField.setPromptText("Enter amount");
    amountField.setStyle("-fx-font-weight: bold;");

    Label statsLabel = new Label("Price per stock: " + price);
    statsLabel.setStyle("-fx-text-fill: #ecf0f1; -fx-font-weight: bold; -fx-font-size: 14px;");

    commission = new Label("Fees");
    total = new Label("Total");
    commission.setStyle("-fx-text-fill: #ecf0f1; -fx-font-weight: bold;");
    total.setStyle("-fx-text-fill: #ecf0f1; -fx-font-weight: bold; -fx-font-size: 16px;");

    amountError = new Label("");
    amountError.setStyle("-fx-text-fill: #ff0000; -fx-font-weight: bold;");

    confirmation = new Label("");

    amount = new Label("Amount: ");
    amount.setStyle("-fx-text-fill: #ecf0f1; -fx-font-weight: bold;");

    initController(stock, exchange);

    Button amountButton = new Button("Select");
    amountButton.setStyle("-fx-font-weight: bold;");
    amountButton.setOnAction(e -> onAmountClicked(amountField.getText()));

    amountBox.getChildren().addAll(amountField, amountButton);

    Button actionButton = new Button(getActionButtonText());
    actionButton.setStyle(getActionButtonStyle());
    actionButton.setPrefWidth(200);
    actionButton.setOnAction(e -> onActionClicked());

    Button closeBtn = new Button("Lukk");
    closeBtn.setStyle("-fx-background-color: #f1c40f; -fx-font-weight: bold; -fx-cursor: hand;");
    closeBtn.setOnAction(e -> parent.getChildren().remove(popupBox));

    mainContent.getChildren().addAll(
            title, amountBox, amountError, amount, statsLabel,
            commission, total, actionButton, confirmation, closeBtn
    );

    contentWrapper.getChildren().addAll(mainContent, balance);
    popupBox.getChildren().add(contentWrapper);
    StackPane.setAlignment(popupBox, Pos.CENTER);

    return popupBox;
  }


  protected abstract void initController(Stock stock, Exchange exchange);

  private static VBox buildPopupBox() {
    VBox popupBox = new VBox();
    popupBox.setAlignment(Pos.CENTER);
    popupBox.setPadding(new Insets(20));
    popupBox.setMaxSize(800, 500);
    popupBox.setStyle(
            "-fx-background-color: #2c3e50;" +
                    "-fx-border-color: #f1c40f;" +
                    "-fx-border-width: 3;" +
                    "-fx-background-radius: 15;" +
                    "-fx-border-radius: 15;" +
                    "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.5), 10, 0, 0, 5);"
    );
    return popupBox;
  }



  public void commisionSetPrice(String price) {
    commission.setText("Commission: " + price);
  }

  public void setAmountErrorMessage(String message) {
    amountError.setText(message);
  }

  public void setConfirmationErrorMessage(String message) {
    confirmation.setStyle("-fx-text-fill: #ff0000; -fx-font-weight: bold;");
    confirmation.setText(message);
  }

  public void setConfirmationSuccessMessage(String message) {
    confirmation.setStyle("-fx-text-fill: #65ff00; -fx-font-weight: bold;");
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