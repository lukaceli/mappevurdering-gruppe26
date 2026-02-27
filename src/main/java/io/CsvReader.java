package io;

import model.stock.Stock;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.ArrayList;

public class CsvReader {

  public ArrayList<String> readFile(Path filePath) throws IOException {
    ArrayList<String> lines = new ArrayList<>();
    try {
      BufferedReader br = new BufferedReader(new FileReader(filePath.toFile()));
      String line;
      while ((line = br.readLine()) != null) {
        if (line.trim().isEmpty() || line.trim().charAt(0) == '#') continue;
        lines.add(line.trim());
      }

    } catch (IOException e) {
      //må gjøre noe her.
      throw new IOException("Could not read file" + filePath + e.getMessage(), e);
    }
    return lines;
  }

  protected Stock concvertStringToStock(String stockFromFile) {
    String symbol;
    String name;
    BigDecimal price;

    String[] section = stockFromFile.trim().split(",");
    symbol = section[0];
    name = section[1];
    try {
      price = new BigDecimal(section[2]);
    }  catch (NumberFormatException e) {
      throw new NumberFormatException("Price in file is not a number");
    }
    ArrayList<BigDecimal> priceHistory = new ArrayList<>();
    priceHistory.add(price);
    return new Stock(symbol, name, priceHistory);
  }

  public ArrayList<Stock> getStocksFromFile(Path filePath) throws IOException {
    ArrayList<Stock> stocks = new ArrayList<>();
    for (String section : readFile(filePath)) {
      stocks.add(concvertStringToStock(section));
    }
    return stocks;
  }
}
