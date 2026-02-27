package io;

import model.stock.Stock;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class CsvWriter {

  public void writeStocksToFile(ArrayList<Stock> stocks, Path filePath) throws IOException {
    try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
      writer.write("# Symbol,Name,Price");
      writer.newLine();
      writer.newLine();
      for (Stock stock : stocks) {
        writer.write(stock.getSymbol() + "," + stock.getName() + "," + stock.getCurrentPrice());
        writer.newLine();
      }
  } catch (IOException e) {
    throw new IOException("Could not write to specified file" + e.getMessage(), e);}
  }

  //Bare for testing
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
