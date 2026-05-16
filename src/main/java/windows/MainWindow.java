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
import model.exchange.ExchangeList;
import model.player.Player;
import model.player.PlayerArchive;
import model.player.PlayerObserver;

public class MainWindow implements PlayerObserver {
  private final Stage window;
  private final BorderPane root;
  private ExchangeWindow exchangeWindow;
  private final ExchangeList exchangeList;
  public final static int sceneHeight = 1000;
  public final static int sceneWidth = 1000;
  private final AppState appState;
  private final PlayerArchive playerArchive;
  private ProfileWindow profileWindow;
  private Player player;
  private final StartWindow startWindow;
  private final ToolBar toolBar;
  private final Scene scene;
  private final MainController controller;
  private final Label statusPlayerName;
  private final Label statusBalance;
  private final Label statusNetWorth;
  private final Label statusWeek;
  private final Label statusPlayerStatus;
  private final HBox statusBar;
  private final NewPlayerWindow newPlayerWindow;


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

    Label nameLabel = new Label("PLAYER");
    Label balLabel = new Label("BALANCE");
    Label nwLabel = new Label("NET WORTH");
    Label weekLabel = new Label("WEEK");
    Label statusLabel = new Label("STATUS");

    nameLabel.getStyleClass().add("status-chip-label");
    balLabel.getStyleClass().add("status-chip-label");
    nwLabel.getStyleClass().add("status-chip-label");
    weekLabel.getStyleClass().add("status-chip-label");
    statusLabel.getStyleClass().add("status-chip-label");

    VBox nameBox = new VBox(2, nameLabel, statusPlayerName);
    VBox balBox = new VBox(2, balLabel, statusBalance);
    VBox nwBox = new VBox(2, nwLabel, statusNetWorth);
    VBox weekBox = new VBox(2, weekLabel, statusWeek);
    VBox statusBox = new VBox(2, statusLabel, statusPlayerStatus);

    statusBar = new HBox(20, nameBox, balBox, nwBox, weekBox, statusBox);

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

    window.setTitle("Millions - A Trading Game");
    window.setScene(scene);

    playerArchive.addPlayerObserver(this);
  }

  public void init() {
    this.player = appState.getSelectedPlayer();
    this.exchangeWindow = controller.createExchangeWindow(player, this::updateStatusBar);
    this.profileWindow = controller.createPortefolioWindow(player, this::updateStatusBar);
    root.setCenter(exchangeWindow.getRoot());
    toolBar.getItems().clear();
    statusPlayerName.setText(player.getName());
    statusBalance.setText(player.getBalance() + "$");
    statusNetWorth.setText(player.getNetWorth() + "$");
    statusWeek.setText("Week " + controller.getWeek());
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
      if (profileWindow != null) {
        profileWindow.refreshData();
        updateStatusBar();
        root.setCenter(profileWindow.getRoot());
      }
    });
    btnProfile.getStyleClass().addAll("nav-button");

    btnStart.setOnAction(e -> root.setCenter(newPlayerWindow.getRoot()));
    btnStart.getStyleClass().addAll("nav-button");


    newPlayerWindow.getYesBtn().setOnAction(e -> root.setCenter(startWindow.getRoot()));
    newPlayerWindow.getNoBtn().setOnAction(e -> root.setCenter(exchangeWindow.getRoot()));
  }

  private void updateStatusBar() {
    statusPlayerName.setText(player.getName());
    statusBalance.setText(player.getBalance() + "$");
    statusNetWorth.setText(player.getNetWorth() + "$");
    statusWeek.setText("Week" + controller.getWeek());
    statusPlayerStatus.setText(player.getStatus());
  }

  public void show() {
    window.show();
  }

  @Override
  public void gameStart() {
    init();
  }
}