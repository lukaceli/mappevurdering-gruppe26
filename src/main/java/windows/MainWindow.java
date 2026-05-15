package windows;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.appState.AppState;
import model.exchange.Exchange;
import model.exchange.ExchangeList;
import model.exchange.PlayerObserver;
import model.player.Player;

import java.io.IOException;

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
  private StartWindow startWindow;
  private ToolBar toolBar;
  private Scene scene;
  private MainController controller;

  public MainWindow(Stage primaryStage) {
    this.root = new BorderPane();
    this.playerArchive = new PlayerArchive();
    this.appState = new AppState();
    this.exchangeList = new ExchangeList();

    this.controller = new MainController(appState, exchangeList);
    this.scene = new Scene(root, sceneHeight, sceneWidth);

    this.toolBar = new ToolBar();
    this.toolBar.setStyle("-fx-background-color: #c15959");
    root.setTop(toolBar);

    this.window = primaryStage;

    this.startWindow = new StartWindow(exchangeList, appState, playerArchive);
    root.setCenter(startWindow.getRoot());

    window.setTitle("Aksje Spill");
    window.setScene(scene);

    playerArchive.addPlayerObserver(this);
  }

  public void init() {
    this.player = appState.getSelectedPlayer();
    this.exchangeWindow = controller.createExchangeWindow(player);
    this.portefolioWindow = controller.createPortefolioWindow(player);
    root.setCenter(exchangeWindow.getRoot());
    toolBar.getItems().clear();

    Button btnStart = new Button("Start");
    Button btnAdvance = new Button("Advance");
    Button btnExchange = new Button("Exchange");
    Button btnProfile = new Button("Profile");

    toolBar.getItems().addAll(btnAdvance, btnExchange, btnStart, btnProfile);

    btnAdvance.setOnAction(e -> controller.advanceAllExchanges());

    btnExchange.setOnAction(e -> {
      if (exchangeWindow != null) root.setCenter(exchangeWindow.getRoot());
    });

    btnProfile.setOnAction(e -> {
      if (portefolioWindow != null) {
        portefolioWindow.refreshData();
        root.setCenter(portefolioWindow.getRoot());
      }
    });

    btnStart.setOnAction(e -> root.setCenter(startWindow.getRoot()));
  }

  public void show() {
    window.show();
  }

  @Override
  public void gameStart() {
    init();
  }
}