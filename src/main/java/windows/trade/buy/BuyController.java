package windows.trade.buy;

import execeptions.InsufficientBalanceException;
import model.calculator.PurchaseCalculator;
import model.exchange.Exchange;
import model.player.Player;
import model.stock.Share;
import model.stock.Stock;
import model.transaction.Purchase;
import model.transaction.TransactionFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
    this.player = player;
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
    window.setTaxPrice(calculator.get().calculateTax().toString());
    window.setTotalPrice(calculator.get().calculateTotal().toString());
    window.setAmount(amount);
  }

  public BigDecimal onMaxBtnClicked() {
    BigDecimal totalPrice = new PurchaseCalculator(share.get()).calculateTotal();
    return player.getBalance().divide(totalPrice, 0, RoundingMode.DOWN);
  }


  public void onBuyBtnClicked() {
    Purchase purchase;
    try {
      purchase = TransactionFactory.createPurchase(share.get(), exchange.getWeek());
    } catch (NumberFormatException _) {
      window.setAmountErrorMessage("Please enter a valid amount");
      return;
    }
    try {
      purchase.commit(player);
      player.getTransactionArchive().add(purchase);
      window.setConfirmationSuccessMessage();
      window.setBalance(String.format("%.2f", player.getBalance()));
      window.notifyAfterTrade();
    } catch (InsufficientBalanceException ex) {
      window.setConfirmationErrorMessage();
    }
  }
}
