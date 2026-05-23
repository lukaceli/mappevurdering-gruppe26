package model.calculator;

import java.math.BigDecimal;

/**
 * Defines the contract for calculating the costs or proceeds of a financial transaction.
 */
public interface TransactionCalculator {

  /**
   * Calculates the gross amount before commission and tax.
   *
   * @return the gross amount
   */
  BigDecimal calculateGross();

  /**
   * Calculates the commission for the transaction.
   *
   * @return the commission amount
   */
  BigDecimal calculateCommission();

  /**
   * Calculates the tax for the transaction.
   *
   * @return the tax amount
   */
  BigDecimal calculateTax();

  /**
   * Calculates the total amount after applying commission and tax.
   *
   * @return the total amount
   */
  BigDecimal calculateTotal();
}