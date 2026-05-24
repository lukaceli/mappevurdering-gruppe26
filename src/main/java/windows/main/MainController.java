package windows.main;

import model.appstate.AppState;
import model.exchange.Exchange;
import model.exchange.ExchangeList;
import model.player.Player;
import windows.exchange.ExchangeWindow;
import windows.profile.ProfileWindow;

/**
 * Controller for the main window, responsible for creating sub-windows
 * and advancing the simulation.
 */
public class MainController {

  private final ExchangeList exchangeList;
  private final AppState appState;

  /**
   * Constructs a {@code MainController} with the given application state and exchange list.
   *
   * @param appState     the shared application state
   * @param exchangeList the list of available exchanges
   */
  public MainController(AppState appState, ExchangeList exchangeList) {
    this.appState = appState;
    this.exchangeList = exchangeList;
  }

  /**
   * Creates and returns an {@link ExchangeWindow} for the given player.
   *
   * When the week is advanced, all exchanges are updated and {@code onAfterAdvance} is called.
   *
   * @param player         the current player
   * @param onAfterAdvance a callback to run after each week is advanced
   * @return a new {@link ExchangeWindow}
   */
  public ExchangeWindow createExchangeWindow(Player player, Runnable onAfterAdvance) {
    return new ExchangeWindow(exchangeList, appState, player, () -> {
      advanceAllExchanges();
      onAfterAdvance.run();
    }, onAfterAdvance);
  }

  /**
   * Creates and returns a {@link ProfileWindow} for the given player.
   *
   * Uses the first exchange in the list, or {@code null} if no exchanges are available.
   *
   * @param player          the current player
   * @param onBalanceUpdate a callback to run when the balance is updated
   * @return a new {@link ProfileWindow}
   */
  public ProfileWindow createPortfolioWindow(Player player, Runnable onBalanceUpdate) {
    Exchange firstExchange = exchangeList.getExchanges().isEmpty()
            ? null
            : exchangeList.getExchanges().getFirst();
    return new ProfileWindow(player, firstExchange, onBalanceUpdate);
  }

  /**
   * Advances all exchanges by one week and updates the player's status.
   */
  public void advanceAllExchanges() {
    for (Exchange exchange : exchangeList.getExchanges()) {
      exchange.advance();
    }
    Player player = appState.getSelectedPlayer();
    player.updateStatusIfNeeded(player.getTransactionArchive().countDistinctWeeks());
  }

  /**
   * Returns the current week number from the first exchange.
   *
   * @return the current week
   */
  public int getWeek() {
    return exchangeList.getExchanges().getFirst().getWeek();
  }
}