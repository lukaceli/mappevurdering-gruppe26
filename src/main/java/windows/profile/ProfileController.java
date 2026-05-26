package windows.profile;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import model.calculator.SaleCalculator;
import model.calculator.ShareCalculator;
import model.exchange.Exchange;
import model.player.Player;
import model.stock.Share;
import model.transaction.Purchase;
import model.transaction.Sale;
import model.transaction.Transaction;

/**
 * Controller for the profile window, providing data and actions
 * related to the player's portfolio and transaction history.
 */
public class ProfileController {

  private final Player player;
  private final Exchange exchange;

  /**
   * Constructs a {@code ProfileController} for the given player and exchange.
   *
   * @param player   the current player
   * @param exchange the exchange used to execute sell transactions
   */
  public ProfileController(Player player, Exchange exchange) {
    this.player = player;
    this.exchange = exchange;
  }

  /**
   * Returns all shares currently held in the player's portfolio.
   *
   * @return a list of {@link Share} objects
   */
  public List<Share> getShares() {
    return player.getPortfolio().getShares();
  }

  /**
   * Sells the given share and records the transaction in the player's archive.
   *
   * @param share the share to sell
   */
  public void sellShare(Share share) {
    exchange.sell(share, player);
  }

  /**
   * Returns all transactions in the player's transaction archive.
   *
   * @return a list of all {@link Transaction} objects
   */
  public List<Transaction> getAllTransactions() {
    return player.getTransactionArchive().getAll();
  }

  /**
   * Returns a string describing the type of the given transaction.
   *
   * @param transaction the transaction to classify
   * @return {@code "Sale"}, {@code "Purchase"}, or {@code null} if unrecognised
   */
  public String getTransactionType(Transaction transaction) {
    if (transaction instanceof Sale) {
      return "Sale";
    } else if (transaction instanceof Purchase) {
      return "Purchase";
    }
    return null;
  }

  /**
   * Returns the absolute value change of the given share since purchase.
   *
   * @param share the share to evaluate
   * @return the absolute value change
   */
  public BigDecimal valueChangePerShare(Share share) {
    return new ShareCalculator(share).calculateValueChange();
  }

  /**
   * Returns the percentage value change of the given share since purchase.
   *
   * @param share the share to evaluate
   * @return the percentage change
   */
  public BigDecimal percentageChangePerShare(Share share) {
    return new ShareCalculator(share).calculatePercentageChange();
  }

  /**
   * Returns the total current market value of the given share position.
   *
   * @param share the share position to evaluate
   * @return the total market value
   */
  public BigDecimal totalShareValue(Share share) {
    return ShareCalculator.calculateTotalShareValue(share);
  }

  /**
   * Returns the player's total net worth including cash and portfolio value.
   *
   * @return the total account value
   */
  public BigDecimal totalAccountValue() {
    return player.getNetWorth();
  }

  /**
   * Returns the absolute change in net worth compared to the starting balance.
   *
   * @return the overall value change
   */
  public BigDecimal totalOverallValueChange() {
    return player.getNetWorth().subtract(player.getStartingBalance());
  }

  /**
   * Returns the percentage change in net worth compared to the starting balance,
   * rounded to 4 decimal places using {@link RoundingMode#HALF_UP}.
   * Returns zero if the starting balance is zero.
   *
   * @return the overall percentage change
   */
  public BigDecimal totalOverallPercentageChange() {
    if (player.getStartingBalance().compareTo(BigDecimal.ZERO) == 0) {
      return BigDecimal.ZERO;
    }
    return player.getNetWorth().subtract(player.getStartingBalance())
            .divide(player.getStartingBalance(), 4, RoundingMode.HALF_UP)
            .multiply(new BigDecimal(100));
  }

  /**
   * Returns the total proceeds if all shares in the portfolio were sold now.
   *
   * @return the total sell-all proceeds
   */
  public BigDecimal totalSellAllProceeds() {
    BigDecimal total = BigDecimal.ZERO;
    for (Share share : player.getPortfolio().getShares()) {
      total = total.add(new SaleCalculator(share).calculateTotal());
    }
    return total;
  }

  /**
   * Sells all shares currently held in the player's portfolio.
   */
  public void sellAll() {
    for (Share share : new ArrayList<>(player.getPortfolio().getShares())) {
      sellShare(share);
    }
  }
}