package nz.ac.auckland.se206;

import java.util.HashMap;
import java.util.Stack;
import javafx.scene.Parent;

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
  }
}
