package windows.trade.buy;

import java.math.BigDecimal;
import model.exchange.Exchange;
import model.player.Player;
import model.stock.Stock;
import windows.trade.TradeWindow;

/**
 * Creates a buy window from transaction window which allows the player to buy specified stock.
 */
public class BuyWindow extends TradeWindow {

  private BuyController controller;

  /**
   * Super method from TradeWindow.
   *
   * @param player player object.
   */
  public BuyWindow(Player player) {
    super(player);
  }

  @Override
  protected String getActionLabel() {
    return "Buy: ";
  }

  @Override
  protected String getActionButtonText() {
    return "Buy";
  }

  @Override
  protected String getActionButtonStyle() {
    return "-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold;"
        + " -fx-font-size: 16px; -fx-cursor: hand;";
  }

  @Override
  protected void initController(Stock stock, Exchange exchange, Player player) {
    controller = new BuyController(this, stock, exchange, player);
  }

  @Override
  protected void onAmountClicked(String text) {
    controller.onAmountBtnClicked(text);
  }

  @Override
  protected void onMaxBtnClicked() {
    BigDecimal maxBuy = controller.onMaxBtnClicked();
    getAmountField().setText(maxBuy.toString());
    controller.onAmountBtnClicked(maxBuy.toString());

  }

  @Override
  protected void onActionClicked() {
    controller.onBuyBtnClicked();
  }

  /**
   * Sets confirmation text on error.
   */
  public void setConfirmationErrorMessage() {
    super.setConfirmationErrorMessage("Insufficient Balance");
  }

  /**
   * Sets confirmation text on success.
   */
  public void setConfirmationSuccessMessage() {
    super.setConfirmationSuccessMessage("Purchase successful!");
  }
}