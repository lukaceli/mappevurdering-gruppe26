package windows.start;

import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import model.appstate.AppState;
import model.exchange.ExchangeList;
import model.player.PlayerArchive;

import java.io.File;

public class StartWindow {
  private final BorderPane root;
  private final StartController controller;

  public StartWindow(ExchangeList exchangeList, AppState appState, PlayerArchive playerArchive, Runnable onBack) {
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

    VBox createUser = new VBox(15);
    createUser.getStyleClass().add("start-form");
    root.setCenter(createUser);

    Label name = new Label("Name");
    TextField nameField = new TextField();
    nameField.setPromptText("Name");
    nameField.setPrefColumnCount(15);
    HBox nameBox = new HBox(10);
    nameBox.getChildren().addAll(name, nameField);


    Label capital = new Label("Start capital");
    TextField capitalField = new TextField();
    capitalField.setPromptText("Amount");
    capitalField.setPrefColumnCount(15);
    HBox capitalBox = new HBox(10);
    capitalBox.getChildren().addAll(capital, capitalField);

    nameField.getStyleClass().add("form-input");
    capitalField.getStyleClass().add("form-input");
    name.getStyleClass().add("form-label");
    capital.getStyleClass().add("form-label");

    Label error = new Label("Error");
    error.getStyleClass().add("form-error");
    error.setVisible(false);

    Button fileBtn = new Button("Choose file");
    Label helpIconFile = new Label("?");
    helpIconFile.getStyleClass().add("help-icon");

    Button backBtn = new Button("Back");
    backBtn.setOnAction(e -> onBack.run());
    backBtn.getStyleClass().add("nav-button");

    Tooltip fileTooltip = new Tooltip("Add your own CSV file with stock data to create a custom exchange.\n" +
            "Read the manual to see how this is done. \nThis field is optional.");
    fileTooltip.setFont(Font.font(13));
    Tooltip.install(helpIconFile, fileTooltip);
    fileTooltip.setShowDelay(javafx.util.Duration.millis(50));
    fileTooltip.setShowDuration(javafx.util.Duration.INDEFINITE);
    fileTooltip.setHideDelay(javafx.util.Duration.millis(200));

    Label fileMsg = new Label("Error");
    fileMsg.getStyleClass().add("form-error");
    fileMsg.setVisible(false);

    HBox fileHelp = new HBox(10);
    fileHelp.getChildren().addAll(fileBtn, helpIconFile);
    VBox fileBox = new VBox(5);
    fileBox.getChildren().addAll(fileMsg, fileHelp);

    HBox difficultyBox = new HBox(10);
    ToggleGroup difficultyGroup = new ToggleGroup();
    ToggleButton easyBtn = new ToggleButton("EASY");
    ToggleButton normalBtn = new ToggleButton("NORMAL");
    ToggleButton hardBtn = new ToggleButton("HARD");
    easyBtn.setToggleGroup(difficultyGroup);
    normalBtn.setToggleGroup(difficultyGroup);
    hardBtn.setToggleGroup(difficultyGroup);
    normalBtn.setSelected(true);

    Label helpIconDiff = new Label("?");
    helpIconDiff.getStyleClass().add("help-icon");
    Tooltip diffTooltip = new Tooltip("The difficulty level changes the luck factor \n" +
            " which causes stocks to rise in price over time," +
            "\n and how volatile the stock price is. " +
            "\nIn addition, tax and commission increase at higher difficulty levels.");
    diffTooltip.setFont(Font.font(13));
    Tooltip.install(helpIconDiff, diffTooltip);
    diffTooltip.setShowDelay(javafx.util.Duration.millis(50));
    diffTooltip.setShowDuration(javafx.util.Duration.INDEFINITE);
    diffTooltip.setHideDelay(javafx.util.Duration.millis(200));

    difficultyBox.getChildren().addAll(easyBtn, normalBtn, hardBtn, helpIconDiff);

    easyBtn.getStyleClass().add("difficulty-btn");
    normalBtn.getStyleClass().add("difficulty-btn");
    hardBtn.getStyleClass().add("difficulty-btn");


    Button createBtn = new Button("Create Game");
    createBtn.setPrefWidth(100);
    createBtn.getStyleClass().add("advance-button");

    createUser.getChildren().addAll(nameBox, capitalBox, error, fileBox, difficultyBox, createBtn, backBtn);


    createBtn.setOnAction(event -> {
      ToggleButton selected = (ToggleButton) difficultyGroup.getSelectedToggle();
      String difficulty = selected.getText();
      String errorMsg = controller.createPlayer(nameField.getText(), capitalField.getText(), difficulty);

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
          fileMsg.setText("File " + file.getName() + " loaded" );
        }
      } else {
        fileMsg.setText("No file selected");
      }
    });
  }


  public BorderPane getRoot() {
    return root;
  }
}