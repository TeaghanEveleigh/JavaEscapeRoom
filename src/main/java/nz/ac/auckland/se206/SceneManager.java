package nz.ac.auckland.se206;

import java.io.IOException;
import java.util.HashMap;
import java.util.Stack;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import nz.ac.auckland.se206.controllers.LoadingMenuController;

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
  private static Parent loadingRoot;
  private static LoadingMenuController loadingController;

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
}
