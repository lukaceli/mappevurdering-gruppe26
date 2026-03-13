package windows;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.exchange.Exchange;

public class ExchangeWindow {
  private BorderPane root;
  private ExchangeController controller;

  public ExchangeWindow(Exchange exchange) {
    root = new BorderPane();
    controller = new ExchangeController(exchange);
    HBox stocks = new HBox(50);
    stocks.getChildren().addAll(controller.getStockNames(),
            controller.getStockSymbols(),
            controller.getStockPrice());
    stocks.setStyle("-fx-background-color: #878c8b; -fx-padding: 20; -fx-background-radius: 10;");
    stocks.setAlignment(javafx.geometry.Pos.CENTER);
    stocks.setMaxWidth(VBox.USE_PREF_SIZE);

    ScrollPane scrollPane = new ScrollPane(stocks);
    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);


    scrollPane.setMaxWidth(VBox.USE_PREF_SIZE);
    scrollPane.setMaxHeight((double) MainWindow.sceneHeight /2);
    Button button = new Button("Close");
    root.setRight(button);

    root.setCenter(scrollPane);
  }


  public BorderPane getRoot() {
    return root;
  }
}
