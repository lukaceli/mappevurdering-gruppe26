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
import model.stock.Share;
import model.stock.Stock;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicReference;

public class BuyWindow {

  public static VBox create(Stock stock, StackPane parent) {

    VBox popupBox = getVBox();
    String stockName = stock.getName();
    BigDecimal price = stock.getCurrentPrice();
    AtomicReference<Share> share = new AtomicReference<>();
    share.set(new Share(stock, new BigDecimal(1), price));

    Label title = new Label("Buy: " +  stockName);
    title.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 18px;");

    HBox amountBox = new HBox(20);
    TextField amountField = new TextField();
    amountField.setPromptText("Enter amount");

    Label statsLabel = new Label("Price per stock: " + price);
    statsLabel.setStyle("-fx-text-fill: #ecf0f1;");
    statsLabel.setAlignment(Pos.CENTER);
    Label commission = new Label("Fees");
    Label total = new Label("Total");
    commission.setStyle("-fx-text-fill: #ecf0f1;");
    total.setStyle("-fx-text-fill: #ecf0f1;");


    Button amountButton = new Button("Select");
    amountButton.setOnAction(e -> {
      share.set(new Share(stock, new BigDecimal(amountField.getText()), price));
      PurchaseCalculator calculator = new PurchaseCalculator(share.get());
      commission.setText("Commission " + calculator.calculateCommission().toString());
      total.setText("Total " + calculator.calculateTotal().toString());
    });
    amountBox.getChildren().addAll(amountField, amountButton);






    Button closeBtn = new Button("Lukk");
    closeBtn.setStyle("-fx-background-color: #f1c40f; -fx-cursor: hand;");


    closeBtn.setOnAction(e -> parent.getChildren().remove(popupBox));

    popupBox.getChildren().addAll(title, amountBox, statsLabel, commission, total, closeBtn);


    StackPane.setAlignment(popupBox, Pos.TOP_CENTER);
    StackPane.setMargin(popupBox, new Insets(50, 0, 0, 0)); // 50px fra toppen

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
}