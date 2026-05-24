package utility;

import model.appstate.Difficulty;
import model.exchange.Exchange;
import model.player.Player;
import model.stock.Share;
import model.stock.Stock;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TestFactory {


  public static Player createPlayer() {
    Difficulty.setDifficulty(Difficulty.NORMAL);
    return new Player("TestPlayer", new BigDecimal("10000"));
  }

  public static Stock getAppleStock() {
    TestFactory.createExchange().getStock("AAPL");
    return TestFactory.createExchange().getStock("AAPL");
  }

  public static Exchange createExchange() {
    ArrayList<BigDecimal> prices1 = new ArrayList<>();
    prices1.add(new BigDecimal("90.00"));
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
            new Stock("AAPL", "Apple Inc", new ArrayList<>(prices1)),
            new Stock("GOOG", "Google LLC", new ArrayList<>(prices2)),
            new Stock("AMZN", "Amazon Corp", new ArrayList<>(prices3)),
            new Stock("MSFT", "Microsoft Corp", new ArrayList<>(prices1)),
            new Stock("TSLA", "Tesla Motors", new ArrayList<>(prices2)),
            new Stock("META", "Meta Platforms", new ArrayList<>(prices3)),
            new Stock("NFLX", "Netflix Inc", new ArrayList<>(prices1)),
            new Stock("NVDA", "Nvidia Corp", new ArrayList<>(prices2)),
            new Stock("BABA", "Alibaba Group", new ArrayList<>(prices3)),
            new Stock("ORCL", "Oracle Systems", new ArrayList<>(prices1)),
            new Stock("IBM", "IBM Corporation", new ArrayList<>(prices2)),
            new Stock("INTC", "Intel Corp", new ArrayList<>(prices3))
    );
    Difficulty.setDifficulty(Difficulty.NORMAL);
    return new Exchange("TestExchange", stocks, BigDecimal.ONE);
  }

  public static Share createAppleShare() {
  Exchange exchange =  createExchange();
  Stock stock = exchange.getStock("AAPL");
  return new Share(stock, new BigDecimal("10"), stock.getLowestPrice());
  }

  public static Share createGoogleShare() {
    Exchange exchange = createExchange();
    Stock stock = exchange.getStock("GOOG");
    return new Share(stock, new BigDecimal("10"), stock.getLowestPrice());
  }
}
