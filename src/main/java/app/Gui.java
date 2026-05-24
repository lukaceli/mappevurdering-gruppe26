package app;

import java.io.IOException;
import javafx.application.Application;
import javafx.stage.Stage;
import windows.main.MainWindow;

/**
 * Entry point for the JavaFX application.
 */
public class Gui extends Application {

  /**
   * Starts the application by initializing and showing the main window.
   *
   * @param primaryStage the primary stage provided by the JavaFX runtime
   * @throws IOException if the main window fails to load
   */
  @Override
  public void start(Stage primaryStage) throws IOException {
    MainWindow mainWindow = new MainWindow(primaryStage);
    mainWindow.show();

  }
}
