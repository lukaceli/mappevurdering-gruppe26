package windows;

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
  private Exchange exchange;
  private Stock stock;
  private BuyWindow window;
  private final AtomicReference<Share> share;
  private final AtomicReference<PurchaseCalculator> calculator;
  private Player player;

  public BuyController(BuyWindow window, Stock stock, Exchange exchange) {
    share = new AtomicReference<>();
    //temporary player obj
    player = new Player("hei", new BigDecimal(10000));
    this.stock = stock;
    this.exchange = exchange;
    this.window = window;
    BigDecimal price = stock.getCurrentPrice();
    share.set(new Share(stock, new BigDecimal(1), price));
    calculator = new AtomicReference<>(new PurchaseCalculator(share.get()));
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
