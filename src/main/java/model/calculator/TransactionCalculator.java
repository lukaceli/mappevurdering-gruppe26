package model.calculator;

import model.appState.Difficulty;

import java.math.BigDecimal;

public interface TransactionCalculator {
  BigDecimal calculateGross();
  BigDecimal calculateTax();
  BigDecimal calculateTotal();

  default BigDecimal calculateCommission() {
    BigDecimal commissionRate;
    Difficulty difficulty = Difficulty.getDifficulty();
    commissionRate = switch (difficulty) {
      case EASY -> BigDecimal.ZERO;
      case NORMAL -> new BigDecimal("0.005");
      case HARD -> new BigDecimal("0.01");
    };
    return calculateGross().multiply(commissionRate);
  }
  }


