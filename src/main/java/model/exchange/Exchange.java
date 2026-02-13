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
    Share share = new Share(stock, quantity, stock.getSalePrice());
    Transaction purchase = new Purchase(share, week, new PurchaseCalculator(share));
    if (purchase == null) {
      throw new RuntimeException("Error creating transaction: ");
    }
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
    if (sale == null) {
      throw new RuntimeException("Error creating transaction");
    }
    try {
      sale.commit(player);
      return sale;
    } catch (Exception e) {
      System.out.println(e.getMessage());
      throw new RuntimeException(e + "Cannot sell shares");
    }
  }

  public void advance() {
  }
}
