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
  private final Stock stock;
  private final Exchange exchange;
  private final SellWindow window;
  private final Player player;
  private Share portfolioShare;
  private Share currentSellShare;
  private final Runnable onAfterSell;

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

  public BigDecimal onMaxBtnClicked() {
    if (portfolioShare == null) return BigDecimal.ZERO;
    return portfolioShare.quantity();
  }

  protected void onSellBtnClicked() {
    if (currentSellShare == null || portfolioShare == null) {
      window.setAmountErrorMessage("Please enter a valid amount");
      return;
    }
    BigDecimal soldQty = currentSellShare.quantity();
    BigDecimal ownedQty = portfolioShare.quantity();

    Transaction sale;
    if (soldQty.compareTo(ownedQty) < 0) {
      BigDecimal remaining = ownedQty.subtract(soldQty);
      player.getPortfolio().removeShare(portfolioShare);
      Share remainingShare = new Share(stock, remaining, portfolioShare.purchasePrice());
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
