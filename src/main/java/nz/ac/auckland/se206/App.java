package nz.ac.auckland.se206;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nz.ac.auckland.se206.SceneManager.AppUi;

/**
 * This is the entry point of the JavaFX application, while you can change this class, it should
 * remain as the class that runs the JavaFX application.
 */
public class App extends Application {

  private static Scene scene;

  public static void main(final String[] args) {
    launch();
  }

  public static void setRoot(AppUi appUi) throws IOException {
    scene.setRoot(SceneManager.getUiRoot(appUi));
  }

  /**
   * Returns the node associated to the input file. The method expects that the file is located in
   * "src/main/resources/fxml".
   *
   * @param fxml The name of the FXML file (without extension).
   * @return The node of the input file.
   * @throws IOException If the file is not found.
   */
  private static Parent loadFxml(final String fxml) throws IOException {
    return new FXMLLoader(App.class.getResource("/fxml/" + fxml + ".fxml")).load();
  }

  /**
   * This method is invoked when the application starts. It loads and shows the "Canvas" scene.
   *
   * @param stage The primary stage of the application.
   * @throws IOException If "src/main/resources/fxml/canvas.fxml" is not found.
   */
  @Override
  public void start(final Stage stage) throws IOException {
    // Load all the views
    SceneManager.addUi(AppUi.MAIN_MENU, loadFxml("mainmenu"));
    SceneManager.addUi(AppUi.GAME_SETTINGS, loadFxml("gamesettings"));
    SceneManager.addUi(AppUi.WIRES_GAME, loadFxml("wires"));
    SceneManager.addUi(AppUi.DINOSAUR_ROOM, loadFxml("room1"));
    SceneManager.addUi(AppUi.SECURITY_ROOM, loadFxml("room2"));
    SceneManager.addUi(AppUi.KEYPAD_ROOM, loadFxml("securityroom"));

    scene = new Scene(SceneManager.getUiRoot(AppUi.KEYPAD_ROOM), 816, 585);
    Parent root = SceneManager.getUiRoot(AppUi.KEYPAD_ROOM);
    stage.setScene(scene);
    stage.show();
    root.requestFocus();
  }

  public static void switchScenes(AppUi ui) {
    Parent root = SceneManager.getUiRoot(ui);
    scene.setRoot(root);
    root.requestFocus();
    KeyState.resetKeys();
  }
}
