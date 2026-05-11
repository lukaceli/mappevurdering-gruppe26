package windows.trade;

import javafx.stage.Window;
import model.exchange.Exchange;
import model.stock.Stock;

public class SellController {
  private Stock stock;
  private Exchange exchange;
  private SellWindow window;

  public SellController(SellWindow window, Stock stock, Exchange exchange) {
    this.stock = stock;
    this.exchange = exchange;
    this.window = window;
  }
  protected void onAmountBtnClicked(String amount) {

  }

  protected void onSellBtnClicked() {

  }
}
