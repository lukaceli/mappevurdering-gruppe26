package windows;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
  private Label statusPlayerName;
  private Label statusBalance;
  private Label statusNetWorth;
  private Label statusWeek;
  private Label statusPlayerStatus;
  private HBox statusBar;
  private NewPlayerWindow newPlayerWindow;


  public MainWindow(Stage primaryStage) {
    this.root = new BorderPane();
    this.playerArchive = new PlayerArchive();
    this.appState = new AppState();
    this.exchangeList = new ExchangeList();

    this.controller = new MainController(appState, exchangeList);
    this.scene = new Scene(root, sceneHeight, sceneWidth);
    this.newPlayerWindow = new NewPlayerWindow();
    scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

    this.toolBar = new ToolBar();
    statusPlayerName = new Label("-");
    statusBalance = new Label("-");
    statusNetWorth = new Label("-");
    statusWeek = new Label("-");
    statusPlayerStatus = new Label("-");

    statusPlayerName.getStyleClass().add("status-chip");
    statusBalance.getStyleClass().add("status-chip-green");
    statusNetWorth.getStyleClass().add("status-chip-green");
    statusWeek.getStyleClass().add("status-chip");
    statusPlayerStatus.getStyleClass().add("status-chip-amber");

    statusBar = new HBox(20, statusPlayerName, statusBalance, statusNetWorth, statusWeek, statusPlayerStatus);
    statusBar.getStyleClass().add("status-bar");
    statusBar.setSpacing(10);
    statusBar.setVisible(false);

    VBox topBar = new VBox(toolBar, statusBar);
    root.setTop(topBar);
    toolBar.getStyleClass().add("navbar");

    this.window = primaryStage;

    this.startWindow = new StartWindow(exchangeList, appState, playerArchive,
        () -> root.setCenter(exchangeWindow.getRoot()));
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
    statusPlayerName.setText(player.getName());
    statusBalance.setText(player.getBalance() + "$");
    statusNetWorth.setText(player.getNetWorth() + "$");
    statusWeek.setText("Week " + "Ikke fikset uke metoden ennå");
    statusPlayerStatus.setText(player.getStatus());
    statusBar.setVisible(true);

    Button btnStart = new Button("Start New Game");
    Button btnExchange = new Button("Exchange");
    Button btnProfile = new Button("Profile");

    toolBar.getItems().addAll(btnExchange, btnStart, btnProfile);


    btnExchange.setOnAction(e -> {
      if (exchangeWindow != null) root.setCenter(exchangeWindow.getRoot());
    });
    btnExchange.getStyleClass().addAll("nav-button");

    btnProfile.setOnAction(e -> {
      if (portefolioWindow != null) {
        portefolioWindow.refreshData();
        root.setCenter(portefolioWindow.getRoot());
      }
    });
    btnProfile.getStyleClass().addAll("nav-button");

    btnStart.setOnAction(e -> root.setCenter(newPlayerWindow.getRoot()));
    btnStart.getStyleClass().addAll("nav-button");


    newPlayerWindow.getYesBtn().setOnAction(e -> root.setCenter(startWindow.getRoot()));
    newPlayerWindow.getNoBtn().setOnAction(e -> root.setCenter(exchangeWindow.getRoot()));
  }

  public void show() {
    window.show();
  }

  @Override
  public void gameStart() {
    init();
  }
}