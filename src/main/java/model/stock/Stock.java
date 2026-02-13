package model.stock;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Stock {
  private  String symbol;
  private  String name;
  private List<BigDecimal> prices;

  public Stock(String symbol, String name, ArrayList<BigDecimal> prices) {
    if (symbol == null || symbol.isBlank()) {
      throw new IllegalArgumentException("Symbol has to be filled in");
    }

    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException("Name has to be filled in");
    }
    this.symbol = symbol;
    this.name = name;
    this.prices = prices;
  }

  public String getSymbol() {
    return symbol;
  }

  public String getName() {
    return name;
  }

  public List<BigDecimal> getPrices() {
    return prices;
  }

  public void addNewSalePrice(BigDecimal price) {
    prices.add(price);
  }

  public BigDecimal getSalePrice() {
    return prices.getLast();
  }

}
