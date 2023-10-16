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

    // Load all the views
    loadMainMenu();

    // Load the game settings screen
    FXMLLoader gameSettingsLoader = getFxmlLoader("gamesettings");
    SceneManager.addUi(AppUi.GAME_SETTINGS, gameSettingsLoader.load());
    SceneManager.addController(AppUi.GAME_SETTINGS, gameSettingsLoader.getController());

    // Load the game lost screen
    FXMLLoader gameLostLoader = getFxmlLoader("gamelost");
    SceneManager.addUi(AppUi.GAME_LOST, gameLostLoader.load());
    SceneManager.addController(AppUi.GAME_LOST, gameLostLoader.getController());

    scene = new Scene(SceneManager.getUiRoot(AppUi.MAIN_MENU), 816, 585);
    Parent root = SceneManager.getUiRoot(AppUi.MAIN_MENU); //
    stage.setScene(scene); // Sets the scene to the main menu
    stage.show();
    root.requestFocus();
  }

  /**
   * This method is run when the game is restarted after it is either won or lost.
   *
   * @throws IOException If the FXML file for the main menu is not found.
   */
  public static void restartGame() throws IOException {
    // Clear previously loaded scenes and controllers
    SceneManager.clearAll();

    // Reload all the views
    loadMainMenu();

    // Load the game settings screen
    FXMLLoader gameSettingsLoader = getFxmlLoader("gamesettings");
    SceneManager.addUi(AppUi.GAME_SETTINGS, gameSettingsLoader.load());
    SceneManager.addController(AppUi.GAME_SETTINGS, gameSettingsLoader.getController());

    // Load the game lost screen
    FXMLLoader gameLostLoader = getFxmlLoader("gamelost");
    SceneManager.addUi(AppUi.GAME_LOST, gameLostLoader.load());
    SceneManager.addController(AppUi.GAME_LOST, gameLostLoader.getController());

    // Set the scene to the main menu
    scene.setRoot(SceneManager.getUiRoot(AppUi.MAIN_MENU));
  }

  /**
   * This method loads the main menu of the game when it is stared or restarted.
   *
   * @throws IOException If the FXML file for the main menu is not found.
   */
  public static void loadMainMenu() throws IOException {
    FXMLLoader mainMenuLoader = getFxmlLoader("mainmenu");
    SceneManager.addUi(AppUi.MAIN_MENU, mainMenuLoader.load());
    SceneManager.addController(AppUi.MAIN_MENU, mainMenuLoader.getController());
  }
}
