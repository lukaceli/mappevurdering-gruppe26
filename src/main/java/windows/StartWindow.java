package windows;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;

public class StartWindow {
  private final BorderPane root;
  private final StartController controller;

  public StartWindow() {
    this.controller = new StartController();
    root = new BorderPane();
    VBox createUser = new VBox(10);
    createUser.setStyle("-fx-background-color: #97f875;");
    Label name = new Label("Name");
    Label capital = new Label("Start capital");
    root.setCenter(createUser);
    TextField nameField = new TextField();
    nameField.setPromptText("Name");
    nameField.setPrefColumnCount(10);
    TextField capitalField = new TextField();
    capitalField.setPromptText("Amount");
    capitalField.setPrefColumnCount(10);
    HBox nameBox = new HBox(10);
    HBox capitalBox = new HBox(10);
    Button createBtn = new Button("Create");
    Label error = new Label("Error");
    error.setStyle("-fx-background-color: #801f1f;");
    error.setVisible(false);
    Button fileBtn = new Button("Choose file");
    VBox fileBox = new VBox(10);
    HBox fileHelp = new HBox(50);
    Label fileError = new Label("Error");
    fileError.setStyle("-fx-background-color: #801f1f");
    fileError.setVisible(false);
    Button helpIcon = new Button("?");
    helpIcon.setStyle(
            "-fx-background-color: #555555;" +
                    "-fx-text-fill: white;" +
                    "-fx-font-weight: bold;" +
                    "-fx-padding: 2 6 2 6;" +
                    "-fx-background-radius: 50%;" +
                    "-fx-cursor: hand;"
    );
    Tooltip tooltip = new Tooltip("Legg inn egen csv fil med aksjedata for å oprette egen børs.\n " +
            "Les manual for å se hvordan dette gjøres. \n dette feltet er valgfritt.");
    tooltip.setFont(Font.font(13));
    Tooltip.install(helpIcon, tooltip);
    tooltip.setShowDelay(javafx.util.Duration.millis(100)); // Vises etter 0.1 sekund


    nameBox.getChildren().addAll(name, nameField);
    capitalBox.getChildren().addAll(capital, capitalField);
    fileHelp.getChildren().addAll(fileBtn, helpIcon);
    fileBox.getChildren().addAll(fileError, fileHelp);


    createUser.getChildren().addAll(nameBox, capitalBox, error, fileBox,  createBtn);

    createBtn.setOnAction(event -> {
      String errorMsg = controller.createPlayer(nameField.getText(), capitalField.getText());
      System.out.println(errorMsg);
      if (errorMsg != null) {
        error.setText(errorMsg);
        error.setVisible(true);
      } else {
        error.setVisible(false);
      }
    });

    fileBtn.setOnAction(event -> {
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("Choose file");
      File file = fileChooser.showOpenDialog(root.getScene().getWindow());
      try {
        controller.loadFile(file);
        fileError.setVisible(false);
      } catch (IOException e) {
        fileError.setText(e.getMessage());
        fileError.setVisible(true);
      }
    });

  }
  public BorderPane getRoot() {
    return root;
  }
}
