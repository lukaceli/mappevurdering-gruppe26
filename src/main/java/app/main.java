package app;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import model.calculator.PurchaseCalculator;
import model.exchange.Exchange;
import model.player.Player;
import model.player.Portfolio;
import model.transaction.Purchase;
import model.stock.Share;
import model.stock.Stock;

public class main {
  public static void main(String[] args) {

    Portfolio portfolio = new Portfolio();
    Player player = new Player("Test", new BigDecimal("100"));
    ArrayList<BigDecimal> applePrices = new ArrayList<>();
    Stock apple = new Stock("APPL", "Apple", applePrices);
    Share appleShare = new Share(apple, new BigDecimal("10.1"), apple.getCurrentPrice());
    Purchase purchase = new Purchase(appleShare, 1, new PurchaseCalculator(appleShare));

    /**
     * Liste med aksjer og priser bare for testing
     */
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


    Exchange exchange = new Exchange("Nasdaq", stocks);

    System.out.println(exchange.stockmapToString());
    exchange.advance();
    System.out.println(exchange.stockmapToString());
    purchase.commit(player);
  /*
  purchase.commit(player);
  portfolio.addShare(appleShare);
  System.out.println(appleShare.getPurchasePrice());
  System.out.println(portfolio.getShares());
  System.out.println(player.getBalance());

   */

  }
}

