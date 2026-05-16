package model.exchange;

import execeptions.TransactionFailedException;
import model.calculator.PurchaseCalculator;
import model.calculator.SaleCalculator;
import model.calculator.TransactionCalculator;
import model.stock.Stock;
import model.stock.Share;
import model.player.Player;
import model.transaction.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utility.TestFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExchangeTest {

  private Exchange exchange;
  private Player player;
  private TransactionCalculator applePurchaseCalculator;

  @BeforeEach
  void setUp() {
    exchange = TestFactory.createExchange();

    Stock stock = exchange.getStock("AAPL");
    player = TestFactory.createPlayer();
    applePurchaseCalculator = new PurchaseCalculator(new Share(stock, new BigDecimal("10"), stock.getCurrentPrice()));
  }


  @Test
  void findStocksFindBySymbol() {
    var result = exchange.findStocks("AAP");

    assertEquals(1, result.size());
    assertEquals("AAPL", result.get(0).getSymbol());
  }

  @Test
  void findStocksFindByName() {
    var result = exchange.findStocks("apple");

    assertEquals(1, result.size());
    assertEquals("Apple Inc", result.get(0).getName());
  }

  @Test
  void findStocksFindMultipleByString() {
    var result = exchange.findStocks("corp");
    assertEquals(5, result.size());
  }

  @Test
  void findStocksReturnEmptyIfNotFound() {
    var result = exchange.findStocks("Rek");
    assertTrue(result.isEmpty());
  }


  @Test
  void buyReturnTransactionWhenSuccessful() {
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


  @Test
  void buyShouldAddShareToPortefolio() {
    assertEquals(0, player.getPortfolio().getShares().size());
    exchange.buy("AAPL", new BigDecimal("10"), player);
    assertEquals(1, player.getPortfolio().getShares().size());
  }

  @Test
  void buyShareSymbolMatches() {
    exchange.buy("AAPL", new BigDecimal("10"), player);
    assertEquals("AAPL", player.getPortfolio().getShares().getFirst().getStock().getSymbol());
  }

  @Test
  void sellReturnTransactionWhenSuccessful() {
    // Først kjøp
    exchange.buy("AAPL", new BigDecimal("10"), player);
    Share share = player.getPortfolio().getShares().getFirst();
    Transaction sale = exchange.sell(share, player);

    assertNotNull(sale);
  }

  @Test
  void sellShouldIncreasePlayerBalance() {
    exchange.buy("AAPL", new BigDecimal("5"), player);

    Share share = player.getPortfolio().getShares().getFirst();
    BigDecimal beforeSell = player.getBalance();

    exchange.sell(share, player);

    assertTrue(player.getBalance().compareTo(beforeSell) > 0);
  }

  @Test
  void buy_shouldThrowWhenStockNotFound() {
    assertThrows(TransactionFailedException.class, () ->
            exchange.buy("INVALID", new BigDecimal("10"), player)
    );
  }

  @Test
  void buy_shouldThrowWhenInsufficientBalance() {
    assertThrows(TransactionFailedException.class, () ->
            exchange.buy("AAPL", new BigDecimal("999999"), player)
    );
  }

  @Test
  void testAdvanceAddsOneNewPrice() {
    List<BigDecimal> beforePrices = new ArrayList<>();
    List<Integer> beforePricesLength = new ArrayList<>();
    for (Stock stock : exchange.getStocks()) {
      beforePrices.add(stock.getCurrentPrice());
      beforePricesLength.add(stock.getPriceHistory().size());
    }

    int weekBefore = exchange.getWeek();
    exchange.advance();
    int weekAfter = exchange.getWeek();

    //Week should increase by 1
    assertEquals(weekBefore + 1, weekAfter);

    //price should change
    List<Stock> afterStocks = exchange.getStocks();
    for (int i = 0; i < afterStocks.size(); i++) {
      BigDecimal oldPrice = beforePrices.get(i);
      BigDecimal newPrice = afterStocks.get(i).getCurrentPrice();
      int newPricesLength = afterStocks.get(i).getPriceHistory().size();
      assertNotEquals(oldPrice, newPrice, "Stock price should have changed");
      assertEquals(newPricesLength-1, beforePricesLength.get(i));
    }
  }

  @Test
  void getGainers_shouldReturnStocksWithPositiveChange() {
    exchange.advance();
    ArrayList<Stock> gainers = exchange.getGainers(5);
    for (Stock stock : gainers) {
      assertTrue(stock.getLatestPercentageChange().compareTo(BigDecimal.ZERO) > 0);
    }
  }

  @Test
  void getLosers_shouldReturnStocksWithNegativeChange() {
    exchange.advance();
    ArrayList<Stock> losers = exchange.getLosers(5);
    for (Stock stock : losers) {
      assertTrue(stock.getLatestPercentageChange().compareTo(BigDecimal.ZERO) < 0);
    }
  }

  @Test
  void sell_shouldIncreaseBalanceByCorrectAmount() {
    exchange.buy("AAPL", new BigDecimal("10"), player);
    Share share = player.getPortfolio().getShares().getFirst();
    BigDecimal beforeSell = player.getBalance();
    SaleCalculator calc = new SaleCalculator(share);

    exchange.sell(share, player);

    assertEquals(
            beforeSell.add(calc.calculateTotal()).setScale(2),
            player.getBalance().setScale(2)
    );
  }

  @Test
  void hasStock_returnsTrueForExistingStock() {
    assertTrue(exchange.hasStock("AAPL"));
  }

  @Test
  void hasStock_returnsFalseForMissingStock() {
    assertFalse(exchange.hasStock("INVALID"));
  }
}
