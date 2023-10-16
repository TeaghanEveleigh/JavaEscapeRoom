package nz.ac.auckland.se206;

import java.io.IOException;
import java.util.HashMap;
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
    // Load the scene only if it's not already loaded or if it's a special case
    if (ui == AppUi.SIN_MINIGAME && !SceneManager.containsUi(AppUi.SIN_MINIGAME)) {
      try {
        FXMLLoader sinMinigLoader = App.getFxmlLoader("frequencyMinigame");
        Parent sinMinigameRoot = sinMinigLoader.load();
        SceneManager.addUi(AppUi.SIN_MINIGAME, sinMinigameRoot);
        SceneManager.addController(AppUi.SIN_MINIGAME, sinMinigLoader.getController());
      } catch (IOException e) {
        e.printStackTrace();
        return; // If an error occurs, exit the method
      }
    }

    Parent root = SceneManager.getUiRoot(ui);
    BaseController baseController = SceneManager.getUiController(ui);
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

    initializeFxmlMap();
    SceneManager.reloadScenes(fxmlMap);

    scene = new Scene(SceneManager.getUiRoot(AppUi.MAIN_MENU), 816, 585);
    Parent root = SceneManager.getUiRoot(AppUi.MAIN_MENU);
    stage.setScene(scene);
    stage.show();
    root.requestFocus();
  }

  private static void initializeFxmlMap() {
    fxmlMap = new HashMap<AppUi, String>();
    fxmlMap.put(AppUi.MAIN_MENU, "mainmenu");
    fxmlMap.put(AppUi.GAME_SETTINGS, "gamesettings");
    fxmlMap.put(AppUi.GAME_WON, "gamewon");
    fxmlMap.put(AppUi.GAME_LOST, "gamelost");
    fxmlMap.put(AppUi.DINOSAUR_ROOM, "room1");
    fxmlMap.put(AppUi.SECURITY_ROOM, "securityroom");
    fxmlMap.put(AppUi.EXIT_ROOM, "room2");
    fxmlMap.put(AppUi.SIN_MINIGAME, "frequencyMinigame");
    fxmlMap.put(AppUi.WIRES_GAME, "wires");
    fxmlMap.put(AppUi.MEMORY_GAME, "memorygame");
  }

  public static void restartGame() throws IOException {
    initializeFxmlMap();

    SceneManager.reloadScenes(fxmlMap);

    // Set the scene to the main menu
    scene.setRoot(SceneManager.getUiRoot(AppUi.MAIN_MENU));
  }

  public static void loadMainMenu() throws IOException {
    FXMLLoader mainMenuLoader = getFxmlLoader("mainmenu");
    SceneManager.addUi(AppUi.MAIN_MENU, mainMenuLoader.load());
    SceneManager.addController(AppUi.MAIN_MENU, mainMenuLoader.getController());
  }
}
