package windows.trade.buy;

import exceptions.InsufficientBalanceException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.atomic.AtomicReference;
import model.calculator.PurchaseCalculator;
import model.exchange.Exchange;
import model.player.Player;
import model.stock.Share;
import model.stock.Stock;
import model.transaction.Purchase;
import model.transaction.TransactionFactory;

/**
 * Controller for the buy window, handling share quantity input,
 * cost calculations, and purchase execution.
 */
public class BuyController {

  private final Exchange exchange;
  private final Stock stock;
  private final BuyWindow window;
  private final AtomicReference<Share> share;
  private final AtomicReference<PurchaseCalculator> calculator;
  private final Player player;

  /**
   * Constructs a {@code BuyController} and initialises the default share quantity to 1.
   *
   * @param window   the buy window this controller manages
   * @param stock    the stock being purchased
   * @param exchange the exchange on which the purchase is made
   * @param player   the player making the purchase
   */
  public BuyController(BuyWindow window, Stock stock, Exchange exchange, Player player) {
    this.share = new AtomicReference<>();
    this.player = player;
    this.stock = stock;
    this.exchange = exchange;
    this.window = window;
    BigDecimal price = stock.getCurrentPrice();
    share.set(new Share(stock, new BigDecimal(1), price));
    calculator = new AtomicReference<>(new PurchaseCalculator(share.get()));
    onAmountBtnClicked("1");
  }

  /**
   * Updates the share quantity and recalculates commission, tax, and total cost.
   *
   * @param amount the desired quantity as a string
   */
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

  /**
   * Calculates the maximum number of shares the player can afford at the current price.
   *
   * @return the maximum purchasable quantity
   */
  public BigDecimal onMaxBtnClicked() {
    BigDecimal totalPrice = new PurchaseCalculator(share.get()).calculateTotal();
    return player.getBalance().divide(totalPrice, 0, RoundingMode.DOWN);
  }

  /**
   * Executes the purchase transaction and updates the window with the result.
   * Shows a success message and updates the displayed balance on success,
   * or an error message if the player has insufficient balance.
   */
  public void onBuyBtnClicked() {
    Purchase purchase;
    try {
      purchase = TransactionFactory.createPurchase(share.get(), exchange.getWeek());
    } catch (NumberFormatException e) {
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