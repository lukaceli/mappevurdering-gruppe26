package model.exchange;

import model.calculator.PurchaseCalculator;
import model.calculator.SaleCalculator;
import model.stock.Stock;
import model.stock.Share;
import model.player.Player;
import model.transaction.Purchase;
import model.transaction.Sale;
import model.transaction.Transaction;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class Exchange {

  private String name;
  private int week;
  private Map<String, Stock> stockMap;
  private Random random;

  public Exchange(String name, List<Stock> stocks) {
    this.name = name;
    this.week = 1;
    this.stockMap = new HashMap<>();
    this.random = new Random();
    for (Stock stock : stocks) {
      stockMap.put(stock.getSymbol(), stock);
    }
  }

  public String getName() {
    return name;
  }

  public int getWeek() {
    return week;
  }

  public boolean hasStock(String symbol) {
    return stockMap.containsKey(symbol);
  }

  public Stock getStock(String symbol) {
    return stockMap.get(symbol);
  }

  public List<Stock> getStocks() {
    return new ArrayList<>(stockMap.values());
  }

  public List<Stock> findStocks(String searchTerm) {
    List<Stock> foundStocks = new ArrayList<>();
    for (Stock stock : stockMap.values()) {
      if (stock.getSymbol().toLowerCase().contains(searchTerm.toLowerCase())
      || stock.getName().toLowerCase().contains(searchTerm.toLowerCase())) {
        foundStocks.add(stock);
      }
    }
    return foundStocks;
  }

  public Transaction buy(String symbol, BigDecimal quantity, Player player) {
    Stock stock = getStock(symbol);
    Share share = new Share(stock, quantity, stock.getCurrentPrice());
    Transaction purchase = new Purchase(share, week, new PurchaseCalculator(share));
    try {
      purchase.commit(player);
      return purchase;
    } catch (Exception e) {
      System.out.println(e.getMessage());
      throw new RuntimeException(e + "Cannot buy shares: ");
    }
  }

  public Transaction sell(Share share, Player player) {
    Transaction sale = new Sale(share, week, new SaleCalculator(share));
    try {
      sale.commit(player);
      return sale;
    } catch (Exception e) {
      System.out.println(e.getMessage());
      throw new RuntimeException(e + "Cannot sell shares: ");
    }
  }

  public void advance() {
    week++;
    for (Stock stock : getStocks()) {
      BigDecimal priceChange = getRandomPercentChange()
              .multiply(stock.getCurrentPrice());
      stock.setNewPrice(stock.getCurrentPrice().add(priceChange).setScale(2, RoundingMode.HALF_EVEN));
      System.out.println(getRandomPercentChange());
    }
  }


  /**
   * Generates a simulated percent change witch can be applied to stocks.
   * @return sudo random BigDecimal.
   */
  private BigDecimal getRandomPercentChange() {
    BigDecimal percentChange;
    //Rolls 1-8
    int chance = random.nextInt(1, 9);
    if (chance == 8) {
      percentChange = BigDecimal.valueOf(random.nextDouble() * 0.15);
    } else {
      percentChange = BigDecimal.valueOf(random.nextDouble() * 0.03);
    }
    if (random.nextInt(1, 3) == 1)
      return percentChange.setScale(4, RoundingMode.HALF_EVEN);
    else
      return percentChange.negate().setScale(4, RoundingMode.HALF_EVEN);
  }

  public String stockmapToString() {
    StringBuilder builder = new StringBuilder();
    for (Stock stock : stockMap.values()) {
      builder.append(stock.getSymbol());
      builder.append(", ");
      builder.append(stock.getName());
      builder.append(", ");
      builder.append(stock.getCurrentPrice());
      builder.append("\n");
    }
    return builder.toString();
  }
}
