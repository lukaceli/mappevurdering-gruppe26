package model.stock;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class StockTest {
  private ArrayList<BigDecimal> prices = new ArrayList<>();
  private Stock stock = new Stock("AAPL", "Apple", prices);

  @Test
  void getPriceHistory() {
    prices.add(new BigDecimal("2"));
    prices.add(new BigDecimal("3"));
    assertEquals(2, prices.size());
  }

  @Test
  void setNewPrice() {


  }

  @Test
  void addNewSalePrice() {
    stock.addNewSalePrice(new BigDecimal("3"));
    assertEquals(new BigDecimal("3"), prices.get(0));
  }

  @Test
  void getCurrentPrice() {
    stock.addNewSalePrice(new BigDecimal("3"));
    stock.addNewSalePrice(new BigDecimal("2"));
    assertEquals(new BigDecimal("2"), prices.getLast());
  }

  @Test
  void getHighestPrice() {
    stock.addNewSalePrice(new BigDecimal("3"));
    stock.addNewSalePrice(new BigDecimal("2"));
    assertEquals(new BigDecimal("3"), stock.getHighestPrice());
  }

  @Test
  void getLowestPrice() {
    stock.addNewSalePrice(new BigDecimal("3"));
    stock.addNewSalePrice(new BigDecimal("2"));
    assertEquals(new BigDecimal("2"), stock.getLowestPrice());
  }

  @Test
  void getLatestPriceChange() {
    stock.addNewSalePrice(new BigDecimal("3"));
    stock.addNewSalePrice(new BigDecimal("2"));
    assertEquals(new BigDecimal("-1"), stock.getLatestPriceChange());
  }

  @Test
  void getLatestPriceChangeIfPriceSizeIsLessThanTwo() {
    stock.addNewSalePrice(new BigDecimal("3"));
    assertEquals(new BigDecimal("0"), stock.getLatestPriceChange());
  }

  @Test
  void getLatestPriceChangeIfDifferenceIsZero() {
    stock.addNewSalePrice(new BigDecimal("3"));
    assertEquals(new BigDecimal("0"), stock.getLatestPriceChange());
  }



  @Test
  void getLatestPercentageChange() {
    stock.addNewSalePrice(new BigDecimal("2"));
    stock.addNewSalePrice(new BigDecimal("3"));
    assertEquals(new BigDecimal("50.00"), stock.getLatestPercentageChange());
  }


}