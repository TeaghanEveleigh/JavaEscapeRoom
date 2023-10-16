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

  public void initialize() {
    return;
  }

  @FXML
  private void onSettingsPressed(ActionEvent event) throws IOException {
    App.switchScenes(AppUi.GAME_SETTINGS);
  }

  @FXML
  private void onTutorialPressed(ActionEvent event) throws IOException {
    // TODO: implement
  }

  @FXML
  private void onStartPressed(ActionEvent event) {
    Timers.getInstance().initializeMainCountdown(GameState.timeLimit);
    App.switchScenes(AppUi.DINOSAUR_ROOM);
    startSound.pause();
  }

  public void replayMenuMusic() {
    if (startSound != null) {
      startSound.stop();
      startSound.seek(Duration.ZERO);
      startSound.play();
    }
  }
}
