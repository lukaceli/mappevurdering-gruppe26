package windows;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class NewPlayerWindow {

  Label confirmationLabel;
  BorderPane root;
  Button yesBtn;
  Button noBtn;

  NewPlayerWindow() {
    root = new BorderPane();
    root.getStyleClass().add("start-page");

    VBox card = new VBox(16);
    card.setMaxWidth(380);
    card.setAlignment(Pos.CENTER_LEFT);
    card.getStyleClass().add("start-form");

    Label title = new Label("Start over?");
    title.setFont(Font.font("Arial", FontWeight.BOLD, 22));
    title.setStyle("-fx-text-fill: #1a1f2e;");

    confirmationLabel = new Label("Are you sure you want to create a new player?\nBy pressing yes you will lose all your progress and start from scratch.");
    confirmationLabel.setWrapText(true);
    confirmationLabel.setStyle("-fx-text-fill: #9ca3af; -fx-font-size: 13px;");

    VBox titleBox = new VBox(8, title, confirmationLabel);
    titleBox.setPadding(new Insets(0, 0, 8, 0));

    yesBtn = new Button("Yes, start over");
    yesBtn.setMaxWidth(Double.MAX_VALUE);
    yesBtn.setStyle("-fx-background-color: #dc2626; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10 0; -fx-background-radius: 8; -fx-cursor: hand;");

    noBtn = new Button("No, go back");
    noBtn.setMaxWidth(Double.MAX_VALUE);
    noBtn.setStyle("-fx-background-color: #16a34a; -fx-text-fill: white; " +
            "-fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10 0; -fx-background-radius: 8; " +
            "-fx-cursor: hand;");

    card.getChildren().addAll(titleBox, yesBtn, noBtn);

    StackPane centered = new StackPane(card);
    centered.getStyleClass().add("start-page");
    root.setCenter(centered);
  }

  public Button getYesBtn() { return yesBtn; }
  public Button getNoBtn() { return noBtn; }

  public BorderPane getRoot() {
    return root;
  }
}