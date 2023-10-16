package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.BaseController;

/** Controller class for the game won view. */
public class GameWonController implements BaseController {

  private MediaPlayer mediaPlayer;

  @FXML
  public void initialize() {
    String soundPath = getClass().getResource("/sounds/victory-96688.mp3").toString();
    if (soundPath == null) {
      System.err.println("Failed to load sound file.");
      return;
    }

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
   * Ends the game and exits the program
   *
   * @param event the action event triggered by the go back button
   * @throws IOException
   */
  @FXML
  private void onExitGame(ActionEvent event) throws IOException {
    App.restartGame();
  }

  @Override
  public void start() {
    return;
  }
}
