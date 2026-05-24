package model.exchange;

import io.CsvReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;

/**
 * Factory class for creating {@link Exchange} instances from external data sources.
 */
public class ExchangeFactory {

  private ExchangeFactory() {
  }

  /**
   * Creates an {@link Exchange} by reading stocks from a CSV file.
   *
   * @param name            the name of the exchange
   * @param filePath        the path to the CSV file containing stock data
   * @param volatilityMultiplier  a multiplier applied to price changes on volatile weeks
   * @return a new {@link Exchange} populated with stocks from the file
   * @throws IOException if the CSV file cannot be read
   */
  public static Exchange fromCsv(String name, String filePath, BigDecimal volatilityMultiplier)
          throws IOException {
    Path path = Path.of(filePath);
    CsvReader csvReader = new CsvReader(path);
    return new Exchange(name, csvReader.getStocksFromFile(), volatilityMultiplier);
  }
}