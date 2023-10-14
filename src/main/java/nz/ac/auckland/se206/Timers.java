package nz.ac.auckland.se206;

import java.util.ArrayList;
import java.util.List;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.util.Duration;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class Timers {

  private static Timers instance;

  public static Timers getInstance() {
    if (instance == null) {
      instance = new Timers();
    }
    return instance;
  }

  private int startingTimeInMilliseconds;
  private int timeInMilliseconds;
  private Timeline countdownTimeline;
  private List<Label> subscribedLabels = new ArrayList<>();
  private boolean is30SecondTriggered = false;

  private Timers() {
    // private constructor to enforce singleton
  }

  public void initializeMainCountdown(int minutes) {
    startingTimeInMilliseconds = minutes * 60 * 1000;
    timeInMilliseconds = startingTimeInMilliseconds;
    countdownTimeline = createCountdownTimeline();
    countdownTimeline.setCycleCount(Timeline.INDEFINITE);
    countdownTimeline.play();
  }

  public void subscribeLabel(Label label) {
    subscribedLabels.add(label);
    Font font = Font.loadFont(getClass().getResource("/fonts/DS-DIGIB.TTF").toExternalForm(), 40);
    label.setFont(font);
 
  }

  public void subtractTime(int seconds) {
    timeInMilliseconds -= seconds * 1000;
    if (timeInMilliseconds < 0) {
      timeInMilliseconds = 0;
    }
  }

  private void thirtySecondPassed() {

    App.switchScenes(AppUi.SIN_MINIGAME);
    System.out.println("30 seconds have passed.");
  }

  private Timeline createCountdownTimeline() {
    return new Timeline(
        new KeyFrame(
            Duration.seconds(1),
            e -> {
              timeInMilliseconds -= 1000;
              if (timeInMilliseconds < 0) {
                timeInMilliseconds = 0;
                countdownTimeline.stop();
              }
              if ((timeInMilliseconds / 1000 % 60) < 10) {
                String time =
                    (timeInMilliseconds / 1000 / 60) + " : 0" + (timeInMilliseconds / 1000 % 60);
                for (Label label : subscribedLabels) {
                  Platform.runLater(() -> label.setText(time));
                }
              } else {
                String time =
                    (timeInMilliseconds / 1000 / 60) + " : " + (timeInMilliseconds / 1000 % 60);
                for (Label label : subscribedLabels) {
                  Platform.runLater(() -> label.setText(time));
                }
              }
              if (timeInMilliseconds <= 15000 && timeInMilliseconds > 0) {
                Timeline flashTimeline = new Timeline(
                    new KeyFrame(Duration.seconds(0.5), ev -> {
                        for (Label label : subscribedLabels) {
                            if ("RED".equals(label.getTextFill().toString())) {
                                Platform.runLater(() -> label.setStyle("-fx-text-fill: black;"));
                            } else {
                                Platform.runLater(() -> label.setStyle("-fx-text-fill: red;"));
                            }
                        }
                    })
                );
                flashTimeline.setCycleCount(30); // Flash for 15 seconds
                flashTimeline.play();
              }
              if (timeInMilliseconds == 0) {
                App.switchScenes(AppUi.GAME_LOST);
              }
              if (!is30SecondTriggered
                  && timeInMilliseconds <= (startingTimeInMilliseconds - 30000)) {
                thirtySecondPassed();
                is30SecondTriggered = true;
              }
            }));
  }
}
