package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.animation.PauseTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.Window;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.BaseController;
import nz.ac.auckland.se206.GameState;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.Timers;

/**
 * Controller for the main menu. This is where the player starts when they first load into the
 * application and where they can start the game, view the tutorial, or change the game settings.
 */
public class MainMenuController implements BaseController {
  @FXML private Button startGameButton;
  @FXML private Button tutorialButton;
  @FXML private Button gameSettingButton;
  @FXML private Text loadingText;
  @FXML private ProgressBar loadingBar;
  @FXML private ImageView loadingScreen;

  private String policeSound = getClass().getResource("/sounds/intense.mp3").toString();
  private Media media = new Media(policeSound);
  private MediaPlayer startSound = new MediaPlayer(media);

  /**
   * Initializes the controller class. This method is automatically called after the fxml file has
   * been loaded.
   */
  public void initialize() {
    startSound.play();

    // Adds a listener to the sceneProperty of the loadingBar object
    loadingBar
        .sceneProperty()
        .addListener(
            new ChangeListener<Scene>() {
              @Override
              public void changed(
                  ObservableValue<? extends Scene> observable, Scene oldScene, Scene newScene) {
                if (newScene != null) {
                  // Adds a listener to the window property of the new scene
                  newScene
                      .windowProperty()
                      .addListener(
                          new ChangeListener<Window>() {
                            @Override
                            public void changed(
                                ObservableValue<? extends Window> observable,
                                Window oldWindow,
                                Window newWindow) {
                              if (newWindow != null) {
                                // Adds a listener to the showing property of the new window
                                newWindow
                                    .showingProperty()
                                    .addListener(
                                        new ChangeListener<Boolean>() {
                                          @Override
                                          public void changed(
                                              ObservableValue<? extends Boolean> observable,
                                              Boolean oldValue,
                                              Boolean newValue) {
                                            if (newValue) {
                                              // Play sound when loading screen is shown
                                              startSound.stop();
                                              startSound.seek(Duration.ZERO);
                                              startSound.play();
                                            }
                                          }
                                        });
                              }
                            }
                          });

                  // Hide loading screen when loading is complete
                  loadingBar.setProgress(0);
                  loadingScreen.toBack();
                  loadingBar.toBack();
                  loadingText.toBack();
                }
              }
            });
  }

  @FXML
  private void onSettingsPressed(ActionEvent event) throws IOException {
    App.setRoot(AppUi.GAME_SETTINGS);
  }

  /**
   * This method is used to opens the tutorial when the tutorial button is pressed.
   *
   * @param event The event that triggered this method.
   * @throws IOException If the fxml file cannot be loaded.
   */
  @FXML
  private void onTutorialPressed(ActionEvent event) throws IOException {
    // TODO: implement
  }

  /**
   * This method is used to start the game when the start button is pressed.
   *
   * @param event The event that triggered this method.
   * @throws IOException If the fxml file cannot be loaded.
   */
  @FXML
  private void onStartPressed(ActionEvent event) {
    // Show loading screen
    loadingScreen.toFront();
    loadingText.toFront();
    loadingBar.toFront();

    // Pause for 1 second before loading
    PauseTransition pause = new PauseTransition(Duration.seconds(1));
    pause.setOnFinished(e -> startLoading());
    pause.play();
  }

  /** Starts loading the game rooms of the game. */
  private void startLoading() {
    try {

      // Load and show progress in steps
      loadRoom1();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Loads the dinosaur room of the game.
   *
   * @throws IOException If the fxml file cannot be loaded.
   */
  private void loadRoom1() throws IOException {
    // Load dinosaur room
    FXMLLoader dinosaurRoomLoader = App.getFxmlLoader("room1");
    SceneManager.addUi(AppUi.DINOSAUR_ROOM, dinosaurRoomLoader.load());
    SceneManager.addController(AppUi.DINOSAUR_ROOM, dinosaurRoomLoader.getController());
    loadingBar.setProgress(0.33); // Updated to 33%

    // Pause for 250ms before loading next room
    PauseTransition pause = new PauseTransition(Duration.millis(250));
    pause.setOnFinished(
        e -> {
          try {
            loadRoom2();
          } catch (IOException ioException) {
            ioException.printStackTrace();
          }
        });
    pause.play();
  }

  /**
   * Loads the exit room of the game.
   *
   * @throws IOException If the fxml file cannot be loaded.
   */
  private void loadRoom2() throws IOException {
    // Load exit room
    FXMLLoader exitRoomLoader = App.getFxmlLoader("securityroom");
    SceneManager.addUi(AppUi.EXIT_ROOM, exitRoomLoader.load());
    SceneManager.addController(AppUi.EXIT_ROOM, exitRoomLoader.getController());
    loadingBar.setProgress(0.66); // Updated to 66%

    // Pause for 250ms before loading next room
    PauseTransition pause = new PauseTransition(Duration.millis(250));
    pause.setOnFinished(
        e -> {
          try {
            loadRoom3();
          } catch (IOException ioException) {
            ioException.printStackTrace();
          }
        });
    pause.play();
  }

  /**
   * Loads the security room of the game.
   *
   * @throws IOException If the fxml file cannot be loaded.
   */
  private void loadRoom3() throws IOException {
    // Load security room
    FXMLLoader securityRoomLoader = App.getFxmlLoader("room2");
    SceneManager.addUi(AppUi.SECURITY_ROOM, securityRoomLoader.load());
    SceneManager.addController(AppUi.SECURITY_ROOM, securityRoomLoader.getController());
    loadingBar.setProgress(1); // Updated to 100%

    // Pause for 250ms before loading next room
    PauseTransition pause = new PauseTransition(Duration.millis(250));
    pause.setOnFinished(e -> App.switchScenes(AppUi.DINOSAUR_ROOM));
    pause.play();
    Timers.getInstance().initializeMainCountdown(GameState.timeLimit);
    startSound.pause();
  }

  /** This method is used to replay the menu music. */
  public void replayMenuMusic() {
    if (startSound != null) {
      // Replay the menu music
      startSound.stop();
      startSound.seek(Duration.ZERO);
      startSound.play();
    }
  }
}
