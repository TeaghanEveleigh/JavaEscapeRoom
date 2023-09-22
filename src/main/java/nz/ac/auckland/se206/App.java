package nz.ac.auckland.se206;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.controllers.GameController;

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

  private static FXMLLoader getFxmlLoader(final String fxml) {
    return new FXMLLoader(App.class.getResource("/fxml/" + fxml + ".fxml"));
  }

  /**
   * This method is invoked when the application starts. It loads and shows the "Canvas" scene.
   *
   * @param stage The primary stage of the application.
   * @throws IOException If "src/main/resources/fxml/canvas.fxml" is not found.
   */
  @Override
  public void start(final Stage stage) throws IOException {
    Timers.getInstance().initializeMainCountdown(2);

    // Load all the views
    FXMLLoader mainMenuLoader = getFxmlLoader("mainmenu");
    SceneManager.addUi(AppUi.MAIN_MENU, mainMenuLoader.load());
    SceneManager.addController(AppUi.MAIN_MENU, mainMenuLoader.getController());

    FXMLLoader gameSettingsLoader = getFxmlLoader("gamesettings");
    SceneManager.addUi(AppUi.GAME_SETTINGS, gameSettingsLoader.load());
    SceneManager.addController(AppUi.GAME_SETTINGS, gameSettingsLoader.getController());

    FXMLLoader wiresLoader = getFxmlLoader("wires");
    SceneManager.addUi(AppUi.WIRES_GAME, wiresLoader.load());
    SceneManager.addController(AppUi.WIRES_GAME, wiresLoader.getController());

    FXMLLoader dinosaurRoomLoader = getFxmlLoader("room1");
    SceneManager.addUi(AppUi.DINOSAUR_ROOM, dinosaurRoomLoader.load());
    SceneManager.addController(AppUi.DINOSAUR_ROOM, dinosaurRoomLoader.getController());

    FXMLLoader exitRoomLoader = getFxmlLoader("securityroom");
    SceneManager.addUi(AppUi.EXIT_ROOM, exitRoomLoader.load());
    SceneManager.addController(AppUi.EXIT_ROOM, exitRoomLoader.getController());

    FXMLLoader securityRoomLoader = getFxmlLoader("room2");
    SceneManager.addUi(AppUi.SECURITY_ROOM, securityRoomLoader.load());
    SceneManager.addController(AppUi.SECURITY_ROOM, securityRoomLoader.getController());

    FXMLLoader memoryGameLoader = getFxmlLoader("memorygame");
    SceneManager.addUi(AppUi.MEMORY_GAME, memoryGameLoader.load());
    SceneManager.addController(AppUi.MEMORY_GAME, memoryGameLoader.getController());

    scene = new Scene(SceneManager.getUiRoot(AppUi.EXIT_ROOM), 816, 585);
    Parent root = SceneManager.getUiRoot(AppUi.EXIT_ROOM);
    GameController controller = (GameController) SceneManager.getUiController(AppUi.EXIT_ROOM);
    controller.unpauseRoom();

    stage.setScene(scene);
    stage.show();
    root.requestFocus();
  }

  public static void switchScenes(AppUi ui) {
    Parent root = SceneManager.getUiRoot(ui);
    BaseController baseController = SceneManager.getUiController(ui);
    if (baseController instanceof GameController) {
      GameController gameController = (GameController) baseController;
      gameController.unpauseRoom();
    }
    scene.setRoot(root);
    root.requestFocus();
    KeyState.resetKeys();
  }
}
