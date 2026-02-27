package app;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import io.CsvReader;
import model.calculator.PurchaseCalculator;
import model.exchange.Exchange;
import model.player.Player;
import model.player.Portfolio;
import model.transaction.Purchase;
import model.stock.Share;
import model.stock.Stock;

public class main {
  public static void main(String[] args) {
    final Path filePath = Path.of("src/main/resources/S&P500Stocks.csv");

    CsvReader csvReader = new CsvReader();

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

    List<Stock> stocks = csvReader.getStocksFromFile(filePath);

    Portfolio portfolio = new Portfolio();
    Player player = new Player("Test", new BigDecimal("100"));
    Share appleShare = new Share(stocks.getFirst(), new BigDecimal("10.1"), stocks.getFirst().getCurrentPrice());
    Purchase purchase = new Purchase(appleShare, 1, new PurchaseCalculator(appleShare));


    Exchange exchange = new Exchange("Nasdaq", stocks);

    System.out.println(exchange.stockmapToString());
    exchange.advance();
    purchase.commit(player);
    portfolio.addShare(appleShare);
    /**
    System.out.println(appleShare.getPurchasePrice());
    System.out.println(portfolio.getShares());
    System.out.println(player.getBalance());
     **/

  }
}

