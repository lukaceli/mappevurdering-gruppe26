package windows;

import execeptions.IllegalFileFormatException;
import io.CsvReader;
import model.appState.AppState;
import model.appState.Difficulty;
import model.exchange.Exchange;
import model.exchange.ExchangeFactory;
import model.exchange.ExchangeList;
import model.player.Player;
import model.player.PlayerArchive;
import model.stock.Stock;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public class StartController {
  private Player player;
  private CsvReader reader;
  private final ExchangeList exchangeList;
  private final AppState appState;
  private final PlayerArchive playerArchive;
  private List<Stock> customStocks;

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
        exchangeList.clearExchanges();
        initDefaultExchanges();

      if (customStocks != null) {
        Exchange exchange = new Exchange("Custom Exchange", customStocks, BigDecimal.ONE);
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
    exchangeList.addExchange(ExchangeFactory.fromCsv("S&P500", "src/main/resources/S&P500Stocks.csv", BigDecimal.ONE));
    exchangeList.addExchange(ExchangeFactory.fromCsv("Crypto", "src/main/resources/crypto_top40.csv", new BigDecimal("2")));
    exchangeList.addExchange(ExchangeFactory.fromCsv("Oslo", "src/main/resources/oslo_bors.csv",  new BigDecimal("1.3")));
  }

  public String loadFile(File file) {
    reader = new CsvReader(file);
    try {
      customStocks = reader.getStocksFromFile();
      return null;
    } catch (IllegalFileFormatException e) {
      return "File does not follow the given format";
    } catch (IOException e) {
      return e.getMessage();
    }
  }
}