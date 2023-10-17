package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.BaseController;
import nz.ac.auckland.se206.GameState;
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
    return;
  }

  @FXML
  private void onSettingsPressed(ActionEvent event) throws IOException {
    App.switchScenes(AppUi.GAME_SETTINGS);
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
    Timers.getInstance().initializeMainCountdown(GameState.timeLimit);
    App.switchScenes(AppUi.DINOSAUR_ROOM);
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

  @Override
  public void start() {
    startSound.play();
  }
}
