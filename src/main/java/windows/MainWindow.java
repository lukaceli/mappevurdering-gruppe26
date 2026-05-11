package windows;

import io.CsvReader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.appState.AppState;
import model.exchange.Exchange;
import model.exchange.ExchangeList;

import java.io.IOException;
import java.nio.file.Path;

public class MainWindow {
  private Stage window;
  private BorderPane root;
  private ExchangeWindow exchangeWindow;
  private Exchange nasdaq;
  private CsvReader csvReader;
  private ExchangeList exchangeList;
  public final static int sceneHeight = 1000;
  public final static int sceneWidth = 1000;


  public MainWindow(Stage primaryStage) {
    this.window = primaryStage;
  }

  public void init() throws IOException {
    exchangeList = new ExchangeList();
    final Path filePath = Path.of("src/main/resources/S&P500Stocks.csv");
    csvReader = new CsvReader(filePath);
    nasdaq = new Exchange("Nasdaq" , csvReader.getStocksFromFile());
    exchangeList.addExchange(nasdaq);
    StartWindow startWindow = new StartWindow(exchangeList);
    AppState appState = new AppState();
    exchangeWindow = new ExchangeWindow(exchangeList, appState);
    root = new BorderPane();
    Scene scene = new Scene(root, sceneHeight, sceneWidth);
    ToolBar toolBar = new ToolBar();
    toolBar.setStyle("-fx-background-color: #c15959");
    root.setTop(toolBar);
    Button btnStart = new Button("Start");
    Button btnAdvance = new Button("Advance");
    Button btnExchange = new Button("Exchange");
    toolBar.getItems().addAll(btnAdvance, btnExchange, btnStart);

    btnAdvance.setOnAction(e -> {
      for (Exchange exchange : exchangeList.getExchanges()) {
        exchange.advance();
      }
    });

    btnExchange.setOnAction(e -> {
      root.setCenter(exchangeWindow.getRoot());
    });

    btnStart.setOnAction(e -> {
      root.setCenter(startWindow.getRoot());
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
