package windows;

import javafx.scene.Node;
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
    VBox stocks = new VBox(20);
    stocks.getChildren().addAll(controller.createStockRow());
    stocks.setStyle("-fx-background-color: #878c8b; -fx-padding: 20; -fx-background-radius: 10;");
    stocks.setAlignment(javafx.geometry.Pos.CENTER);
    stocks.setMaxWidth(VBox.USE_PREF_SIZE);
    for (Node node : stocks.getChildren()) {
      node.setStyle("""
        -fx-border-color: black;
        -fx-border-width: 1;
        -fx-padding: 5;
    """);
    }

    ScrollPane scrollPane = new ScrollPane(stocks);
    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);


    scrollPane.setMaxWidth(VBox.USE_PREF_SIZE);
    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    scrollPane.setMaxHeight((double) MainWindow.sceneHeight /2);
    Button button = new Button("Close");
    root.setRight(button);

    root.setCenter(scrollPane);
  }


  public BorderPane getRoot() {
    return root;
  }
}
