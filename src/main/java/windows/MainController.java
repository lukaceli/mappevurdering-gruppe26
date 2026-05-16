package windows;

import model.appState.AppState;
import model.exchange.Exchange;
import model.exchange.ExchangeList;
import model.player.Player;

import java.io.IOException;

public class MainController {
  private final ExchangeList exchangeList;
  private final AppState appState;

  public MainController(AppState appState, ExchangeList exchangeList) {
    this.appState = appState;
    this.exchangeList = exchangeList;
  }

  public ExchangeWindow createExchangeWindow(Player player, Runnable onAfterAdvance) {
    return new ExchangeWindow(exchangeList, appState, player, () -> {
      advanceAllExchanges();
      onAfterAdvance.run();
    }, onAfterAdvance);
  }

  public PortefolioWindow createPortefolioWindow(Player player, Runnable onBalanceUpdate) {
    Exchange firstExchange = exchangeList.getExchanges().isEmpty() ? null : exchangeList.getExchanges().getFirst();
    return new PortefolioWindow(player, firstExchange, onBalanceUpdate);
  }

  public void advanceAllExchanges() {
    for (Exchange exchange : exchangeList.getExchanges()) {
      exchange.advance();
    }
    appState.getSelectedPlayer().updateStatusIfNeeded(exchangeList.getExchanges().getFirst().
        getWeek());
  }

  public int getWeek() {
    return exchangeList.getExchanges().getFirst().getWeek();
  }

}