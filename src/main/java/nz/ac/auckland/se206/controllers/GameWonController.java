package nz.ac.auckland.se206.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.BaseController;
import nz.ac.auckland.se206.SceneManager;

/**
 * Controller class for the game won view. This runs when player successfully steals the treasure
 * and escapes within the given time.
 */
public class GameWonController implements BaseController {

  private MediaPlayer mediaPlayer;

  /**
   * Initializes the controller class. This method is automatically called after the fxml file has
   * been loaded.
   */
  @FXML
  public void initialize() {
    String soundPath = getClass().getResource("/sounds/victory-96688.mp3").toString();
    if (soundPath == null) {
      System.err.println("Failed to load sound file.");
      return;
    }

    // Create a new media player and play the victory sound
    Media media = new Media(soundPath);
    mediaPlayer = new MediaPlayer(media);
    playVictorySound();
  }

  /** Play the victory sound. */
  public void playVictorySound() {
    if (mediaPlayer != null) {
      mediaPlayer.seek(Duration.ZERO);
      mediaPlayer.play();
    }
  }

  /**
   * Ends the game and exits the program.
   *
   * @param event the action event triggered by the go back button.
   */
  @FXML
  private void onExitGame(ActionEvent event) {
    App.switchScenes(SceneManager.AppUi.MAIN_MENU);

    // Get the MainMenuController and replay the menu music
    MainMenuController mainMenuController =
        (MainMenuController) SceneManager.getUiController(SceneManager.AppUi.MAIN_MENU);
    if (mainMenuController != null) {
      mainMenuController.replayMenuMusic();
    }
  }
}
