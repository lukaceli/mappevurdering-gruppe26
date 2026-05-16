package io;

import execeptions.IllegalFileFormatException;
import model.stock.Stock;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.ArrayList;

public class CsvReader {
  private final File file;

  public CsvReader(File file) {
    this.file = file;
  }

  public CsvReader(Path filePath) {
    this.file = filePath.toFile();
  }

  public ArrayList<String> readFile() throws IOException {
    ArrayList<String> lines = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
      String line;
      while ((line = br.readLine()) != null) {
        if (line.trim().isEmpty() || line.trim().charAt(0) == '#') continue;
        lines.add(line);
      }
    } catch (IOException e) {
      throw new IOException("Could not read file " + file + ": " + e.getMessage(), e);
    }
    return lines;
  }

  protected Stock convertStringToStock(String stockFromFile) {
    String[] section = stockFromFile.split(",");

    if (section.length < 3) {
      throw new IllegalFileFormatException("This file doesnt follow the given format: " + stockFromFile);
    }

    String symbol = section[0].trim();
    String name = section[1].trim();

    BigDecimal price;
    try {
      price = new BigDecimal(section[2].trim());
    } catch (NumberFormatException e) {
      throw new IllegalFileFormatException("Price in file is not a number: " + section[2]);
    }

    ArrayList<BigDecimal> priceHistory = new ArrayList<>();
    priceHistory.add(price);
    return new Stock(symbol, name, priceHistory);
  }

  public ArrayList<Stock> getStocksFromFile() throws IOException {
    ArrayList<Stock> stocks = new ArrayList<>();
    for (String line : readFile()) {
      try {
        stocks.add(convertStringToStock(line));
      } catch (IllegalFileFormatException e) {
        throw new IllegalFileFormatException("Could not read file " + file + ": " + e.getMessage());
      }
    }
    return stocks;
  }
}