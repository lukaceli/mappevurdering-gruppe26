package model.calculator;

import java.math.BigDecimal;

public interface TransactionCalculator {
  BigDecimal calculateGross();
  BigDecimal calculateTax();
  BigDecimal calculateTotal();
  BigDecimal calculateCommission();
  }


