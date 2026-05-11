package windows.trade;

import execeptions.InsufficientBalanceException;
import model.calculator.PurchaseCalculator;
import model.exchange.Exchange;
import model.player.Player;
import model.stock.Share;
import model.stock.Stock;
import model.transaction.Purchase;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicReference;

public class BuyController {
  private final Exchange exchange;
  private final Stock stock;
  private final BuyWindow window;
  private final AtomicReference<Share> share;
  private final AtomicReference<PurchaseCalculator> calculator;
  private final Player player;

  public BuyController(BuyWindow window, Stock stock, Exchange exchange, Player player) {
    share = new AtomicReference<>();
    //temporary player obj
    this.player = player;
    this.stock = stock;
    this.exchange = exchange;
    this.window = window;
    BigDecimal price = stock.getCurrentPrice();
    share.set(new Share(stock, new BigDecimal(1), price));
    calculator = new AtomicReference<>(new PurchaseCalculator(share.get()));
    //sets 1 as starting amount
    onAmountBtnClicked("1");
  }


  public void onAmountBtnClicked(String amount) {
    try {
      window.setAmountErrorMessage("");
      share.set(new Share(stock, new BigDecimal(amount), stock.getCurrentPrice()));
    } catch (IllegalArgumentException ex) {
      window.setAmountErrorMessage("Please enter a valid amount");
      return;
    }
    calculator.set(new PurchaseCalculator(share.get()));
    window.commisionSetPrice(calculator.get().calculateCommission().toString());
    window.setTotalPrice(calculator.get().calculateTotal().toString());
    window.setAmount(amount);
  }


  public void onBuyBtnClicked() {
    Purchase purchase;
    try {
      purchase = new Purchase(share.get(), exchange.getWeek(), calculator.get());
    } catch (NumberFormatException _) {
      window.setAmountErrorMessage("Please enter a valid amount");
      return;
    }
    try {
      purchase.commit(player);
      window.setConfirmationSuccessMessage();
      window.setBalance(String.format("%.2f", player.getBalance()));
    } catch (InsufficientBalanceException ex) {
      window.setConfirmationErrorMessage();
    }
  }
}
