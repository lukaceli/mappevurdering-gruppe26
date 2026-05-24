package model.exchange;

import exceptions.TransactionFailedException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import model.appstate.Difficulty;
import model.calculator.PurchaseCalculator;
import model.calculator.SaleCalculator;
import model.player.Player;
import model.stock.Share;
import model.stock.Stock;
import model.stock.StockObserver;
import model.stock.StockSubject;
import model.transaction.Purchase;
import model.transaction.Sale;
import model.transaction.Transaction;

/**
 * Represents a stock exchange that manages stocks, simulates price changes,
 * and handles buy and sell transactions.
 * Price volatility and commission rates are determined by the current {@link Difficulty}.
 */
public class Exchange implements StockSubject {

  private final String name;
  private final Map<String, Stock> stockMap;
  private final Random random;
  private final BigDecimal biggestPriceChange;
  private final BigDecimal volatilityMultiplier;
  private final List<StockObserver> observers = new ArrayList<>();
  private int week;
  private final BigDecimal bonusPriceGain;

  /**
   * Constructs an {@code Exchange} with the given name, stocks and volatility multiplier.
   * Price change limits and bonus gain are set based on the current {@link Difficulty}.
   *
   * @param name                 the name of the exchange
   * @param stocks               the list of stocks available on this exchange
   * @param volatilityMultiplier a multiplier applied to an exchange which decides the maximum
   *                             a stock can move in one week
   * @throws IllegalArgumentException if the current difficulty is not set or invalid
   */
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
      case EASY:
        bonusPriceGain = new BigDecimal("0.012");
        biggestPriceChange = new BigDecimal("0.1");
        break;
      case NORMAL:
        bonusPriceGain = new BigDecimal("0.008");
        biggestPriceChange = new BigDecimal("0.15");
        break;
      case HARD:
        bonusPriceGain = BigDecimal.ZERO;
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

  /**
   * Returns the name of the exchange.
   *
   * @return the exchange name
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the current week number.
   *
   * @return the current week
   */
  public int getWeek() {
    return week;
  }

  /**
   * Returns whether a stock with the given symbol exists on this exchange.
   *
   * @param symbol the stock symbol to look up
   * @return {@code true} if the stock exists, {@code false} otherwise
   */
  public boolean hasStock(String symbol) {
    return stockMap.containsKey(symbol);
  }

  /**
   * Returns the stock with the given symbol, or {@code null} if not found.
   *
   * @param symbol the stock symbol to look up
   * @return the matching {@link Stock}, or {@code null}
   */
  public Stock getStock(String symbol) {
    return stockMap.get(symbol);
  }

  /**
   * Returns all stocks listed on this exchange.
   *
   * @return a list of all {@link Stock} objects
   */
  public List<Stock> getStocks() {
    return new ArrayList<>(stockMap.values());
  }

  /**
   * Returns all stocks whose symbol or name contains the given search term (case-insensitive).
   *
   * @param searchTerm the term to search for
   * @return a list of matching {@link Stock} objects
   */
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

  /**
   * Executes a purchase of the given stock symbol and quantity for the given player.
   *
   * @param symbol   the symbol of the stock to buy
   * @param quantity the number of shares to buy
   * @param player   the player making the purchase
   * @return the committed {@link Transaction}
   * @throws TransactionFailedException if the stock is not found or the transaction fails
   */
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

  /**
   * Executes a sale of the given share for the given player.
   *
   * @param share  the share to sell
   * @param player the player making the sale
   * @return the committed {@link Transaction}
   * @throws TransactionFailedException if the transaction fails
   */
  public Transaction sell(Share share, Player player) {
    Transaction sale = new Sale(share, week, new SaleCalculator(share));
    try {
      sale.commit(player);
      return sale;
    } catch (RuntimeException e) {
      throw new TransactionFailedException("Cannot sell shares: " + e.getMessage());
    }
  }

  /**
   * Advances the simulation by one week, updating all stock prices randomly
   * and notifying observers.
   * Each stock price is adjusted by a random percentage.
   */
  public void advance() {
    week++;
    for (Stock stock : getStocks()) {
      BigDecimal priceChange = makeRandomPercentChange()
              .multiply(stock.getCurrentPrice());
      stock.setNewPrice(stock.getCurrentPrice()
              .add(priceChange)
              .setScale(2, RoundingMode.HALF_EVEN));
    }
    notifyStockObservers();
  }

  /**
   * Generates a simulated random percent change to apply to a stock price.
   * There is a 1-in-8 chance of a high-volatility change scaled by
   * {@code biggestPriceChange} and {@code volatilityMultiplier}.
   * Otherwise, a smaller change of up to 3% is generated.
   * The {@code bonusPriceGain} is added before the sign is determined,
   * providing a slight upward bias on average.
   *
   * @return a random percent change as a {@link BigDecimal}
   */
  protected BigDecimal makeRandomPercentChange() {
    int chance = random.nextInt(1, 9);
    BigDecimal percentChange;
    if (chance == 8) {
      percentChange = BigDecimal.valueOf(random.nextDouble()
              * biggestPriceChange.multiply(volatilityMultiplier).doubleValue());
    } else {
      percentChange = BigDecimal.valueOf(random.nextDouble() * 0.03);
    }
    if (random.nextInt(1, 3) == 1) {
      return percentChange.add(bonusPriceGain).setScale(4, RoundingMode.HALF_EVEN);
    }
    return percentChange.negate().add(bonusPriceGain).setScale(4, RoundingMode.HALF_EVEN);
  }

  /**
   * Returns the top gaining stocks this week, up to the given limit.
   *
   * @param limit the maximum number of stocks to return
   * @return a list of top gaining {@link Stock} objects sorted by percentage change descending
   */
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

  /**
   * Returns the top losing stocks this week, up to the given limit.
   *
   * @param limit the maximum number of stocks to return
   * @return a list of top losing {@link Stock} objects sorted by percentage change ascending
   */
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
}