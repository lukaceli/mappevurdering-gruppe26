package windows.trade;

import model.exchange.Exchange;
import model.stock.Stock;

public class BuyWindow extends TradeWindow {

  private BuyController controller;

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
  protected void initController(Stock stock, Exchange exchange) {
    controller = new BuyController(this, stock, exchange);
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