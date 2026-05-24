package model.stock;

import static java.util.Collections.max;
import static java.util.Collections.min;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import javafx.scene.chart.XYChart;

/**
 * Represents a stock with a price history and a chart data series.
 * Prices are stored in chronological order, with the most recent price last.
 */
public class Stock {

  private final String symbol;
  private final String name;
  private final List<BigDecimal> prices;
  private final XYChart.Series<Number, Number> series = new XYChart.Series<>();

  /**
   * Constructs a {@code Stock} with the given symbol, name and price history.
   *
   * @param symbol       the stock ticker symbol, must not be blank
   * @param name         the company name, must not be blank
   * @param priceHistory the initial price history, must not be empty
   * @throws NullPointerException if any argument is null or blank/empty
   */
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

  /**
   * Returns the stock's ticker symbol.
   *
   * @return the symbol
   */
  public String getSymbol() {
    return symbol;
  }

  /**
   * Returns the company name.
   *
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the full price history in chronological order.
   *
   * @return the list of historical prices
   */
  public List<BigDecimal> getPriceHistory() {
    return prices;
  }

  /**
   * Adds a new price to the history and updates the chart series.
   *
   * @param price the new price to add
   */
  public void setNewPrice(BigDecimal price) {
    prices.add(price);
    updateSeries();
  }

  /**
   * Adds a new price to the history without updating the chart series.
   *
   * @param price the price to add
   */
  public void addNewSalePrice(BigDecimal price) {
    prices.add(price);
  }

  /**
   * Returns the most recent price.
   *
   * @return the current price
   */
  public BigDecimal getCurrentPrice() {
    return prices.getLast();
  }

  /**
   * Returns the highest price in the price history.
   *
   * @return the highest price
   */
  public BigDecimal getHighestPrice() {
    return max(prices);
  }

  /**
   * Returns the lowest price in the price history.
   *
   * @return the lowest price
   */
  public BigDecimal getLowestPrice() {
    return min(prices);
  }

  /**
   * Returns the absolute price change between the two most recent prices.
   * Returns zero if fewer than two prices are recorded.
   *
   * @return the latest absolute price change
   */
  public BigDecimal getLatestPriceChange() {
    if (prices.size() < 2) {
      return BigDecimal.ZERO;
    }
    BigDecimal lastPrice = prices.getLast();
    BigDecimal previousPrice = prices.get(prices.size() - 2);
    return lastPrice.subtract(previousPrice);
  }

  /**
   * Returns the percentage price change between the two most recent prices,
   * rounded to 2 decimal places using {@link RoundingMode#HALF_UP}.
   * Returns zero if fewer than two prices are recorded or the previous price is zero.
   *
   * @return the latest percentage price change
   */
  public BigDecimal getLatestPercentageChange() {
    if (prices.size() < 2) {
      return BigDecimal.ZERO;
    }
    BigDecimal lastPrice = prices.getLast();
    BigDecimal previousPrice = prices.get(prices.size() - 2);
    if (previousPrice.compareTo(BigDecimal.ZERO) == 0) {
      return BigDecimal.ZERO;
    }
    return lastPrice.subtract(previousPrice)
            .divide(previousPrice, 4, RoundingMode.HALF_UP)
            .multiply(BigDecimal.valueOf(100))
            .setScale(2, RoundingMode.HALF_UP);
  }

  /**
   * Returns a copy of the chart series for this stock.
   *
   * @return a copied {@link XYChart.Series}
   */
  public XYChart.Series<Number, Number> getCopiedSeries() {
    XYChart.Series<Number, Number> copy = new XYChart.Series<>();
    copy.getData().addAll(series.getData());
    return copy;
  }

  /**
   * Appends the most recent price as a new data point in the chart series.
   */
  public void updateSeries() {
    series.getData().add(new XYChart.Data<>(prices.size() - 1, prices.getLast()));
  }

  @Override
  public String toString() {
    return "Stock{"
            + "symbol='" + symbol + '\''
            + ", name='" + name + '\''
            + ", currentPrice=" + getCurrentPrice()
            + ", percentageChange=" + getLatestPercentageChange()
            + '}';
  }

  private void initSeries() {
    for (int i = 0; i < prices.size(); i++) {
      series.getData().add(new XYChart.Data<>(i, prices.get(i)));
    }
  }
}