package windows;

import javafx.geometry.Pos;
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
  private VBox stocksBox;

  public ExchangeWindow(Exchange exchange) {
    root = new BorderPane();
    controller = new ExchangeController(exchange);
    stocksBox = new VBox(20);
    stocksBox.getChildren().addAll(controller.createStockRow());
    stocksBox.setStyle("-fx-background-color: #878c8b; -fx-padding: 20; -fx-background-radius: 10;");
    stocksBox.setAlignment(Pos.CENTER);
    stocksBox.setMaxWidth(VBox.USE_PREF_SIZE);

    ScrollPane scrollPane = new ScrollPane(stocksBox);
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

  public ExchangeController getController() {
    return controller;
  }
}
