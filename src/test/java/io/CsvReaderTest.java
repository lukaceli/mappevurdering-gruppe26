package io;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.math.BigDecimal;
import java.util.ArrayList;
import model.stock.Stock;

import static org.junit.jupiter.api.Assertions.*;

class CsvReaderTest {
  private CsvReader csvReader;
  private Path testFilePath;

  @BeforeEach
  void setUp() {
    testFilePath = Path.of("src/main/resources/S&P500Stocks.csv");
    csvReader = new CsvReader(testFilePath);
  }

  @Test
  @DisplayName("Test path works")
  void testReadFile() throws IOException {
    ArrayList<String> lines = csvReader.readFile();
    assertNotNull(lines);
  }

  @Test
  @DisplayName("Tests that a single string is correctly converted to a Stock object")
  void testConvertStringToStock() {
    String testLine = "NVDA,Nvidia,191.27";
    Stock result = csvReader.convertStringToStock(testLine);
    assertNotNull(result, "Stock should not be null");
    assertEquals("NVDA", result.getSymbol());
    assertEquals("Nvidia", result.getName());

    BigDecimal expectedPrice = new BigDecimal("191.27");
    assertEquals(expectedPrice, result.getPriceHistory().getFirst(), "Price should be correctly converted to BigDecimal and added to history");
  }

  @Test
  void testGetStocksFromFile() throws IOException {
    ArrayList<Stock> result = csvReader.getStocksFromFile();

    assertNotNull(result);
    assertEquals("NVDA", result.get(0).getSymbol());
    assertEquals(new BigDecimal("191.27"), result.get(0).getPriceHistory().get(0));
  }

  @Test
  void testInvalidPriceFormat() {
    assertThrows(NumberFormatException.class, () -> {
      new BigDecimal("102.g1");
    });
  }

  @Test
  void testReadFileKeepsName() throws IOException {
    ArrayList<String> lines = csvReader.readFile();
    assertNotNull(lines);
    assertTrue(lines.contains("GOOGL,Alphabet Inc. (Class A),311.20"));
  }
}