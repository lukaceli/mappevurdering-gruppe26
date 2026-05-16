package model.stock;

import javafx.scene.chart.XYChart;

import static java.util.Collections.max;
import static java.util.Collections.min;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class Stock {
  private final String symbol;
  private final String name;
  private final List<BigDecimal> prices;
  private final XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();


  public Stock(String symbol, String name, List<BigDecimal> priceHistory) {
    if (symbol == null || symbol.isBlank()) {
      throw new NullPointerException("Symbol has to be filled in");
    }

    if (name == null || name.isBlank()) {
      throw new NullPointerException("Name has to be filled in");
    }

    if (priceHistory == null || priceHistory.isEmpty()) {
      throw new NullPointerException("Prices have to be filled in");
    }
    this.symbol = symbol;
    this.name = name;
    this.prices = priceHistory;
    initSeries();
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
    updateSeries();
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

    BigDecimal lastPrice = prices.getLast();
    BigDecimal oldPrice = prices.get(prices.size() - 2);

    return lastPrice.subtract(oldPrice);
  }

  private void initSeries() {
    for (int i = 0; i < prices.size(); i++) {
      series.getData().add(new XYChart.Data<>(i, prices.get(i)));
    }
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
    return lastPrice.subtract(oldPrice).divide(oldPrice, 4, RoundingMode.HALF_UP).
            multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP);
  }

  public void updateSeries() {
      series.getData().add(new XYChart.Data<>(prices.size()-1, prices.getLast()));
  }


  @Override
  public String toString() {
    return "Stock{" +
            "symbol='" + symbol + '\'' +
            ", name='" + name + '\'' +
            ", prices=" + getCurrentPrice().toString() +
            '}' +
            " Precent change=" + getLatestPercentageChange().toString() + "\n";
  }

  public XYChart.Series<Number, Number> getCopiedSeries() {
    XYChart.Series<Number, Number> copy = new XYChart.Series<>();
    copy.getData().addAll(this.series.getData());
    return copy;
  }

}
