package model.stock;

import java.math.BigDecimal;

/**
 * Represents a position in a stock, defined by the stock itself,
 * the quantity held, and the price at which it was purchased.
 *
 * @param stock         the stock this share position belongs to
 * @param quantity      the number of shares held, must be greater than zero
 * @param purchasePrice the price per share at the time of purchase
 */
public record Share(Stock stock, BigDecimal quantity, BigDecimal purchasePrice) {

  /**
   * Compact constructor that validates the share fields.
   *
   * @throws NullPointerException     if {@code stock} is null
   * @throws IllegalArgumentException if {@code quantity} is null or not greater than zero
   */
  public Share {
    if (stock == null) {
      throw new NullPointerException("Stock cannot be null");
    }
    if (quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Quantity cannot be negative or null");
    }
  }
}