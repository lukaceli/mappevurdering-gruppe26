package windows.trade;

import model.exchange.Exchange;
import model.player.Player;
import model.stock.Stock;

public class SellWindow extends TradeWindow {

  private SellController controller;

  public SellWindow(Player player) {
    super(player);
  }

  @Override
  protected String getActionLabel() {
    return "Sell: ";
  }

  @Override
  protected String getActionButtonText() {
    return "Sell";
  }

  @Override
  protected String getActionButtonStyle() {
    return "-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px; -fx-cursor: hand;";
  }

  @Override
  protected void initController(Stock stock, Exchange exchange,  Player player) {
    controller = new SellController(this, stock, exchange);
  }

  @Override
  protected void onAmountClicked(String text) {
    controller.onAmountBtnClicked(text);
  }

  @Override
  protected void onActionClicked() {
    controller.onSellBtnClicked();
  }

  public void setConfirmationErrorMessage() {
    super.setConfirmationErrorMessage("Insufficient shares");
  }


  public void setConfirmationSuccessMessage() {
    super.setConfirmationSuccessMessage("Sale successful!");
  }
}