import calculator.PurchaseCalculator;
import model.Player;
import model.Portfolio;
import model.Purchase;
import model.Share;
import model.Stock;

void main() {

  Portfolio portfolio = new Portfolio();
  Player player = new Player("Test", new BigDecimal("100"));
  ArrayList<BigDecimal> applePrices = new ArrayList<>();
  applePrices.add(new BigDecimal("101.2"));
  applePrices.add(new BigDecimal("103.32"));
  Stock apple = new Stock("APPL", "Apple", applePrices);
  Share appleShare = new Share(apple, new BigDecimal("10.1"), apple.getSalePrice());
  Purchase purchase = new Purchase(appleShare, 1, new PurchaseCalculator(appleShare));
  purchase.commit(player);
  portfolio.addShare(appleShare);
  System.out.println(appleShare.getPurchasePrice());
  System.out.println(portfolio.getShares());
  System.out.println(player.getBalance());

}

