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

  /**
   * This method sets the root of the scene to the given AppUi.
   *
   * @param appUi The AppUi to set the root to.
   * @throws IOException If the FXML file for the given AppUi is not found.
   */
  public static void setRoot(AppUi appUi) throws IOException {
    scene.setRoot(SceneManager.getUiRoot(appUi));
  }

  /**
   * This method returns a FXMLLoader for the given FXML file.
   *
   * @param fxml The name of the FXML file to load.
   * @return The FXMLLoader for the given FXML file.
   */
  public static FXMLLoader getFxmlLoader(final String fxml) {
    return new FXMLLoader(App.class.getResource("/fxml/" + fxml + ".fxml"));
  }

  /**
   * This method switches the scene to the given AppUi.
   *
   * @param ui The AppUi to switch to.
   */
  public static void switchScenes(AppUi ui) {
    Parent root = SceneManager.getUiRoot(ui);
    BaseController baseController = SceneManager.getUiController(ui);
    baseController.start();
    if (baseController instanceof GameController) { // If the controller is a GameController
      GameController gameController = (GameController) baseController;
      gameController.unpauseRoom();
    }

    // Important: Add the current UI to history before switching the root
    SceneManager.addToHistory(ui);

    scene.setRoot(root);
    root.requestFocus();
    KeyState.resetKeys();
  }

  /** This method switches the scene to the previous scene. */
  public static void goToPreviousScene() {
    AppUi previousScene = SceneManager.getLastScene();
    if (previousScene != null) {
      System.out.println("Switching to scene: " + previousScene); // Debug output
      switchScenes(previousScene);
    } else {
      System.out.println("Previous scene is null"); // Debug output
    }
  }

  /**
   * This method is called when the JavaFX application is started.
   *
   * @param stage The stage to set the scene to.
   * @throws IOException If the FXML file for the main menu is not found.
   */
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
    GameController.resetAllChecklists();
    root.requestFocus();
    SceneManager.restartScenes(fxmlMap, gameRooms);
  }
}
