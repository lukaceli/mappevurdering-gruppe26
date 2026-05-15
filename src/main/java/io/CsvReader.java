package io;

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
    try {
      BufferedReader br = new BufferedReader(new FileReader(file));
      String line;
      while ((line = br.readLine()) != null) {
        if (line.trim().isEmpty() || line.trim().charAt(0) == '#') continue;
        lines.add(line);
      }

    } catch (IOException e) {
      throw new IOException("Could not read file" + file + e.getMessage(), e);
    }
    return lines;
  }

  protected Stock concvertStringToStock(String stockFromFile) {
    String symbol;
    String name;
    BigDecimal price;
    String[] section = stockFromFile.split(",");
    try {
      symbol = section[0];
      name = section[1];
    } catch (ArrayIndexOutOfBoundsException e) {
      throw new IllegalArgumentException("This file dosnt follow the given format " + stockFromFile + ".", e);
    }
    try {
      price = new BigDecimal(section[2]);
    }  catch (NumberFormatException e) {
      throw new NumberFormatException("Price in file is not a number");
    }
    ArrayList<BigDecimal> priceHistory = new ArrayList<>();
    priceHistory.add(price);
    return new Stock(symbol, name, priceHistory);
  }

  public ArrayList<Stock> getStocksFromFile() throws IOException {
    ArrayList<Stock> stocks = new ArrayList<>();
    for (String section : readFile()) {
      stocks.add(concvertStringToStock(section));
    }
    return stocks;
  }
}
