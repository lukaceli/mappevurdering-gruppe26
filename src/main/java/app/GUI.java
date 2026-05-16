package app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import windows.MainWindow;

import java.io.IOException;

public class GUI extends Application {

  @Override
  public void start(Stage primaryStage) throws IOException {
    MainWindow mainWindow = new MainWindow(primaryStage);
    mainWindow.show();

  }
}
