package windows.trade;

import model.calculator.SaleCalculator;
import model.exchange.Exchange;
import model.player.Player;
import model.stock.Share;
import model.stock.Stock;
import model.transaction.Transaction;
import model.transaction.TransactionFactory;

import java.math.BigDecimal;

public class SellController {
  private Stock stock;
  private Exchange exchange;
  private SellWindow window;
  private Player player;
  private Share portfolioShare;
  private Share currentSellShare;
  private Runnable onAfterSell;

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

  protected void onAmountBtnClicked(String amount) {
    if (portfolioShare == null) {
      window.setAmountErrorMessage("No shares left to sell");
      return;
    }
    try {
      window.setAmountErrorMessage("");
      BigDecimal qty = new BigDecimal(amount);
      if (qty.compareTo(portfolioShare.getQuantity()) > 0) {
        window.setAmountErrorMessage(
            "Cannot sell more than you own (" + portfolioShare.getQuantity() + ")");
        return;
      }
      currentSellShare = new Share(stock, qty, portfolioShare.getPurchasePrice());
    } catch (IllegalArgumentException ex) {
      window.setAmountErrorMessage("Please enter a valid amount");
      return;
    }
    SaleCalculator calc = new SaleCalculator(currentSellShare);
    window.commisionSetPrice(calc.calculateCommission().toString());
    window.setTotalPrice(calc.calculateTotal().toString());
    window.setAmount(amount);
  }

  public BigDecimal onMaxBtnClicked() {
    if (portfolioShare == null) return BigDecimal.ZERO;
    return portfolioShare.getQuantity();
  }

  protected void onSellBtnClicked() {
    if (currentSellShare == null || portfolioShare == null) {
      window.setAmountErrorMessage("Please enter a valid amount");
      return;
    }
    BigDecimal soldQty = currentSellShare.getQuantity();
    BigDecimal ownedQty = portfolioShare.getQuantity();

    Transaction sale;
    if (soldQty.compareTo(ownedQty) < 0) {
      BigDecimal remaining = ownedQty.subtract(soldQty);
      player.getPortfolio().removeShare(portfolioShare);
      Share remainingShare = new Share(stock, remaining, portfolioShare.getPurchasePrice());
      player.getPortfolio().addShare(remainingShare);
      sale = TransactionFactory.createSale(currentSellShare, exchange.getWeek());
      portfolioShare = remainingShare;
    } else {
      sale = TransactionFactory.createSale(portfolioShare, exchange.getWeek());
      portfolioShare = null;
    }

    currentSellShare = null;
    sale.commit(player);
    player.getTransactionArchive().add(sale);

    window.setConfirmationSuccessMessage();
    window.setBalance(String.format("%.2f", player.getBalance()));
    if (onAfterSell != null) onAfterSell.run();
  }
}
