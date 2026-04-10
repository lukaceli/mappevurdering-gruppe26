package windows;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import model.calculator.PurchaseCalculator;
import model.exchange.Exchange;
import model.stock.Stock;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicReference;

public class BuyWindow {

  private Label commission;
  private Label total;
  private Label amountError;
  private Label confirmation;
  private BuyController controller;
  private Label amount;

  public VBox create(Stock stock, StackPane parent, Exchange exchange) {
    VBox popupBox = getVBox();
    String stockName = stock.getName();
    BigDecimal price = stock.getCurrentPrice();




    Label title = new Label("Buy: " +  stockName);
    title.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 18px;");

    HBox amountBox = new HBox(20);
    TextField amountField = new TextField();
    amountField.setPromptText("Enter amount");

    Label statsLabel = new Label("Price per stock: " + price);
    statsLabel.setStyle("-fx-text-fill: #ecf0f1;");
    statsLabel.setAlignment(Pos.CENTER);
    commission = new Label("Fees");
    total = new Label("Total");
    commission.setStyle("-fx-text-fill: #ecf0f1;");
    total.setStyle("-fx-text-fill: #ecf0f1;");
    amountError = new Label("");
    confirmation = new Label("");
    amountError.setStyle("-fx-text-fill: #ff0000;");
    amount = new Label("Amount: ");
    amount.setStyle("-fx-text-fill: #ecf0f1;");
    controller = new BuyController(this, stock, exchange);


    Button amountButton = new Button("Select");
    amountButton.setOnAction(e -> {
      controller.onAmountBtnClicked(amountField.getText());
    });
    amountBox.getChildren().addAll(amountField, amountButton);

    Button buyButton = new Button("Buy");

    buyButton.setOnAction(e -> {
      controller.onBuyBtnClicked();
    });


    Button closeBtn = new Button("Lukk");
    closeBtn.setStyle("-fx-background-color: #f1c40f; -fx-cursor: hand;");


    closeBtn.setOnAction(e -> parent.getChildren().remove(popupBox));

    popupBox.getChildren().addAll(title, amountBox,amountError, amount, statsLabel, commission, total, buyButton, confirmation, closeBtn);


    StackPane.setAlignment(popupBox, Pos.TOP_CENTER);
    StackPane.setMargin(popupBox, new Insets(50, 0, 0, 0));

    return popupBox;
  }

  private static VBox getVBox() {
    VBox popupBox = new VBox(15);
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
  public void setConfirmationErrorMessage() {
    confirmation.setStyle("-fx-text-fill: #ff0000;");
    confirmation.setText("Insufficient Balance");
  }
  public void setConfirmationSuccessMessage() {
    confirmation.setStyle("-fx-text-fill: #65ff00;");
    confirmation.setText("Purchase successfull!");
  }

  public void setTotalPrice(String price) {
    total.setText("Total: " + price);
  }

  public void setAmount(String text) {
    amount.setText("Amount: " + text);
  }
}