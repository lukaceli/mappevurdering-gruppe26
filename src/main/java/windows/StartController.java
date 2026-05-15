package windows;

import io.CsvReader;
import javafx.stage.FileChooser;
import model.appState.AppState;
import model.appState.Difficulty;
import model.exchange.Exchange;
import model.exchange.ExchangeFactory;
import model.exchange.ExchangeList;
import model.player.Player;
import model.stock.Stock;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

public class StartController {
  private Player player;
  private CsvReader reader;
  private ExchangeList exchangeList;
  private AppState appState;
  private PlayerArchive playerArchive;
  private ArrayList<Stock> costumStocks;

  public StartController(ExchangeList exchangeList, AppState appState, PlayerArchive playerArchive) {
    this.exchangeList = exchangeList;
    this.appState = appState;
    this.playerArchive = playerArchive;
  }

  public String createPlayer(String name, String capital, String difficulty) {
    try {
      if (name.isEmpty() || capital.isEmpty()) {
        throw new IllegalArgumentException("Player name and capital must be filled");
      }
      BigDecimal capitalBigDecimal = new BigDecimal(capital);
      player = new Player(name, capitalBigDecimal);

      appState.setSelectedPlayer(player);
      switch (difficulty) {
        case "EASY":
          Difficulty.setDifficulty(Difficulty.EASY);
          break;
        case "NORMAL":
          Difficulty.setDifficulty(Difficulty.NORMAL);
          break;
        case "HARD":
          Difficulty.setDifficulty(Difficulty.HARD);
          break;
        default:
          throw new IllegalArgumentException("Invalid difficulty");
      }

      if (exchangeList.getExchanges().isEmpty()) {
        initDefaultExchanges();
      }

      if (costumStocks != null) {
        Exchange exchange = new Exchange("Custom Exchange", costumStocks);
        exchangeList.addExchange(exchange);
      }

      playerArchive.addPlayer(player);

      return null;
    } catch (IllegalArgumentException ex) {
      return ex.getMessage();
    } catch (IOException e) {
      return "Error loading stock data: " + e.getMessage();
    }
  }

  private void initDefaultExchanges() throws IOException {
    exchangeList.addExchange(ExchangeFactory.fromCsv("S&P500", "src/main/resources/S&P500Stocks.csv"));
    exchangeList.addExchange(ExchangeFactory.fromCsv("Crypto", "src/main/resources/crypto_top40.csv"));
    exchangeList.addExchange(ExchangeFactory.fromCsv("Oslo", "src/main/resources/oslo_bors.csv"));
  }

  public String loadFile(File file) throws IOException {
    reader = new CsvReader(file);
    try {
      costumStocks = reader.getStocksFromFile();
      return null;
    } catch (IllegalArgumentException e) {
      return "File does not follow the given format";
    }
  }
}