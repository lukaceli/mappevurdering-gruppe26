package windows;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;

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
    HBox fileBox = new HBox(10);

    nameBox.getChildren().addAll(name, nameField);
    capitalBox.getChildren().addAll(capital, capitalField);
    fileBox.getChildren().addAll(fileBtn);


    createUser.getChildren().addAll(nameBox, capitalBox, error, createBtn, fileBox);

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
      controller.loadFile(file);
    });

  }
  public BorderPane getRoot() {
    return root;
  }
}
