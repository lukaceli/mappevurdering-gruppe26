package windows;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import model.exchange.Exchange;
import model.stock.Stock;

import java.util.ArrayList;

public class ExchangeWindow {
  private BorderPane root;
  private ExchangeController controller;
  private VBox stocksBox;
  private Label stockNameLabel;
  private Label stockPriceLabel;

  public ExchangeWindow(Exchange exchange) {
    root = new BorderPane();
    controller = new ExchangeController(exchange);
    stocksBox = new VBox(20);

    ArrayList<HBox> rows = controller.createStockRow();
    for (int i = 0; i < rows.size(); i++) {
      HBox row = rows.get(i);
      Stock stock = exchange.getStocks().get(i);
      row.setCursor(javafx.scene.Cursor.HAND);

      row.setOnMouseClicked(e -> {
        setCurrentStock(stock);
        controller.setCurrentStock(stock);
        controller.updateChart();
      });
    }
      stocksBox.getChildren().addAll(rows);
      stocksBox.setStyle("-fx-background-color: #878c8b; -fx-padding: 20; -fx-background-radius: 10;");
      stocksBox.setAlignment(Pos.CENTER);
      stocksBox.setMaxWidth(VBox.USE_PREF_SIZE);

      ScrollPane scrollPane = new ScrollPane(stocksBox);
      scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
      scrollPane.setMaxWidth(VBox.USE_PREF_SIZE);
      scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
      scrollPane.setMaxHeight((double) MainWindow.sceneHeight / 2);


    VBox stockPopUp = new VBox(20);
    Button btnBuy = new Button("Buy");
    btnBuy.setOnAction(e -> {});
    Button btnSell = new Button("Sell");
    btnSell.setOnAction(e -> {});
    HBox btnBock = new HBox(20);
    btnBock.setAlignment(Pos.CENTER);
    btnBock.getChildren().addAll(btnBuy, btnSell);


    stockPopUp.setStyle("-fx-background-color: #50d3b8; -fx-padding: 20; -fx-background-radius: 10;");

    stockNameLabel = new Label("No stock selected");
    stockPriceLabel = new Label("0");
    stockNameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
    stockPriceLabel.setFont(Font.font(20));

    stockPopUp.getChildren().addAll(stockNameLabel, stockPriceLabel, btnBock, controller.createStockChart());
    root.setRight(stockPopUp);
    root.setCenter(scrollPane);


  }

  public void setCurrentStock(Stock currentStock) {
    stockNameLabel.setText(currentStock.getName());
    stockPriceLabel.setText(currentStock.getCurrentPrice().toString());
  }

  public BorderPane getRoot() {
    return root;
  }

  public ExchangeController getController() {
    return controller;
  }
}
