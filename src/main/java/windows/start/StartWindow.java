package windows.start;

import java.io.File;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import model.appstate.AppState;
import model.exchange.ExchangeList;
import model.player.PlayerArchive;

/**
 * JavaFX view for the game setup screen.
 * Allows the player to enter their name, starting capital, difficulty level,
 * and optionally load a custom stock CSV file before starting the game.
 */
public class StartWindow {

  private final BorderPane root;
  private final StartController controller;

  /**
   * Constructs a {@code StartWindow} and initialises all UI components.
   *
   * @param exchangeList  the list of exchanges to populate on game start
   * @param appState      the shared application state
   * @param playerArchive the archive to register the new player in
   * @param onBack        a callback to run when the back button is pressed
   */
  public StartWindow(ExchangeList exchangeList, AppState appState, PlayerArchive playerArchive,
                     Runnable onBack) {
    this.controller = new StartController(exchangeList, appState, playerArchive);
    this.root = new BorderPane();
    root.getStyleClass().add("start-page");

    Label welcomeLabel = new Label("Welcome To Millions - A Stock-Trading Game");
    welcomeLabel.getStyleClass().add("welcome-title");

    Label subtitleLabel = new Label("Create a user to start game");
    subtitleLabel.getStyleClass().add("welcome-subtitle");

    VBox welcomeBox = new VBox(8, welcomeLabel, subtitleLabel);
    welcomeBox.getStyleClass().add("welcome-box");
    root.setTop(welcomeBox);

    Label nameLabel = new Label("Name");
    TextField nameField = new TextField();
    nameField.setPromptText("Name");
    nameField.setPrefColumnCount(15);
    nameLabel.getStyleClass().add("form-label");
    nameField.getStyleClass().add("form-input");

    Label capitalLabel = new Label("Start capital");
    TextField capitalField = new TextField();
    capitalField.setPromptText("Amount");
    capitalField.setPrefColumnCount(15);
    capitalLabel.getStyleClass().add("form-label");
    capitalField.getStyleClass().add("form-input");
    Label error = new Label("Error");
    error.getStyleClass().add("form-error");
    error.setVisible(false);

    Label fileMsg = new Label("Error");
    fileMsg.getStyleClass().add("form-error");
    fileMsg.setVisible(false);

    Label helpIconFile = new Label("?");
    helpIconFile.getStyleClass().add("help-icon");

    Tooltip fileTooltip = new Tooltip(
            "Add your own CSV file with stock data to create a custom exchange.\n"
                    + "Read the manual to see how this is done.\nThis field is optional.");
    fileTooltip.setFont(Font.font(13));
    fileTooltip.setShowDelay(Duration.millis(50));
    fileTooltip.setShowDuration(Duration.INDEFINITE);
    fileTooltip.setHideDelay(Duration.millis(200));
    Tooltip.install(helpIconFile, fileTooltip);

    ToggleGroup difficultyGroup = new ToggleGroup();
    ToggleButton easyBtn = new ToggleButton("EASY");
    ToggleButton normalBtn = new ToggleButton("NORMAL");
    ToggleButton hardBtn = new ToggleButton("HARD");
    easyBtn.setToggleGroup(difficultyGroup);
    normalBtn.setToggleGroup(difficultyGroup);
    hardBtn.setToggleGroup(difficultyGroup);
    normalBtn.setSelected(true);
    easyBtn.getStyleClass().add("difficulty-btn");
    normalBtn.getStyleClass().add("difficulty-btn");
    hardBtn.getStyleClass().add("difficulty-btn");

    Label helpIconDiff = new Label("?");
    helpIconDiff.getStyleClass().add("help-icon");

    Tooltip diffTooltip = new Tooltip(
            "The difficulty level changes the luck factor\n"
                    + "which causes stocks to rise in price over time,\n"
                    + "and how volatile the stock price is.\n"
                    + "In addition, tax and commission increase at higher difficulty levels.");
    diffTooltip.setFont(Font.font(13));
    diffTooltip.setShowDelay(Duration.millis(50));
    diffTooltip.setShowDuration(Duration.INDEFINITE);
    diffTooltip.setHideDelay(Duration.millis(200));
    Tooltip.install(helpIconDiff, diffTooltip);

    Button createBtn = new Button("Create Game");
    createBtn.setPrefWidth(100);
    createBtn.getStyleClass().add("advance-button");

    Button backBtn = new Button("Back");
    backBtn.getStyleClass().add("nav-button");
    backBtn.setOnAction(e -> onBack.run());

    Button fileBtn = new Button("Choose file");
    HBox fileHelp = new HBox(10, fileBtn, helpIconFile);
    VBox fileBox = new VBox(5, fileMsg, fileHelp);
    HBox capitalBox = new HBox(10, capitalLabel, capitalField);
    HBox nameBox = new HBox(10, nameLabel, nameField);
    HBox difficultyBox = new HBox(10, easyBtn, normalBtn, hardBtn, helpIconDiff);
    VBox createUser = new VBox(15, nameBox, capitalBox, error, fileBox,
            difficultyBox, createBtn, backBtn);
    createUser.getStyleClass().add("start-form");
    root.setCenter(createUser);

    createBtn.setOnAction(event -> {
      ToggleButton selected = (ToggleButton) difficultyGroup.getSelectedToggle();
      String difficulty = selected.getText();
      String errorMsg = controller.createPlayer(
              nameField.getText(), capitalField.getText(), difficulty);
      if (errorMsg != null) {
        error.setText(errorMsg);
        error.setVisible(true);
      } else {
        error.setVisible(false);
        fileMsg.setVisible(false);
      }
    });

    fileBtn.setOnAction(event -> {
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Choose Stock CSV File");
      File file = fileChooser.showOpenDialog(root.getScene().getWindow());
      fileMsg.setVisible(true);
      fileMsg.setStyle("-fx-text-fill: red;");
      if (file != null) {
        String errorMsg = controller.loadFile(file);
        if (errorMsg != null) {
          fileMsg.setText(errorMsg);
        } else {
          fileMsg.setStyle("-fx-text-fill: green;");
          fileMsg.setText("File " + file.getName() + " loaded");
        }
      } else {
        fileMsg.setText("No file selected");
      }
    });
  }

  /**
   * Returns the root pane of this window.
   *
   * @return the {@link BorderPane} root
   */
  public BorderPane getRoot() {
    return root;
  }
}