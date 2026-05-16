package model.exchange;

import model.calculator.PurchaseCalculator;
import model.calculator.TransactionCalculator;
import model.stock.Stock;
import model.stock.Share;
import model.player.Player;
import model.transaction.Transaction;
import model.transaction.TransactionFactory;
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

    player = TestFactory.createPlayer();
    applePurchaseCalculator = new PurchaseCalculator(TestFactory.createShare());
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

  /**
  @Test
  void testRandomPercentChangeWithinRange() {
    Exchange exchange = new Exchange("Test", List.of());

    for (int i = 0; i < 5000; i++) {
      BigDecimal change = exchange.makeRandomPercentChange();
      // Sjekk at den ikke er større enn 0.153 eller mindre enn -0.15
      assertTrue(change.compareTo(exchange.biggestPriceChange.negate()) >= 0, "Change too low: " + change);
      assertTrue(change.compareTo(exchange.biggestPriceChange.add(exchange.bonusPriceGain)) <= 0, "Change too high: " + change);
    }
  }
   **/

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

}
