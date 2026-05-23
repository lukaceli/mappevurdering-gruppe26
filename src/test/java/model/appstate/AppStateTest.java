package model.appstate;

import model.exchange.Exchange;
import model.exchange.ExchangeObserver;
import model.player.Player;
import model.player.PlayerObserver;
import model.stock.Stock;
import model.stock.StockObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utility.TestFactory;

import static org.junit.jupiter.api.Assertions.*;

class AppStateTest {

  private AppState appState;
  private Exchange exchange;
  private Stock stock;
  private Player player;

  //Ai used to set up fake observers.
  static class FakeStockObserver implements StockObserver {
    int callCount = 0;
    @Override public void onStockUpdate() { callCount++; }
  }

  static class FakeExchangeObserver implements ExchangeObserver {
    int callCount = 0;
    @Override public void onExchangeUpdate() { callCount++; }
  }

  static class FakePlayerObserver implements PlayerObserver {
    int callCount = 0;
    @Override public void gameStart() { callCount++; }
  }

  @BeforeEach
  void setUp() {
    appState = new AppState();
    exchange = TestFactory.createExchange();
    stock = TestFactory.getAppleStock();
    player = TestFactory.createPlayer();
  }

  @Test
  void setSelectedExchange_setsExchange() {
    appState.setSelectedExchange(exchange);
    assertEquals(exchange, appState.getSelectedExchange());
  }

  @Test
  void setSelectedExchange_setsFirstStock() {
    appState.setSelectedExchange(exchange);
    assertNotNull(appState.getSelectedStock());
  }

  @Test
  void setSelectedExchange_notifiesExchangeObservers() {
    FakeExchangeObserver observer = new FakeExchangeObserver();
    appState.addExchangeObserver(observer);

    appState.setSelectedExchange(exchange);

    assertEquals(1, observer.callCount);
  }

  @Test
  void setSelectedStock_setsStock() {
    appState.setSelectedStock(stock);
    assertEquals(stock, appState.getSelectedStock());
  }

  @Test
  void setSelectedStock_notifiesStockObservers() {
    FakeStockObserver observer = new FakeStockObserver();
    appState.addStockObserver(observer);

    appState.setSelectedStock(stock);

    assertEquals(1, observer.callCount);
  }

  @Test
  void removeStockObserver_doesNotNotifyAfterRemoval() {
    FakeStockObserver observer = new FakeStockObserver();
    appState.addStockObserver(observer);
    appState.removeStockObserver(observer);

    appState.setSelectedStock(stock);

    assertEquals(0, observer.callCount);
  }

  @Test
  void setSelectedPlayer_setsPlayer() {
    appState.setSelectedPlayer(player);
    assertEquals(player, appState.getSelectedPlayer());
  }

  @Test
  void setSelectedPlayer_notifiesPlayerObservers() {
    FakePlayerObserver observer = new FakePlayerObserver();
    appState.addPlayerObserver(observer);

    appState.setSelectedPlayer(player);

    assertEquals(1, observer.callCount);
  }

  //Ai used to test observers.

  @Test
  void multipleStockObservers_allGetNotified() {
    FakeStockObserver observer1 = new FakeStockObserver();
    FakeStockObserver observer2 = new FakeStockObserver();
    appState.addStockObserver(observer1);
    appState.addStockObserver(observer2);

    appState.setSelectedStock(stock);

    assertEquals(1, observer1.callCount);
    assertEquals(1, observer2.callCount);
  }

  @Test
  void multipleExchangeObservers_allGetNotified() {
    FakeExchangeObserver observer1 = new FakeExchangeObserver();
    FakeExchangeObserver observer2 = new FakeExchangeObserver();
    appState.addExchangeObserver(observer1);
    appState.addExchangeObserver(observer2);

    appState.setSelectedExchange(exchange);

    assertEquals(1, observer1.callCount);
    assertEquals(1, observer2.callCount);
  }

  @Test
  void multiplePlayerObservers_allGetNotified() {
    FakePlayerObserver observer1 = new FakePlayerObserver();
    FakePlayerObserver observer2 = new FakePlayerObserver();
    appState.addPlayerObserver(observer1);
    appState.addPlayerObserver(observer2);

    appState.setSelectedPlayer(player);

    assertEquals(1, observer1.callCount);
    assertEquals(1, observer2.callCount);
  }
}