package windows;



import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import model.calculator.ShareCalculator;
import model.exchange.Exchange;
import model.player.Player;
import model.stock.Share;
import model.transaction.Purchase;
import model.transaction.Sale;
import model.transaction.Transaction;

public class PortefolioController {
  private Player player;
  private Exchange exchange;


  public PortefolioController(Player player, Exchange exchange) {
    this.player = player;
    this.exchange = exchange;
  }

  public List<Share> getShares() {
    return player.getPortfolio().getShares();
  }

  public void sellShare(Share share) {
    Transaction soldShare = exchange.sell(share, player);
    player.getTransactionArchive().add(soldShare);
  }

  public List<Transaction> getAllTransactions() {
    return player.getTransactionArchive().getAll();
  }

  public String getTransactionType(Transaction transaction) {
    if (transaction instanceof Sale) {
      return "Sale";
    }
    else if (transaction instanceof Purchase) {
      return "Purchase";
    }
    else {
      return null;
    }
  }

  public BigDecimal valueChangePerShare(Share share) {
    return new ShareCalculator(share).calculateValueChange();
  }

  public BigDecimal percentageChangePerShare(Share share) {
    return new ShareCalculator(share).calculatePercentageChange();
  }

  public BigDecimal totalPortfolioPercentageChange() {
    return ShareCalculator.calculateTotalPercentageChange(player.getPortfolio(),
        player.getStartingBalance());
  }

  public BigDecimal totalValueChange() {
    return ShareCalculator.calculateTotalValueChange(player.getPortfolio());
  }

  public BigDecimal totalShareValue(Share share) {
    return ShareCalculator.calculateTotalShareValue(share);
  }

  public BigDecimal totalAccountValue() {
    return player.getNetWorth();
  }

  public BigDecimal totalOverallValueChange() {
    return player.getNetWorth().subtract(player.getStartingBalance());
  }

  public BigDecimal totalOverallPercentageChange() {
    if (player.getStartingBalance().compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO;
    return player.getNetWorth().subtract(player.getStartingBalance())
        .divide(player.getStartingBalance(), 4, RoundingMode.HALF_UP)
        .multiply(new BigDecimal(100));
  }

  public void sellAll() {
    for (Share share : new ArrayList<>(player.getPortfolio().getShares())) {
      sellShare(share);
    }
  }
}


