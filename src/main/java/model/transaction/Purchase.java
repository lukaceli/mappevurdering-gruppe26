package model.transaction;

import execeptions.DobbleCommitException;
import execeptions.InsufficientBalanceException;
import model.calculator.TransactionCalculator;
import model.player.Player;
import model.stock.Share;

import java.math.BigDecimal;

public class Purchase extends Transaction {


  public Purchase(Share share, int week, TransactionCalculator calculator) {
    super(share, week, calculator);
  }

  @Override
  public void commit(Player player) {
    if (!commited) {
      BigDecimal purchasePriceTotal = calculator.calculateTotal();
      if (player.getBalance().compareTo(purchasePriceTotal) >= 0) {
        player.withdrawMoney(purchasePriceTotal);
        player.getPortfolio().addShare(share);
        commited = true;
      } else  {
        throw new InsufficientBalanceException("Not enough balance");
      }
    } else {
      throw new DobbleCommitException("Purchase is already committed.");
    }
  }
}
