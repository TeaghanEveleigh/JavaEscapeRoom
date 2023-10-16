package nz.ac.auckland.se206;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
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
  private static HashMap<AppUi, String> fxmlMap;
  private static Set<AppUi> gameRooms;

  public static void main(final String[] args) {
    launch();
  }

  public static void setRoot(AppUi appUi) throws IOException {
    scene.setRoot(SceneManager.getUiRoot(appUi));
  }

  public static FXMLLoader getFxmlLoader(final String fxml) {
    return new FXMLLoader(App.class.getResource("/fxml/" + fxml + ".fxml"));
  }

  /**
   * This method is invoked when the application starts. It loads and shows the "Canvas" scene.
   *
   * @param stage The primary stage of the application.
   * @throws IOException If "src/main/resources/fxml/canvas.fxml" is not found.
   */
  public static void switchScenes(AppUi ui) {
    Parent root = SceneManager.getUiRoot(ui);
    BaseController baseController = SceneManager.getUiController(ui);
    baseController.start();
    if (baseController instanceof GameController) {
      GameController gameController = (GameController) baseController;
      gameController.unpauseRoom();
    }

    // Important: Add the current UI to history before switching the root
    SceneManager.addToHistory(ui);

    scene.setRoot(root);
    root.requestFocus();
    KeyState.resetKeys();
  }

  public static void goToPreviousScene() {
    AppUi previousScene = SceneManager.getLastScene();
    if (previousScene != null) {
      System.out.println("Switching to scene: " + previousScene); // Debug output
      switchScenes(previousScene);
    } else {
      System.out.println("Previous scene is null"); // Debug output
    }
  }

  @Override
  public void start(final Stage stage) throws IOException {

    Parent loadingRoot = SceneManager.initializeLoadingScreen("loading");
    scene = new Scene(loadingRoot, 816, 585);
    stage.setScene(scene);
    stage.show();
    loadingRoot.requestFocus();

    initializeFxmlMap();
    initializeGameRooms();
    SceneManager.reloadScenes(fxmlMap);
  }

  private static void initializeFxmlMap() {
    fxmlMap = new HashMap<AppUi, String>();
    fxmlMap.put(AppUi.MAIN_MENU, "mainmenu");
    fxmlMap.put(AppUi.GAME_SETTINGS, "gamesettings");
    fxmlMap.put(AppUi.GAME_WON, "gamewon");
    fxmlMap.put(AppUi.GAME_LOST, "gamelost");
    fxmlMap.put(AppUi.DINOSAUR_ROOM, "room1");
    fxmlMap.put(AppUi.SECURITY_ROOM, "room2");
    fxmlMap.put(AppUi.EXIT_ROOM, "securityroom");
    fxmlMap.put(AppUi.SIN_MINIGAME, "frequencyMinigame");
    fxmlMap.put(AppUi.WIRES_GAME, "wires");
    fxmlMap.put(AppUi.MEMORY_GAME, "memorygame");
  }

  private static void initializeGameRooms() {
    gameRooms = Set.of(AppUi.DINOSAUR_ROOM, AppUi.SECURITY_ROOM, AppUi.EXIT_ROOM);
  }

  public static void restartGame() throws IOException {
    Parent root = SceneManager.getLoadingParent();
    scene.setRoot(root);
    GameState.resetGameState();
    KeyState.resetKeys();
    Passcode.resetPasscode();
    Timers.reset();
    root.requestFocus();
    SceneManager.restartScenes(fxmlMap, gameRooms);
  }
}
