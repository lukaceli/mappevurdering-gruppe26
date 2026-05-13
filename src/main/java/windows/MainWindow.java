package windows;

import io.CsvReader;
import java.math.BigDecimal;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.appState.AppState;
import model.exchange.Exchange;
import model.exchange.ExchangeList;
import model.exchange.PlayerObserver;
import model.player.Player;

import java.io.IOException;
import java.nio.file.Path;
import model.player.Player;

public class MainWindow implements PlayerObserver {
  private Stage window;
  private BorderPane root;
  private ExchangeWindow exchangeWindow;
  private ExchangeList exchangeList;
  public final static int sceneHeight = 1000;
  public final static int sceneWidth = 1000;
  private AppState appState;
  private PlayerArchive playerArchive;
  private PortefolioWindow portefolioWindow;
  private Player player;


  public MainWindow(Stage primaryStage) {
    this.window = primaryStage;
  }

  public void init() throws IOException {
    this.appState = new AppState();
    this.playerArchive = new PlayerArchive();
    playerArchive.addPlayerObserver(this);
    exchangeList = new ExchangeList();
    final Path filePathSap = Path.of("src/main/resources/S&P500Stocks.csv");
    final Path filePathCrypto =  Path.of("src/main/resources/crypto_top40.csv");
    CsvReader sapReader = new CsvReader(filePathSap);
    CsvReader cryptoReader = new CsvReader(filePathCrypto);

    Exchange nasdaq = new Exchange("S&P500", sapReader.getStocksFromFile());
    Exchange crypto = new Exchange("Crypto", cryptoReader.getStocksFromFile());
    exchangeList.addExchange(nasdaq);
    exchangeList.addExchange(crypto);
    StartWindow startWindow = new StartWindow(exchangeList, appState, playerArchive);
    root = new BorderPane();
    Scene scene = new Scene(root, sceneHeight, sceneWidth);
    ToolBar toolBar = new ToolBar();
    toolBar.setStyle("-fx-background-color: #c15959");
    root.setTop(toolBar);
    Button btnStart = new Button("Start");
    Button btnAdvance = new Button("Advance");
    Button btnExchange = new Button("Exchange");

    Button btnProfile = new Button("Profile");
    toolBar.getItems().addAll(btnAdvance, btnExchange, btnStart, btnProfile);
    window.setTitle("Aksje Spill");
    window.setScene(scene);
    root.setCenter(startWindow.getRoot());
    btnAdvance.setOnAction(e -> {
      for (Exchange exchange : exchangeList.getExchanges()) {
        exchange.advance();
      }
    });

    btnExchange.setOnAction(e -> {
      root.setCenter(exchangeWindow.getRoot());
    });

    btnProfile.setOnAction(e -> {
      portefolioWindow.refreshData();
      root.setCenter(portefolioWindow.getRoot());});

    btnStart.setOnAction(e -> {
      root.setCenter(startWindow.getRoot());
    });
  }
  public void show() {
    window.show();
  }
  public void close() {
    window.close();
  }


  @Override
  public void onPlayerChanged() {
    this.player = playerArchive.getPlayers().getFirst();
    portefolioWindow = new PortefolioWindow(player, exchangeList.getExchanges().getFirst());
    exchangeWindow = new ExchangeWindow(exchangeList, appState, player);
    portefolioWindow = new PortefolioWindow(player, exchangeList.getExchanges().getFirst());
  }
}
