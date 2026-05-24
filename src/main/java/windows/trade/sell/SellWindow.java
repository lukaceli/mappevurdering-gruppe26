package windows.trade.sell;

import java.math.BigDecimal;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import model.exchange.Exchange;
import model.player.Player;
import model.stock.Share;
import model.stock.Stock;
import windows.trade.TradeWindow;

/**
 * Creates a sell window from transaction window which allows the player to sell a specified stock.
 */
public class SellWindow extends TradeWindow {

  private SellController controller;
  private Share portfolioShare;
  private Runnable onAfterSell;

  /**
   * Constructor from trade window.
   *
   * @param player player object.
   */
  public SellWindow(Player player) {
    super(player);
  }

  /**
   * Creates the sell window.
   *
   * @param share The share you want to sell from.
   * @param parent Portefolio window.
   * @param exchange The exchange the stock is from.
   * @param onAfterSell A runback call.
   * @return Returns the sell window.
   */
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
    return "-fx-background-color: #e74c3c; -fx-text-fill: white; "
            + "-fx-font-weight: bold; -fx-font-size: 16px; -fx-cursor: hand;";
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

  /**
   * Sets confirmation on successful sale.
   */
  public void setConfirmationSuccessMessage() {
    super.setConfirmationSuccessMessage("Sale successful!");
  }
}
