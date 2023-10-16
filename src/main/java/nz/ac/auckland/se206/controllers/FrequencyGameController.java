package nz.ac.auckland.se206.controllers;

import java.time.LocalTime;
import java.util.Random;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.BaseController;
import nz.ac.auckland.se206.Timers;

public class FrequencyGameController implements BaseController {

  private String policeSound =
      getClass().getResource("/sounds/618971__mrrap4food__radio-police-inside-car.mp3").toString();
  private Media media = new Media(policeSound);
  private MediaPlayer startSound = new MediaPlayer(media);

  private boolean matched = false;
  @FXML private Button backButton;
  @FXML private Text guideTop;
  @FXML private Text guideBottom;
  @FXML private Text failureText;
  @FXML private Text timer;
  @FXML private Rectangle blurryscreen;
  @FXML private Text sucessTxt;
  @FXML private Slider amplitudeSlider;

  @FXML private Slider frequencySlider;

  @FXML private Path targetSineWave;

  @FXML private Path userSineWave;
  private boolean hasLost = false;
  private boolean hasWon = false;

  private Timeline countdownTimeline;
  private int timeInSeconds = 10000; // 10 seconds * 1000 milliseconds

  @FXML
  public void initialize() {
    amplitudeSlider.valueProperty().addListener((obs, oldVal, newVal) -> updateWave());
    frequencySlider.valueProperty().addListener((obs, oldVal, newVal) -> updateWave());
    Font font = Font.loadFont(getClass().getResource("/fonts/DS-DIGIB.TTF").toExternalForm(), 40);
    timer.setFont(font);
    // Set a random wave without updating immediately.
    setRandomInitialWave(false);
    drawTargetWave();
  }

  private void setRandomInitialWave(boolean updateImmediately) {
    Random random = new Random(LocalTime.now().toNanoOfDay());

    // Temporarily remove listeners
    amplitudeSlider.valueProperty().removeListener((obs, oldVal, newVal) -> updateWave());
    frequencySlider.valueProperty().removeListener((obs, oldVal, newVal) -> updateWave());

    // Ensure amplitude is not 50 to avoid a match
    double amplitude;
    do {
      amplitude = random.nextDouble() * amplitudeSlider.getMax();
    } while (Math.abs(amplitude - 50) < 5); // Ensure a minimum difference

    // Ensure frequency is not matching the target frequency to avoid a match
    double frequency;
    do {
      frequency =
          frequencySlider.getMin()
              + random.nextDouble() * (frequencySlider.getMax() - frequencySlider.getMin());
    } while (Math.abs(frequency - 0.01) < 0.005); // Ensure a minimum difference

    amplitudeSlider.setValue(amplitude);
    frequencySlider.setValue(frequency);

    // Restore listeners
    amplitudeSlider.valueProperty().addListener((obs, oldVal, newVal) -> updateWave());
    frequencySlider.valueProperty().addListener((obs, oldVal, newVal) -> updateWave());

    if (updateImmediately) {
      updateWave();
    }
  }

  private void initializeCountdown() {
    startSound.play();
    countdownTimeline =
        new Timeline(
            new KeyFrame(
                Duration.millis(1),
                e -> {
                  timeInSeconds--;

                  int seconds = timeInSeconds / 1000;
                  timer.setText(String.format("%2d", seconds));

                  if (seconds <= 3) {
                    timer.setStyle("-fx-text-fill: red;"); // Changing text color to red
                  }

                  if (timeInSeconds <= 0 && matched != true) {
                    countdownTimeline.stop();
                    gameOver("timer_done");
                  }
                }));

    countdownTimeline.setCycleCount(10000);
    countdownTimeline.play();
  }

  private void drawTargetWave() {
    targetSineWave.getElements().add(new MoveTo(0, 150));
    for (int i = 0; i <= 480; i += 10) {
      double y = 150 + 50 * Math.sin(0.01 * i);
      targetSineWave.getElements().add(new LineTo(i, y));
    }
  }

  private void updateWave() {
    userSineWave.getElements().clear();

    double amplitude = amplitudeSlider.getValue();
    double frequency = frequencySlider.getValue();

    userSineWave.getElements().add(new MoveTo(0, 150));
    for (int i = 0; i <= 480; i += 10) {
      double y = 150 + amplitude * Math.sin(frequency * i);
      userSineWave.getElements().add(new LineTo(i, y));
    }

    if (wavesMatch()) {
      userSineWave.setOpacity(0);
      matched = true;
      gameOver("game_won");
    }
  }

  @FXML
  private void onBackPressed() {
    // Handle back button press here

    App.goToPreviousScene();
  }

  private boolean wavesMatch() {
    double amplitude = amplitudeSlider.getValue();
    double frequency = frequencySlider.getValue();
    for (int i = 0; i <= 480; i += 10) {
      double targetY = 150 + 50 * Math.sin(0.01 * i);
      double userY = 150 + amplitude * Math.sin(frequency * i);
      if (Math.abs(targetY - userY) > 5) { // 5 is an arbitrary threshold, adjust as needed
        return false;
      }
    }
    return true;
  }

  private void gameOver(String reason) {
    guideBottom.setOpacity(0);
    guideTop.setOpacity(0);
    startSound.stop();
    switch (reason) {
      case "timer_done":
        // Handle the game over due to timer running out.
        if (hasWon != true) {
          targetSineWave.setStroke(javafx.scene.paint.Color.RED);
          userSineWave.setStroke(javafx.scene.paint.Color.RED);
          amplitudeSlider.setDisable(true);
          frequencySlider.setDisable(true);
          blurryscreen.toFront();
          failureText.toFront();
          // Handle other logic if needed.
          hasLost = true;
          Timers mainTimer = Timers.getInstance();
          mainTimer.subtractTime(10);
        }
        break;
      case "game_won":
        if (hasLost != true) {
          // Handle the game being won.
          // If needed, additional logic for when the game is won can be added here.
          targetSineWave.setStroke(javafx.scene.paint.Color.GREEN);
          userSineWave.setStroke(javafx.scene.paint.Color.GREEN);
          amplitudeSlider.setDisable(true);
          frequencySlider.setDisable(true);
          blurryscreen.toFront();
          sucessTxt.toFront();
          countdownTimeline.stop();
        }
        break;
      default:
        // Handle any other scenarios or ignore.
        break;
    }
    backButton.toFront();
    backButton.setOpacity(1);
  }

  @Override
  public void start() {
    PauseTransition delay =
        new PauseTransition(Duration.seconds(2)); // Delay for 2 seconds or adjust as needed
    delay.setOnFinished(e -> updateWave()); // Start the game's matching mechanism after the delay
    delay.play();

    initializeCountdown();
  }
}
