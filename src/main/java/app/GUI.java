package app;

import javafx.application.Application;
import javafx.stage.Stage;
import windows.main.MainWindow;

import java.io.IOException;

public class GUI extends Application {

  @Override
  public void start(Stage primaryStage) throws IOException {
    MainWindow mainWindow = new MainWindow(primaryStage);
    mainWindow.show();

  }
}
