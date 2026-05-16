package model.exchange;

import execeptions.TransactionFailedException;
import model.appState.Difficulty;
import model.calculator.PurchaseCalculator;
import model.calculator.SaleCalculator;
import model.stock.Stock;
import model.stock.Share;
import model.player.Player;
import model.stock.StockObserver;
import model.stock.StockSubject;
import model.transaction.Purchase;
import model.transaction.Sale;
import model.transaction.Transaction;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

public class Exchange implements StockSubject {

  private final String name;
  private int week;
  private final Map<String, Stock> stockMap;
  private final Random random;
  private final BigDecimal biggestPriceChange;
  private final BigDecimal volatilityMultiplier;
  BigDecimal bonusPriceGain;
  private final List<StockObserver> observers = new ArrayList<>();

  public Exchange(String name, List<Stock> stocks, BigDecimal volatilityMultiplier) {
    this.name = name;
    this.week = 1;
    this.stockMap = new HashMap<>();
    this.random = new Random();
    this.volatilityMultiplier = volatilityMultiplier;
    for (Stock stock : stocks) {
      stockMap.put(stock.getSymbol(), stock);
    }
    switch (Difficulty.getDifficulty()) {
      case Difficulty.EASY:
        bonusPriceGain = new BigDecimal("0.110");
        biggestPriceChange = new BigDecimal("0.1");
        break;
      case Difficulty.NORMAL:
        bonusPriceGain = new BigDecimal("0.005");
        biggestPriceChange = new BigDecimal("0.15");
        break;
      case Difficulty.HARD:
        bonusPriceGain = new BigDecimal("-0.001");
        biggestPriceChange = new BigDecimal("0.2");
        break;
      default:
        throw new IllegalArgumentException("Invalid difficulty");
    }
  }

  @Override
  public void addStockObserver(StockObserver observer) {
    observers.add(observer);
  }

  @Override
  public void removeStockObserver(StockObserver observer) {
    observers.remove(observer);
  }

  @Override
  public void notifyStockObservers() {
    for (StockObserver observer : observers) {
      observer.onStockUpdate();
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
    ArrayList<Stock> foundStocks = new ArrayList<>();
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
    if (stock == null) {
      throw new TransactionFailedException("Stock not found");
    }
    Share share = new Share(stock, quantity, stock.getCurrentPrice());
    Transaction purchase = new Purchase(share, week, new PurchaseCalculator(share));
    try {
      purchase.commit(player);
      return purchase;
    } catch (RuntimeException e) {
      throw new TransactionFailedException("Cannot buy shares: " + e.getMessage());
    }
  }

  public Transaction sell(Share share, Player player) {
    Transaction sale = new Sale(share, week, new SaleCalculator(share));
    try {
      sale.commit(player);
      return sale;
    } catch (RuntimeException e) {
      throw new TransactionFailedException(e + "Cannot sell shares: ");
    }
  }

  /**
   * Updates prices of all stocks by amount given by getRandomPercentChange()
   */
  public void advance() {
    week++;
    for (Stock stock : getStocks()) {
      BigDecimal priceChange = makeRandomPercentChange()
              .multiply(stock.getCurrentPrice());
      stock.setNewPrice(stock.getCurrentPrice().add(priceChange).setScale(2, RoundingMode.HALF_EVEN));
    }
    notifyStockObservers();
  }


  /**
   * Generates a simulated percent change witch can be applied to stocks.
   * @return sudo random BigDecimal.
   * bonusGain is how much on average the price will increase
   */
  protected BigDecimal makeRandomPercentChange() {
    BigDecimal percentChange;
    //Rolls 1-8
    int chance = random.nextInt(1, 9);
    if (chance == 8) {
      percentChange = BigDecimal.valueOf(random.nextDouble() * biggestPriceChange.multiply(volatilityMultiplier).doubleValue());
    } else {
      percentChange = BigDecimal.valueOf(random.nextDouble() * 0.03);
    }
    if (random.nextInt(1, 3) == 1)
      return percentChange.add(bonusPriceGain).setScale(4, RoundingMode.HALF_EVEN);
    else
      return percentChange.negate().add(bonusPriceGain).setScale(4, RoundingMode.HALF_EVEN);
  }

  public List<Stock> getGainers(int limit) {
    ArrayList<Stock> gainers = new ArrayList<>();
    for (Stock stock : stockMap.values()) {
      if (stock.getLatestPercentageChange().compareTo(BigDecimal.ZERO) > 0) {
        gainers.add(stock);
      }
    }

    gainers.sort(Comparator.comparing(Stock::getLatestPercentageChange).reversed());

    return gainers.stream()
            .limit(limit)
            .collect(Collectors.toCollection(ArrayList::new));
  }

  public List<Stock> getLosers(int limit) {
    List<Stock> losers = new ArrayList<>();
    for (Stock stock : stockMap.values()) {
      if (stock.getLatestPercentageChange().compareTo(BigDecimal.ZERO) < 0) {
        losers.add(stock);
      }
    }

    losers.sort(Comparator.comparing(Stock::getLatestPercentageChange));
    return losers.stream()
            .limit(limit)
            .collect(Collectors.toCollection(ArrayList::new));
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

  public List<String> getStockSymbols() {
      return getStocks().stream()
              .map(Stock::getSymbol)
              .collect(Collectors.toCollection(ArrayList::new));
  }

  public List<String> getStockNames() {
    return getStocks().stream()
            .map(Stock::getName)
            .collect(Collectors.toCollection(ArrayList::new));
  }

  public List<BigDecimal> getStockPrices() {
    return getStocks().stream()
            .map(Stock::getCurrentPrice)
            .collect(Collectors.toCollection(ArrayList::new));
  }
}
