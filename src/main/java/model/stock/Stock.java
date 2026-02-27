package model.stock;

import static java.util.Collections.max;
import static java.util.Collections.min;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class Stock {
  private  String symbol;
  private  String name;
  private List<BigDecimal> prices;

  public Stock(String symbol, String name, ArrayList<BigDecimal> priceHistory) {
    if (symbol == null || symbol.isBlank()) {
      throw new IllegalArgumentException("Symbol has to be filled in");
    }

    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException("Name has to be filled in");
    }
    this.symbol = symbol;
    this.name = name;
    this.prices = priceHistory;
  }

  public String getSymbol() {
    return symbol;
  }

  public String getName() {
    return name;
  }

  public List<BigDecimal> getPriceHistory() {
    return prices;
  }

  public void setNewPrice(BigDecimal price) {
    this.prices.add(price);
  }

  public void addNewSalePrice(BigDecimal price) {
    prices.add(price);
  }

  public BigDecimal getCurrentPrice() {
    return prices.getLast();
  }

  public BigDecimal getHighestPrice() {
    return max(prices);
  }

  public BigDecimal getLowestPrice() {
    return min(prices);
  }

  public BigDecimal getLatestPriceChange() {
    if (prices.size() < 2) {
      return BigDecimal.ZERO;
    }

    BigDecimal lastPrice = prices.get(prices.size() - 1);
    BigDecimal oldPrice = prices.get(prices.size() - 2);
    BigDecimal diff = lastPrice.subtract(oldPrice);

    return diff;
  }

  public BigDecimal getLatestPercentageChange() {
    if (prices.size() < 2) {
      return BigDecimal.ZERO;
    }
    BigDecimal lastPrice = prices.get(prices.size() - 1);
    BigDecimal oldPrice = prices.get(prices.size() - 2);

    if (oldPrice.compareTo(BigDecimal.ZERO) == 0) {
      return BigDecimal.ZERO;
    }

    return lastPrice.subtract(oldPrice).divide(oldPrice, 2, RoundingMode.HALF_UP);
  }


  @Override
  public String toString() {
    return "Stock{" +
            "symbol='" + symbol + '\'' +
            ", name='" + name + '\'' +
            ", prices=" + prices +
            '}';
  }
}
