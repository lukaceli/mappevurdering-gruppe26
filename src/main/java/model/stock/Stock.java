package model.stock;

import java.math.BigDecimal;
import java.util.ArrayList;

public class Stock {
  private  String symbol;
  private  String name;
  private ArrayList<BigDecimal> prices;

  public Stock(String symbol, String name, ArrayList<BigDecimal> prices) {
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

  public ArrayList<BigDecimal> getPrices() {
    return prices;
  }

  public void addNewSalePrice(BigDecimal price) {
    prices.add(price);
  }

  public BigDecimal getSalePrice() {
    return prices.getLast();
  }

}
