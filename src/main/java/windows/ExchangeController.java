package windows;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.exchange.Exchange;
import model.stock.Stock;

import java.util.ArrayList;

public class ExchangeController {
  private Exchange exchange;
  private final ArrayList<Label> priceLabels = new ArrayList<>();

  public ExchangeController(Exchange exchange) {
    this.exchange = exchange;
  }

  public VBox getStockNames() {
    VBox names = new VBox(20);
    for (String name : exchange.getStockNames()) {
      System.out.println(name);
      Label nameLabel = new Label(name);
      names.getChildren().add(nameLabel);
    }
    return names;
  }

  public ArrayList<HBox> createStockRow() {
    ArrayList<HBox> stockRow = new ArrayList<>();
    for (Stock stock : exchange.getStocks()) {

      Label name = new Label(stock.getName());
      Label symbol = new Label(stock.getSymbol());
      Label price = new Label(String.valueOf(stock.getCurrentPrice()));
      priceLabels.add(price);
      HBox row = new HBox(20, name, symbol, price);

      row.setStyle("""
        -fx-border-color: black;
        -fx-border-width: 1;
        -fx-padding: 10;
      """);

      row.setCursor(javafx.scene.Cursor.HAND);

      row.setOnMouseClicked(e -> {
        System.out.println("Clicked stock: " + stock.getSymbol());
      });
      stockRow.add(row);
    }
    return stockRow;
  }

  public void updatePrices() {
    for (int i = 0; i < exchange.getStocks().size(); i++) {
      Stock stock = exchange.getStocks().get(i);
      priceLabels.get(i).setText(String.valueOf(stock.getCurrentPrice()));
    }
  }
}
