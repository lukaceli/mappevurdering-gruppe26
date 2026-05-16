package model.exchange;

import io.CsvReader;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;

public class ExchangeFactory {
  public static Exchange fromCsv(String name, String filePath, BigDecimal volatilityMult) throws IOException {
    Path path = Path.of(filePath);
    CsvReader csvReader = new CsvReader(path);
    return new Exchange(name, csvReader.getStocksFromFile(), volatilityMult);
  }
}