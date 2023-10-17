package nz.ac.auckland.se206;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.Stack;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import nz.ac.auckland.se206.controllers.GameController;
import nz.ac.auckland.se206.controllers.LoadingMenuController;

/**
 * Manages the different views of the application. Adds a set of controllers and fxml files that can
 * be accessed throughout the game.
 */
public class SceneManager {

  /** This enum is used to store the different views in the game. */
  public enum AppUi {
    MAIN_MENU,
    GAME_SETTINGS,
    MEMORY_GAME,
    WIRES_GAME,
    SIN_MINIGAME,
    SECURITY_ROOM,
    DINOSAUR_ROOM,
    EXIT_ROOM,
    GAME_WON,
    GAME_LOST;
  }

  private static Stack<AppUi> history = new Stack<>();
  private static AppUi currentRoom;
  private static HashMap<AppUi, Parent> sceneMap = new HashMap<AppUi, Parent>();
  private static HashMap<AppUi, BaseController> controllerMap =
      new HashMap<AppUi, BaseController>();
  private static Parent loadingRoot;
  private static LoadingMenuController loadingController;

  /**
   * This method adds the given scene to the history of all scenes.
   *
   * @param appUi The scene to add to the history.
   */
  public static void addToHistory(AppUi appUi) {
    if (appUi != AppUi.SIN_MINIGAME) { // Replace with your actual condition for rooms
      currentRoom = appUi;
    }
    history.push(appUi);
  }

  /**
   * This method gets the second last scene that was added to the history nd returns it.
   *
   * @return The previous scene.
   */
  public static AppUi getLastScene() {
    if (history.size() >= 2) {
      history.pop(); // Remove current scene
      return history.peek(); // Return previous scene without removing it
    } else if (currentRoom != null) {
      return currentRoom;
    }
    return AppUi.EXIT_ROOM; // Return null if there is no previous scene
  }

  /**
   * This method adds a UI to the collection of UIs.
   *
   * @param appUi The UI to add.
   * @param uiRoot The root of the UI.
   */
  public static void addUi(AppUi appUi, Parent uiRoot) {
    sceneMap.put(appUi, uiRoot);
  }

  /**
   * This method adds a controller to the collection of controllers.
   *
   * @param appUi The UI to add the controller to.
   * @param controller The controller to add.
   */
  public static void addController(AppUi appUi, BaseController controller) {
    controllerMap.put(appUi, controller);
  }

  /**
   * This method is used to get the root of the UI.
   *
   * @param appUi The UI to get the root of.
   * @return The root of the UI.
   */
  public static Parent getUiRoot(AppUi appUi) {
    return sceneMap.get(appUi);
  }

  /**
   * This method is used to get the controller of the UI that is related to a given app UI.
   *
   * @param appUi The UI to get the controller of.
   * @return The controller of the UI.
   */
  public static BaseController getUiController(AppUi appUi) {
    return controllerMap.get(appUi);
  }

  /**
   * This method is used to check if a scene hs been loaded and initialised.
   *
   * @param appUi The UI to check.
   * @return Whether the given UI is in the collection of UIs.
   */
  public static boolean containsUi(AppUi appUi) {
    return sceneMap.containsKey(appUi);
  }

  /** This method clears all UIs from the collection of UIs. */
  public static void clearAll() {
    sceneMap.clear();
    controllerMap.clear();
    history.clear();
    currentRoom = null;
    System.gc();
  }

  public static void reloadScenes(HashMap<AppUi, String> fxmlMap) {
    clearAll();

    loadingController.resetLoadingBar();

    Task<Void> loadingTask =
        new Task<Void>() {

          @Override
          protected Void call() throws Exception {

            double progress = 0.0;
            double increment = 1.0 / fxmlMap.size();

            for (HashMap.Entry<AppUi, String> entry : fxmlMap.entrySet()) {
              loadEntry(entry);
              progress += increment;
              loadingController.updateLoadingBar(progress);
            }

            return null;
          }
        };

    loadingTask.setOnSucceeded(
        e -> {
          System.out.println("Completed loading");
          App.switchScenes(AppUi.MAIN_MENU);
        });

    Thread loadingThread = new Thread(loadingTask);
    loadingThread.start();
  }

  public static void restartScenes(HashMap<AppUi, String> fxmlMap, Set<AppUi> gameRooms) {
    // Remove scenes that are not in the gameRooms set
    for (AppUi scene : AppUi.values()) {
      if (!gameRooms.contains(scene)) {
        controllerMap.remove(scene);
        sceneMap.remove(scene);
      }
    }
    history.clear();
    currentRoom = null;
    System.gc();

    loadingController.resetLoadingBar();

    Task<Void> loadingTask =
        new Task<Void>() {

          @Override
          protected Void call() throws Exception {

            double progress = 0.0;
            double increment = 1.0 / fxmlMap.size();

            for (HashMap.Entry<AppUi, String> entry : fxmlMap.entrySet()) {
              if (gameRooms.contains(entry.getKey())) {
                GameController gameController =
                    (GameController) SceneManager.getUiController(entry.getKey());
                gameController.reset();
              } else {
                loadEntry(entry);
              }
              progress += increment;
              loadingController.updateLoadingBar(progress);
            }

            return null;
          }
        };

    loadingTask.setOnSucceeded(
        e -> {
          System.out.println("Completed restarting");
          App.switchScenes(AppUi.MAIN_MENU);
        });

    Thread loadingThread = new Thread(loadingTask);
    loadingThread.start();
  }

  private static void loadEntry(HashMap.Entry<AppUi, String> entry) throws IOException {
    AppUi appUi = entry.getKey();
    String fxml = entry.getValue();
    System.out.println("loading " + fxml);
    FXMLLoader loader = App.getFxmlLoader(fxml);
    addUi(appUi, loader.load());
    addController(appUi, loader.getController());
  }

  public static Parent initializeLoadingScreen(String fxml) throws IOException {
    FXMLLoader loader = App.getFxmlLoader(fxml);
    loadingRoot = loader.load();
    loadingController = (LoadingMenuController) loader.getController();
    return loadingRoot;
  }

  public static Parent getLoadingParent() {
    return loadingRoot;
  }
}
