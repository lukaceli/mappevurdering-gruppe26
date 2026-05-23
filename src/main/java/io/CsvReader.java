package io;

import execeptions.IllegalFileFormatException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import model.stock.Stock;

/**
 * Utility class for reading and parsing CSV files containing stock data.
 * The expected CSV format is:
 *   SYMBOL, Company Name, Price
 * Lines that are empty or start with {@code #} are treated as comments and ignored.
 */
public class CsvReader {

  private final File file;

  /**
   * Constructs a {@code CsvReader} for the given {@link File}.
   *
   * @param file the CSV file to read
   */
  public CsvReader(File file) {
    this.file = file;
  }

  /**
   * Constructs a {@code CsvReader} for the file at the given {@link Path}.
   *
   * @param filePath the path to the CSV file to read
   */
  public CsvReader(Path filePath) {
    this.file = filePath.toFile();
  }

  /**
   * Reads all non-empty, non-comment lines from the CSV file.
   * Lines that are blank or whose first non-whitespace character is {@code #}
   * are skipped.
   *
   * @return a list of raw lines from the file
   * @throws IOException if the file cannot be read
   */
  public List<String> readFile() throws IOException {
    ArrayList<String> lines = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
      String line;
      while ((line = br.readLine()) != null) {
        if (line.trim().isEmpty() || line.trim().charAt(0) == '#') {
          continue;
        }
        lines.add(line);
      }
    } catch (IOException e) {
      throw new IOException("Could not read file " + file + ": " + e.getMessage(), e);
    }
    return lines;
  }

  /**
   * Parses a single CSV line into a {@link Stock} object.
   * The line must follow the format:
   *   SYMBOL, Company Name, Price
   * where {@code Price} is a valid decimal number. The price is also used
   * as the initial entry in the stock's price history.
   *
   * @param stockFromFile a single CSV line representing a stock
   * @return a {@link Stock} parsed from the given line
   * @throws IllegalFileFormatException if the line has fewer than 3 fields,
   *                                    or if the price field is not a valid number
   */
  protected Stock convertStringToStock(String stockFromFile) {
    String[] section = stockFromFile.split(",");

    if (section.length < 3) {
      throw new IllegalFileFormatException(
              "This file doesnt follow the given format: " + stockFromFile);
    }

    String symbol = section[0].trim();
    String name = section[1].trim();

    BigDecimal price;
    try {
      price = new BigDecimal(section[2].trim());
    } catch (NumberFormatException e) {
      throw new IllegalFileFormatException(
              "Price in file is not a number: " + section[2]);
    }

    ArrayList<BigDecimal> priceHistory = new ArrayList<>();
    priceHistory.add(price);
    return new Stock(symbol, name, priceHistory);
  }

  /**
   * Reads the CSV file and returns all valid stocks parsed from it.
   * Each non-comment line in the file is parsed using
   * {@link #convertStringToStock(String)}. If any line is wrong format,
   * an {@link IllegalFileFormatException} is thrown with context about
   * which file caused the error.
   *
   * @return a list of {@link Stock} objects parsed from the file
   * @throws IOException                if the file cannot be read
   * @throws IllegalFileFormatException if any line in the file is malformed
   */
  public List<Stock> getStocksFromFile() throws IOException {
    ArrayList<Stock> stocks = new ArrayList<>();
    for (String line : readFile()) {
      try {
        stocks.add(convertStringToStock(line));
      } catch (IllegalFileFormatException e) {
        throw new IllegalFileFormatException(
                "Could not read file " + file + ": " + e.getMessage());
      }
    }
    return stocks;
  }
}