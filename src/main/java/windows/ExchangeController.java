package windows;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.exchange.Exchange;
import model.stock.Stock;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ExchangeController {
  private Exchange exchange;
  private final ArrayList<String> stockNames = new ArrayList<>();

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
      HBox row = new HBox(20, name, symbol, price);

      row.setStyle("""
        -fx-border-color: black;
        -fx-border-width: 1;
        -fx-padding: 10;
    """);
      stockRow.add(row);
    }
    return stockRow;
  }

  /**
  public VBox getStockSymbols() {
    VBox stockSymbols = new VBox(20);
    for (String symbol : exchange.getStockSymbols()) {
      Label symbolLabel = new Label(symbol);
      stockSymbols.getChildren().add(symbolLabel);
    }
    return stockSymbols;
  }

  public VBox getStockPrice() {
    VBox prices = new VBox(20);
    for (BigDecimal price : exchange.getStockPrices()) {
      Label priceLabel = new Label(price.toString());
      prices.getChildren().add(priceLabel);
    }
    return prices;
  }
   **/
}
