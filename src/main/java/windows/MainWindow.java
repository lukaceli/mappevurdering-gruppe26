package windows;

import io.CsvReader;
import java.math.BigDecimal;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.exchange.Exchange;

import java.io.IOException;
import java.nio.file.Path;
import model.player.Player;

public class MainWindow {
  private Stage window;
  private BorderPane root;
  private ExchangeWindow exchangeWindow;
  private Exchange exchange;
  private CsvReader csvReader;
  public final static int sceneHeight = 1000;
  public final static int sceneWidth = 1000;
  private PortefolioWindow portefolioWindow;
  private Player player;


  public MainWindow(Stage primaryStage) {
    this.window = primaryStage;
  }

  public void init() throws IOException {
    final Path filePath = Path.of("src/main/resources/S&P500Stocks.csv");
    csvReader = new CsvReader();
    exchange = new Exchange("Nasdaq" , csvReader.getStocksFromFile(filePath));
    exchangeWindow = new ExchangeWindow(exchange);
    player = new Player("player", new BigDecimal("1000" ));
    portefolioWindow = new PortefolioWindow(player, exchange);
    root = new BorderPane();
    Scene scene = new Scene(root, sceneHeight, sceneWidth);
    ToolBar toolBar = new ToolBar();
    toolBar.setStyle("-fx-background-color: #c15959");
    root.setTop(toolBar);
    Button btnAdvance = new Button("Advance");
    Button btnExchange = new Button("Exchange");
    Button btnProfile = new Button("Profile");
    toolBar.getItems().addAll(btnAdvance, btnExchange, btnProfile);

    btnAdvance.setOnAction(e -> {
      exchange.advance();
    });

    btnExchange.setOnAction(e -> {
      root.setCenter(exchangeWindow.getRoot());
    });

    btnProfile.setOnAction(e -> {
      root.setCenter(portefolioWindow.getRoot());
    });


    Label label = new Label("Hello");
    root.setCenter(label);

    window.setTitle("Aksje Spill");
    window.setScene(scene);
  }
  public void show() {
    window.show();
  }
  public void close() {
    window.close();
  }


}
