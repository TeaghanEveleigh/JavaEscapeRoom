package nz.ac.auckland.se206;

import java.util.HashMap;
import javafx.scene.Parent;

/** Manages the different views of the application. */
public class SceneManager {
  public enum AppUi {
    MAIN_MENU,
    GAME_SETTINGS,
    WIRES_GAME,
    SIN_MINIGAME,
    SECURITY_ROOM,
    DINOSAUR_ROOM,
    EXIT_ROOM
    

  }

  private static HashMap<AppUi, Parent> sceneMap = new HashMap<AppUi, Parent>();

  public static void addUi(AppUi appUi, Parent uiRoot) {
    sceneMap.put(appUi, uiRoot);
  }

  public static Parent getUiRoot(AppUi appUi) {
    return sceneMap.get(appUi);
  }
}
