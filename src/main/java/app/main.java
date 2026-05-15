package app;

import java.io.IOException;
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
  public static void main(String[] args) throws IOException {
    final Path filePath = Path.of("src/main/resources/S&P500Stocks.csv");

    CsvReader csvReader = new CsvReader(filePath);

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

    List<Stock> stocks = csvReader.getStocksFromFile();

    Player player = new Player("Test", new BigDecimal("10000"));

    Exchange exchange = new Exchange("Nasdaq", stocks, BigDecimal.ONE);
    exchange.buy("AAPL", new BigDecimal("10"), player);
    for (int i = 1; i < 1000; i++) {
      exchange.advance();
    }
    exchange.sell(player.getPortfolio().getShares().getFirst(), player);
    System.out.println(player.getBalance());

  }
}

