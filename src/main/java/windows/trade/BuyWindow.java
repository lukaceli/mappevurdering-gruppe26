package windows.trade;

import model.exchange.Exchange;
import model.player.Player;
import model.stock.Stock;

public class BuyWindow extends TradeWindow {

  private BuyController controller;

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
    return "-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px; -fx-cursor: hand;";
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
  protected void onActionClicked() {
    controller.onBuyBtnClicked();
  }

  public void setConfirmationErrorMessage() {
    super.setConfirmationErrorMessage("Insufficient Balance");
  }

  public void setConfirmationSuccessMessage() {
    super.setConfirmationSuccessMessage("Purchase successful!");
  }
}