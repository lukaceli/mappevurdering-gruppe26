package io;

import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import model.stock.Stock;

/**
 * Utility class for writing stock data to CSV files.
 * The output CSV format is:
 *   SYMBOL, Company Name, Price
 * The file will contain a comment header line starting with {@code #}.
 */
public class CsvWriter {

  /**
   * Writes a list of stocks to a CSV file at the given path.
   * Each stock is written as a single line in the format:
   *   SYMBOL, Name, Price
   * The file will be created or overwritten if it already exists.
   *
   * @param stocks   the list of stocks to write
   * @param filePath the path to the output CSV file
   * @throws IllegalArgumentException if {@code stocks} or {@code filePath} is null or empty
   * @throws IOException              if the file cannot be written
   */
  public void writeStocksToFile(List<Stock> stocks, Path filePath) throws IOException {
    if (stocks == null || filePath == null || stocks.isEmpty()) {
      throw new IllegalArgumentException("Stocks or Files cannot be null or empty");
    }
    try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
      writer.write("# Symbol,Name,Price");
      writer.newLine();
      writer.newLine();
      for (Stock stock : stocks) {
        writer.write(stock.getSymbol() + "," + stock.getName() + "," + stock.getCurrentPrice());
        writer.newLine();
      }
    } catch (IOException e) {
      throw new IOException("Could not write to specified file" + e.getMessage(), e);
    }
  }

  /**
   * Simple manual test method for verifying file writing functionality.
   *
   * @throws IOException if the file cannot be written
   */
  void main() throws IOException {
    CsvWriter writer = new CsvWriter();
    ArrayList<BigDecimal> prices = new ArrayList<>();
    prices.add(new BigDecimal("320.50"));
    Stock stock = new Stock("SNAK", "Snake", prices);
    ArrayList<Stock> stocks = new ArrayList<>();
    stocks.add(stock);
    writer.writeStocksToFile(stocks, Path.of("./stocksWrittenFiles/stocks.csv"));
  }
}