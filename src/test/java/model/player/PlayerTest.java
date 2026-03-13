package model.player;

import execeptions.InsufficientBalanceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
  Player player;

  @BeforeEach
  void setUp() {
    player = new Player("Subject", new BigDecimal("10000"));

  }

  @Test
  void withdrawMoneyThrowsInsufficientBalance() {
    assertThrows(InsufficientBalanceException.class, () -> player.withdrawMoney(new BigDecimal("10000.1")));
  }

  @Test
  void getNetWorth() {
  }

  @Test
  void updateStatusIfNeededBecomesInvestor() {
    player.addMoney(new BigDecimal("20"));
    player.updateStatusIfNeeded(10);
    assertEquals("Investor", player.getStatus());
  }

  @Test
  void updateStatusIfNeededBecomesSpeculator() {
    player.addMoney(new BigDecimal("100"));
    player.updateStatusIfNeeded(20);
    assertEquals("Speculator", player.getStatus());
  }
}