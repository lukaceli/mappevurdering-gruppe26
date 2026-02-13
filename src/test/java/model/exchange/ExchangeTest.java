package model.exchange;

import model.calculator.PurchaseCalculator;
import model.calculator.TransactionCalculator;
import model.stock.Stock;
import model.stock.Share;
import model.player.Player;
import model.transaction.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExchangeTest {

  private Exchange exchange;
  private Player player;
  private Stock apple;
  private TransactionCalculator applePurchaseCalculator;

  @BeforeEach
  void setUp() {
    ArrayList<BigDecimal> prices1 = new ArrayList<>();
    prices1.add(new BigDecimal("100.20"));
    prices1.add(new BigDecimal("110.40"));
    prices1.add(new BigDecimal("100.00"));

    ArrayList<BigDecimal> prices2 = new ArrayList<>();
    prices2.add(new BigDecimal("200.50"));
    prices2.add(new BigDecimal("210.40"));
    prices2.add(new BigDecimal("220.80"));

    ArrayList<BigDecimal> prices3 = new ArrayList<>();
    prices3.add(new BigDecimal("300.80"));
    prices3.add(new BigDecimal("310.70"));
    prices3.add(new BigDecimal("320.50"));

    List<Stock> stocks = List.of(
            new Stock("AAPL", "Apple Inc", prices1),
            new Stock("GOOG", "Google LLC", prices2),
            new Stock("AMZN", "Amazon Corp", prices3),
            new Stock("MSFT", "Microsoft Corp", prices1),
            new Stock("TSLA", "Tesla Motors", prices2),
            new Stock("META", "Meta Platforms", prices3),
            new Stock("NFLX", "Netflix Inc", prices1),
            new Stock("NVDA", "Nvidia Corp", prices2),
            new Stock("BABA", "Alibaba Group", prices3),
            new Stock("ORCL", "Oracle Systems", prices1),
            new Stock("IBM", "IBM Corporation", prices2),
            new Stock("INTC", "Intel Corp", prices3)
    );

    exchange = new Exchange("Nasdaq", stocks);

    player = new Player("subject", new BigDecimal("10000"));
    apple = stocks.getFirst();
    Share appleShare = new Share(apple, new BigDecimal("10"), apple.getSalePrice());
    applePurchaseCalculator = new PurchaseCalculator(appleShare);
  }


  @Test
  void findStocks_shouldFindBySymbol() {
    var result = exchange.findStocks("AAP");

    assertEquals(1, result.size());
    assertEquals("AAPL", result.get(0).getSymbol());
  }

  @Test
  void findStocks_shouldFindByName() {
    var result = exchange.findStocks("apple");

    assertEquals(1, result.size());
    assertEquals("Apple Inc", result.get(0).getName());
  }

  @Test
  void findStocks_shouldFindMultipleByString() {
    var result = exchange.findStocks("corp");
    assertEquals(5, result.size());
  }

  @Test
  void findStocks_shouldReturnEmptyIfNotFound() {
    var result = exchange.findStocks("Rek");
    assertTrue(result.isEmpty());
  }


  @Test
  void buy_shouldReturnTransaction_whenSuccessful() {
    Transaction transaction = exchange.buy(
            "AAPL",
            new BigDecimal("2"),
            player
    );

    assertNotNull(transaction);
  }

  @Test
  void buy_shouldReducePlayerBalance() {
    BigDecimal initialBalance = player.getStartingBalance();
    exchange.buy("AAPL", new BigDecimal("10"), player);
    assertEquals(initialBalance.subtract
            (applePurchaseCalculator.calculateTotal()).setScale(2), player.getBalance().setScale(2));
  }

  // ---------------- SELL ----------------

  @Test
  void buyShouldAddShareToPortefolio() {
    assertEquals(0, player.getPortfolio().getShares().size());
    exchange.buy("AAPL", new BigDecimal("10"), player);
    assertEquals(1, player.getPortfolio().getShares().size());
  }

  @Test
  void buy_shareSymbolMatches() {
    exchange.buy("AAPL", new BigDecimal("10"), player);
    assertEquals("AAPL", player.getPortfolio().getShares().getFirst().getStock().getSymbol());
  }

  @Test
  void sell_shouldReturnTransaction_whenSuccessful() {
    // Først kjøp
    exchange.buy("AAPL", new BigDecimal("10"), player);
    Share share = player.getPortfolio().getShares().getFirst();
    Transaction sale = exchange.sell(share, player);

    assertNotNull(sale);
  }

  @Test
  void sell_shouldIncreasePlayerBalance() {
    exchange.buy("AAPL", new BigDecimal("5"), player);

    Share share = player.getPortfolio().getShares().getFirst();
    BigDecimal beforeSell = player.getBalance();

    exchange.sell(share, player);

    assertTrue(player.getBalance().compareTo(beforeSell) > 0);
  }
}
