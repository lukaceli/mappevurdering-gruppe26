package windows.start;

import exceptions.IllegalFileFormatException;
import io.CsvReader;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import model.appstate.AppState;
import model.appstate.Difficulty;
import model.exchange.Exchange;
import model.exchange.ExchangeFactory;
import model.exchange.ExchangeList;
import model.player.Player;
import model.player.PlayerArchive;
import model.stock.Stock;

/**
 * Controller for the start window, handling player creation,
 * difficulty selection, exchange initialisation, and custom stock file loading.
 */
public class StartController {

  private final ExchangeList exchangeList;
  private final AppState appState;
  private final PlayerArchive playerArchive;
  private List<Stock> customStocks;

  /**
   * Constructs a {@code StartController} with the given exchange list, app state,
   * and player archive.
   *
   * @param exchangeList  the list of exchanges to populate
   * @param appState      the shared application state
   * @param playerArchive the archive to register new players in
   */
  public StartController(ExchangeList exchangeList, AppState appState,
                         PlayerArchive playerArchive) {
    this.exchangeList = exchangeList;
    this.appState = appState;
    this.playerArchive = playerArchive;
  }

  /**
   * Creates a new player with the given name, starting capital, and difficulty,
   * then initialises the default exchanges and optionally adds a custom exchange.
   *
   * @param name       the player's name
   * @param capital    the starting capital as a string
   * @param difficulty the selected difficulty level ("EASY", "NORMAL", or "HARD")
   * @return {@code null} on success, or an error message string on failure
   */
  public String createPlayer(String name, String capital, String difficulty) {
    try {
      if (name.isEmpty() || capital.isEmpty()) {
        throw new IllegalArgumentException("Player name and capital must be filled");
      }
      BigDecimal capitalBigDecimal = new BigDecimal(capital);
      Player player = new Player(name, capitalBigDecimal);
      appState.setSelectedPlayer(player);

      switch (difficulty) {
        case "EASY" -> Difficulty.setDifficulty(Difficulty.EASY);
        case "NORMAL" -> Difficulty.setDifficulty(Difficulty.NORMAL);
        case "HARD" -> Difficulty.setDifficulty(Difficulty.HARD);
        default -> throw new IllegalArgumentException("Invalid difficulty");
      }

      exchangeList.clearExchanges();
      initDefaultExchanges();

      if (customStocks != null) {
        exchangeList.addExchange(
                new Exchange("Custom Exchange", customStocks, BigDecimal.ONE));
      }

      playerArchive.addPlayer(player);
      return null;
    } catch (IllegalArgumentException ex) {
      return ex.getMessage();
    } catch (IOException e) {
      return "Error loading stock data: " + e.getMessage();
    }
  }

  /**
   * Loads a CSV file and parses it into a list of custom stocks.
   *
   * @param file the CSV file to load
   * @return {@code null} on success, or an error message string on failure
   */
  public String loadFile(File file) {
    CsvReader reader = new CsvReader(file);
    try {
      customStocks = reader.getStocksFromFile();
      return null;
    } catch (IllegalFileFormatException e) {
      return "File does not follow the given format";
    } catch (IOException e) {
      return e.getMessage();
    }
  }

  private void initDefaultExchanges() throws IOException {
    exchangeList.addExchange(ExchangeFactory.fromCsv(
            "S&P500", "src/main/resources/S&P500Stocks.csv", BigDecimal.ONE));
    exchangeList.addExchange(ExchangeFactory.fromCsv(
            "Crypto", "src/main/resources/crypto_top40.csv", new BigDecimal("2")));
    exchangeList.addExchange(ExchangeFactory.fromCsv(
            "Oslo", "src/main/resources/oslo_bors.csv", new BigDecimal("1.3")));
  }
}