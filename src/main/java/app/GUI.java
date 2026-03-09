package app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class GUI extends Application {

  @Override
  public void start(Stage stage) {
    Label label = new Label("Hello");
    label.setLayoutX(100);
    label.setLayoutY(100);

    Scene scene = new Scene(label, 400, 300);

    stage.setTitle("Aksje Spill");
    stage.setScene(scene);
    stage.show();
  }

  void main(String[] args) {
    launch();
  }
}
