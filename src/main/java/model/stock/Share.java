package model.stock;

import java.math.BigDecimal;

public class Share {
  private Stock stock;
  private BigDecimal quantity;
  private BigDecimal purchasePrice;

  public Share(Stock stock, BigDecimal quantity, BigDecimal purchasePrice) {
    if (stock == null) {
      throw new IllegalArgumentException("Stock cannot be null");
    }
   if (quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0) {
     throw new IllegalArgumentException("Quantity cannot be negative or null");
   }
    this.stock = stock;
    this.quantity = quantity;
    this.purchasePrice = purchasePrice;
  }

  public Stock getStock() {
    return stock;
  }

  public BigDecimal getQuantity() {
    return quantity;
  }

  public BigDecimal getPurchasePrice() {
    return purchasePrice;
  }
}
