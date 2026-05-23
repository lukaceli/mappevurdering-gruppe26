package io;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import model.stock.Stock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import utility.TestFactory;

class CsvWriterTest {
  private CsvWriter writer;
  private List<Stock> stocks;

  @TempDir
  Path tempDir;

  @BeforeEach
  void setUp() {
    writer = new CsvWriter();
    stocks = TestFactory.createExchange().getStocks();
  }

  @Test
  void writeStocksToFile_writesCorrectContent() throws IOException {
    Path file = tempDir.resolve("test.csv");
    writer.writeStocksToFile(stocks, file);

    List<String> lines = Files.readAllLines(file);
    assertTrue(lines.get(0).startsWith("#"));
    assertTrue(lines.stream().anyMatch(line -> line.contains("AAPL")));
    assertTrue(lines.stream().anyMatch(line -> line.contains("Apple Inc")));
  }

  @Test
  void writeStocksToFile_nullStocks_throwsException() {
    assertThrows(IllegalArgumentException.class,
        () -> writer.writeStocksToFile(null, tempDir.resolve("test.csv")));
  }

  @Test
  void writeStocksToFile_nullPath_throwsException() {
    assertThrows(IllegalArgumentException.class,
        () -> writer.writeStocksToFile(stocks, null));
  }

  @Test
  void writeStocksToFile_emptyStocks_throwsException() {
    assertThrows(IllegalArgumentException.class,
        () -> writer.writeStocksToFile(List.of(), tempDir.resolve("test.csv")));
  }
}