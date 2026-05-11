package windows;

import archive.TransactionArchive;
import java.util.ArrayList;
import java.util.List;
import model.exchange.Exchange;
import model.player.Player;
import model.stock.Share;
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
}
