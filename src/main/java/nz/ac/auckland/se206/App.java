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

  public static FXMLLoader getFxmlLoader(final String fxml) {
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

    // Load all the views
    FXMLLoader mainMenuLoader = getFxmlLoader("mainmenu");
    SceneManager.addUi(AppUi.MAIN_MENU, mainMenuLoader.load());
    SceneManager.addController(AppUi.MAIN_MENU, mainMenuLoader.getController());


    FXMLLoader gameSettingsLoader = getFxmlLoader("gamesettings");
    SceneManager.addUi(AppUi.GAME_SETTINGS, gameSettingsLoader.load());
    SceneManager.addController(AppUi.GAME_SETTINGS, gameSettingsLoader.getController());

    FXMLLoader gameWonLoader = getFxmlLoader("gamewon");
    SceneManager.addUi(AppUi.GAME_WON, gameWonLoader.load());
    SceneManager.addController(AppUi.GAME_WON, gameWonLoader.getController());

    FXMLLoader gameLostLoader = getFxmlLoader("gamelost");
    SceneManager.addUi(AppUi.GAME_LOST, gameLostLoader.load());
    SceneManager.addController(AppUi.GAME_LOST, gameLostLoader.getController());

    scene = new Scene(SceneManager.getUiRoot(AppUi.MAIN_MENU), 816, 585);
    Parent root = SceneManager.getUiRoot(AppUi.MAIN_MENU);
    // GameController controller = (GameController) SceneManager.getUiController(AppUi.EXIT_ROOM);
    // controller.unpauseRoom();

    stage.setScene(scene);
    stage.show();
    root.requestFocus();
  }

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
            return;  // If an error occurs, exit the method
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
    System.out.println("Switching to scene: " + previousScene);  // Debug output
    switchScenes(previousScene);
  } else {
    System.out.println("Previous scene is null");  // Debug output
  }
}

}
