package nz.ac.auckland.se206;

import java.util.HashMap;
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
    EXIT_ROOM
  }

  private static HashMap<AppUi, Parent> sceneMap = new HashMap<AppUi, Parent>();
  private static HashMap<AppUi, BaseController> controllerMap =
      new HashMap<AppUi, BaseController>();

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
}
