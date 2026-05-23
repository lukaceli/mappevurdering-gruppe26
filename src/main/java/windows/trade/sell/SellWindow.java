package windows.trade.sell;

import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import model.exchange.Exchange;
import model.player.Player;
import model.stock.Share;
import model.stock.Stock;
import windows.trade.TradeWindow;

import java.math.BigDecimal;

public class SellWindow extends TradeWindow {

  private SellController controller;
  private Share portfolioShare;
  private Runnable onAfterSell;

  public SellWindow(Player player) {
    super(player);
  }

  public VBox createFromPortfolio(Share share, StackPane parent,
      Exchange exchange, Runnable onAfterSell) {
    this.portfolioShare = share;
    this.onAfterSell = onAfterSell;
    return create(share.stock(), parent, exchange);
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
  protected void initController(Stock stock, Exchange exchange, Player player) {
    controller = new SellController(this, stock, exchange, player, portfolioShare, onAfterSell);
  }

  @Override
  protected void onAmountClicked(String text) {
    controller.onAmountBtnClicked(text);
  }

  @Override
  protected void onMaxBtnClicked() {
    BigDecimal maxSell = controller.onMaxBtnClicked();
    getAmountField().setText(maxSell.toString());
    controller.onAmountBtnClicked(maxSell.toString());
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
