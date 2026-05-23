package model.player;

import execeptions.InsufficientBalanceException;
import execeptions.InvalidPlayerName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utility.TestFactory;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
  Player player;

  @BeforeEach
  void setUp() {
    player = TestFactory.createPlayer();

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
    player.addMoney(new BigDecimal("2000"));
    player.updateStatusIfNeeded(10);
    assertEquals("Investor", player.getStatus());
  }

  @Test
  void updateStatusIfNeededBecomesSpeculator() {
    player.addMoney(new BigDecimal("10000"));
    player.updateStatusIfNeeded(20);
    assertEquals("Speculator", player.getStatus());
  }

  @Test
  void withdrawMoney_shouldReduceBalance() {
    player.withdrawMoney(new BigDecimal("100"));
    assertEquals(new BigDecimal("9900"), player.getBalance());
  }

  @Test
  void addMoney_shouldIncreaseBalance() {
    player.addMoney(new BigDecimal("100"));
    assertEquals(new BigDecimal("10100"), player.getBalance());
  }

  @Test
  void getNetWorth_shouldReturnBalanceWhenNoShares() {
    assertEquals(player.getBalance(), player.getNetWorth());
  }

  @Test
  void updateStatusIfNeeded_shouldRemainNovice() {
    player.updateStatusIfNeeded(5);
    assertEquals("Novice", player.getStatus());
  }

  @Test
  void constructor_shouldThrowWhenNameTooShort() {
    assertThrows(InvalidPlayerName.class, () -> new Player("A", new BigDecimal("1000")));
  }

  @Test
  void constructor_shouldThrowWhenNameTooLong() {
    assertThrows(InvalidPlayerName.class, () -> new Player("ThisNameIsTooLong", new BigDecimal("1000")));
  }

  @Test
  void constructor_shouldThrowWhenBalanceIsZero() {
    assertThrows(IllegalArgumentException.class, () -> new Player("Test", BigDecimal.ZERO));
  }

  @Test
  void constructor_shouldThrowWhenBalanceIsNegative() {
    assertThrows(IllegalArgumentException.class, () -> new Player("Test", new BigDecimal("-100")));
  }
}