package windows;

import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import model.appState.AppState;
import model.exchange.ExchangeList;

import java.io.File;
import java.io.IOException;

public class StartWindow {
  private final BorderPane root;
  private final StartController controller;

  public StartWindow(ExchangeList exchangeList, AppState appState, PlayerArchive playerArchive) {
    this.controller = new StartController(exchangeList, appState, playerArchive);
    this.root = new BorderPane();

    // Hovedcontainer
    VBox createUser = new VBox(15);
    createUser.setStyle("-fx-background-color: #97f875; -fx-padding: 20;");
    root.setCenter(createUser);

    // Navn felt
    Label name = new Label("Name");
    TextField nameField = new TextField();
    nameField.setPromptText("Name");
    nameField.setPrefColumnCount(15);
    HBox nameBox = new HBox(10);
    nameBox.getChildren().addAll(name, nameField);

    // Kapital felt
    Label capital = new Label("Start capital");
    TextField capitalField = new TextField();
    capitalField.setPromptText("Amount");
    capitalField.setPrefColumnCount(15);
    HBox capitalBox = new HBox(10);
    capitalBox.getChildren().addAll(capital, capitalField);

    // Feilmelding label
    Label error = new Label("Error");
    error.setStyle("-fx-background-color: #801f1f; -fx-text-fill: white; -fx-padding: 5;");
    error.setVisible(false);

    // Filvelger seksjon
    Button fileBtn = new Button("Choose file");
    Button helpIcon = new Button("?");
    helpIcon.setStyle(
            "-fx-background-color: #555555;" +
                    "-fx-text-fill: white;" +
                    "-fx-font-weight: bold;" +
                    "-fx-padding: 2 6 2 6;" +
                    "-fx-background-radius: 50%;" +
                    "-fx-cursor: hand;"
    );

    Tooltip tooltip = new Tooltip("Legg inn egen csv fil med aksjedata for å opprette egen børs.\n" +
            "Les manual for å se hvordan dette gjøres. \nDette feltet er valgfritt.");
    tooltip.setFont(Font.font(13));
    Tooltip.install(helpIcon, tooltip);
    tooltip.setShowDelay(javafx.util.Duration.millis(100));

    Label fileError = new Label("Error");
    fileError.setStyle("-fx-background-color: #801f1f; -fx-text-fill: white;");
    fileError.setVisible(false);

    HBox fileHelp = new HBox(10);
    fileHelp.getChildren().addAll(fileBtn, helpIcon);
    VBox fileBox = new VBox(5);
    fileBox.getChildren().addAll(fileError, fileHelp);

    // Vanskelighetsgrad
    HBox difficultyBox = new HBox(10);
    ToggleGroup difficultyGroup = new ToggleGroup();
    ToggleButton easyBtn = new ToggleButton("Easy");
    ToggleButton normalBtn = new ToggleButton("Normal");
    ToggleButton hardBtn = new ToggleButton("Hard");
    easyBtn.setToggleGroup(difficultyGroup);
    normalBtn.setToggleGroup(difficultyGroup);
    hardBtn.setToggleGroup(difficultyGroup);
    normalBtn.setSelected(true);
    difficultyBox.getChildren().addAll(easyBtn, normalBtn, hardBtn);

    // Opprett knapp
    Button createBtn = new Button("Create Game");
    createBtn.setPrefWidth(100);

    // Legg alt inn i VBox
    createUser.getChildren().addAll(nameBox, capitalBox, error, fileBox, difficultyBox, createBtn);


    createBtn.setOnAction(event -> {
      ToggleButton selected = (ToggleButton) difficultyGroup.getSelectedToggle();
      String difficulty = selected.getText();
      String errorMsg = controller.createPlayer(nameField.getText(), capitalField.getText(), difficulty);

      if (errorMsg != null) {
        error.setText(errorMsg);
        error.setVisible(true);
      } else {
        error.setVisible(false);
      }
    });

    fileBtn.setOnAction(event -> {
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Choose Stock CSV File");
      File file = fileChooser.showOpenDialog(root.getScene().getWindow());
      if (file != null) {
        try {
          controller.loadFile(file);
          fileError.setVisible(false);
        } catch (IOException e) {
          fileError.setText(e.getMessage());
          fileError.setVisible(true);
        }
      }
    });
  }

  public BorderPane getRoot() {
    return root;
  }
}