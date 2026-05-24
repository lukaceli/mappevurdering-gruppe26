package model.stock;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utility.TestFactory;

class ShareTest {
  private Share share;
  private Stock stock;



  @BeforeEach
  void setUp() {
    share = TestFactory.createAppleShare();
    stock = share.getStock();
  }

  @Test
  void constructor_throwsNullPointerExceptionIfStockIsNull() {
    assertThrows(NullPointerException.class, () -> new Share(null, new BigDecimal("10"),
        share.getStock().getCurrentPrice()));
  }

  @Test
  void constructor_throwsNullPointerExceptionIfQuantityIsNull() {
    assertThrows(IllegalArgumentException.class, () -> new Share(stock, null,
        share.getStock().getCurrentPrice()));
  }

  @Test
  void constructor_throwsNullPointerExceptionIfQuantityIsNegative() {
    assertThrows(IllegalArgumentException.class, () -> new Share(stock, new BigDecimal("-1"),
        share.getStock().getCurrentPrice()));
  }

  @Test
  void constructor_throwsNullPointerExceptionIfQuantityIsZero() {
    assertThrows(IllegalArgumentException.class, () -> new Share(stock, new BigDecimal("0"),
        share.getStock().getCurrentPrice()));
  }
}