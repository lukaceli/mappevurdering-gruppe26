package windows;

import io.CsvReader;
import model.appState.AppState;
import model.exchange.Exchange;
import model.exchange.ExchangeList;
import model.player.Player;
import model.stock.Stock;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

public class StartController {
  private Player player;
  private CsvReader reader;
  private ExchangeList exchangeList;
  private AppState appState;
  private PlayerArchive playerArchive;

  public StartController(ExchangeList exchangeList, AppState appState,  PlayerArchive playerArchive) {
    this.exchangeList = exchangeList;
    this.appState = appState;
    this.playerArchive = playerArchive;
  }

  public String createPlayer(String name, String capital) {
    try {
      if (name.isEmpty() || capital.isEmpty()) {
        throw new IllegalArgumentException("Player name and capital must be filled");
      }
      BigDecimal capitalBigDecimal = new BigDecimal(capital);
      player = new Player(name, capitalBigDecimal);
      appState.setSelectedPlayer(player);
      playerArchive.addPlayer(player);


      return null;
    } catch (IllegalArgumentException ex) {
      return ex.getMessage();
    }
  }

  public void loadFile(File file) throws IOException {
    reader = new CsvReader(file);
    System.out.println(reader.readFile());
    ArrayList<Stock> stocks = reader.getStocksFromFile();
    Exchange exchange = new Exchange("Crypto" , stocks);
    exchangeList.addExchange(exchange);
  }
}
