package windows.trade.sell;

import java.math.BigDecimal;
import model.calculator.SaleCalculator;
import model.exchange.Exchange;
import model.player.Player;
import model.stock.Share;
import model.stock.Stock;
import model.transaction.Transaction;
import model.transaction.TransactionFactory;

/**
 * Controller for the sell window, handling share quantity input,
 * proceeds calculations, and sale execution.
 */
public class SellController {

  private final Stock stock;
  private final Exchange exchange;
  private final SellWindow window;
  private final Player player;
  private final Runnable onAfterSell;
  private Share portfolioShare;
  private Share currentSellShare;

  /**
   * Constructs a {@code SellController} and initialises the default sell quantity to 1.
   *
   * @param window         the sell window this controller manages
   * @param stock          the stock being sold
   * @param exchange       the exchange on which the sale is made
   * @param player         the player making the sale
   * @param portfolioShare the share position held by the player
   * @param onAfterSell    a callback to run after a successful sale
   */
  public SellController(SellWindow window, Stock stock, Exchange exchange,
                        Player player, Share portfolioShare, Runnable onAfterSell) {
    this.stock = stock;
    this.exchange = exchange;
    this.window = window;
    this.player = player;
    this.portfolioShare = portfolioShare;
    this.onAfterSell = onAfterSell;
    onAmountBtnClicked("1");
    window.setBalance(String.format("%.2f", player.getBalance()));
  }

  /**
   * Updates the sell quantity and recalculates commission, tax, and total proceeds.
   * Shows an error if the quantity exceeds the player's holdings or is invalid.
   *
   * @param amount the desired sell quantity as a string
   */
  protected void onAmountBtnClicked(String amount) {
    if (portfolioShare == null) {
      window.setAmountErrorMessage("No shares left to sell");
      return;
    }
    try {
      window.setAmountErrorMessage("");
      BigDecimal qty = new BigDecimal(amount);
      if (qty.compareTo(portfolioShare.quantity()) > 0) {
        window.setAmountErrorMessage(
                "Cannot sell more than you own (" + portfolioShare.quantity() + ")");
        return;
      }
      currentSellShare = new Share(stock, qty, portfolioShare.purchasePrice());
    } catch (IllegalArgumentException ex) {
      window.setAmountErrorMessage("Please enter a valid amount");
      return;
    }
    SaleCalculator calc = new SaleCalculator(currentSellShare);
    window.commisionSetPrice(calc.calculateCommission().toString());
    window.setTaxPrice(calc.calculateTax().toString());
    window.setTotalPrice(calc.calculateTotal().toString());
    window.setAmount(amount);
  }

  /**
   * Returns the maximum quantity the player can sell, equal to their current holdings.
   * Returns zero if no shares are held.
   *
   * @return the maximum sellable quantity
   */
  public BigDecimal onMaxBtnClicked() {
    if (portfolioShare == null) {
      return BigDecimal.ZERO;
    }
    return portfolioShare.quantity();
  }

  /**
   * Executes the sale transaction and updates the window with the result.
   * If a partial quantity is sold, the remaining shares are kept in the portfolio.
   * Shows a success message and updates the displayed balance on success.
   */
  protected void onSellBtnClicked() {
    if (currentSellShare == null || portfolioShare == null) {
      window.setAmountErrorMessage("Please enter a valid amount");
      return;
    }

    portfolioShare = player.sellShare(currentSellShare, portfolioShare, exchange.getWeek());
    currentSellShare = null;

    window.setConfirmationSuccessMessage();
    window.setBalance(String.format("%.2f", player.getBalance()));
    if (onAfterSell != null) onAfterSell.run();
  }
}