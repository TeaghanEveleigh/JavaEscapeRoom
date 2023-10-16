package nz.ac.auckland.se206;

import java.io.IOException;
import java.util.HashMap;
import java.util.Stack;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

/** Manages the different views of the application. */
public class SceneManager {
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

  public static void addToHistory(AppUi appUi) {
    if (appUi != AppUi.SIN_MINIGAME) { // Replace with your actual condition for rooms
      currentRoom = appUi;
    }
    history.push(appUi);
  }

  public static AppUi getLastScene() {
    if (history.size() >= 2) {
      history.pop(); // Remove current scene
      return history.peek(); // Return previous scene without removing it
    } else if (currentRoom != null) {
      return currentRoom;
    }
    return AppUi.EXIT_ROOM; // Return null if there is no previous scene
  }

  public static void addUi(AppUi appUi, Parent uiRoot) {
    sceneMap.put(appUi, uiRoot);
  }

  public static void addController(AppUi appUi, BaseController controller) {
    controllerMap.put(appUi, controller);
  }

  public static Parent getUiRoot(AppUi appUi) {
    return sceneMap.get(appUi);
  }

  public static BaseController getUiController(AppUi appUi) {
    return controllerMap.get(appUi);
  }

  // Checks if the UI is in the scene map
  public static boolean containsUi(AppUi appUi) {
    return sceneMap.containsKey(appUi);
  }

  public static void clearAll() {
    sceneMap.clear();
    controllerMap.clear();
  }

  public void reloadScenes(HashMap<AppUi, String> fxmlMap) throws IOException {
    clearAll();

    for (HashMap.Entry<AppUi, String> entry : fxmlMap.entrySet()) {
      AppUi appUi = entry.getKey();
      String fxml = entry.getValue();
      FXMLLoader loader = App.getFxmlLoader(fxml);
      addUi(appUi, loader.load());
      addController(appUi, loader.getController());
    }
  }
}
