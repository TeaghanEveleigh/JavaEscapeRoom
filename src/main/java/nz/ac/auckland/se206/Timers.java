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

/**
 * This class is used to make and track the timers used throughout the game. It implements the
 * Singleton pattern.
 */
public class Timers {

  private static Timers instance;
  private static Timeline countdownTimeline;
  private static List<Label> subscribedLabels = new ArrayList<>();
  private static boolean is30SecondTriggered = false;

  /**
   * This method is used to get the instance of the Timers singleton.
   *
   * @return The instance of the Timers singleton.
   */
  public static Timers getInstance() {
    if (instance == null) {
      instance = new Timers();
    }
    return instance;
  }

  private int startingTimeInMilliseconds;
  private int timeInMilliseconds;

  /** This constructor ensures the Singleton pattern is used. */
  private Timers() {
    // private constructor to enforce singleton
  }

  /**
   * This method initialises the main timer of the game with the given amount of time, depending on
   * the time settings chosen.
   *
   * @param minutes The amount of time to initialise the timer with.
   */
  public void initializeMainCountdown(int minutes) {
    startingTimeInMilliseconds = minutes * 60 * 1000; // Convert minutes to milliseconds
    timeInMilliseconds = startingTimeInMilliseconds;
    countdownTimeline = createCountdownTimeline();
    countdownTimeline.setCycleCount(Timeline.INDEFINITE); // Repeat forever
    countdownTimeline.play();
  }

  /**
   * This method subscribes a label to the main timer of the game.
   *
   * @param label The label to subscribe.
   */
  public void subscribeLabel(Label label) {
    subscribedLabels.add(label);
    Font font = Font.loadFont(getClass().getResource("/fonts/DS-DIGIB.TTF").toExternalForm(), 40);
    label.setFont(font);
    System.out.println("Subscribed label: " + label);
  }

  /**
   * This method subtracts an amount of time from the main timer of the game.
   *
   * @param seconds The amount of time to subtract.
   */
  public void subtractTime(int seconds) {
    timeInMilliseconds -= seconds * 1000;
    if (timeInMilliseconds < 0) {
      timeInMilliseconds = 0;
    }
  }

  /** This method switches to the frequency game when 30 seconds have passed. */
  private void thirtySecondPassed() {
    App.switchScenes(AppUi.SIN_MINIGAME);
    System.out.println("30 seconds have passed.");
  }

  /** This method creates the main timer of the game. */
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
              if ((timeInMilliseconds / 1000 % 60) < 10) { // If seconds is less than 10
                String time =
                    (timeInMilliseconds / 1000 / 60) + " : 0" + (timeInMilliseconds / 1000 % 60);
                for (Label label : subscribedLabels) {
                  Platform.runLater(
                      () -> label.setText(time)); // Update label based on the current time left
                }
              } else {
                String time =
                    (timeInMilliseconds / 1000 / 60)
                        + " : "
                        + (timeInMilliseconds
                            / 1000
                            % 60); // Update label based on the current time left
                for (Label label : subscribedLabels) {
                  Platform.runLater(
                      () -> label.setText(time)); // Update label based on the current time left
                }
              }
              if (timeInMilliseconds <= 15000
                  && timeInMilliseconds > 0) { // If time is less than 15 seconds
                Timeline flashTimeline =
                    new Timeline(
                        new KeyFrame(
                            Duration.seconds(0.5),
                            ev -> {
                              for (Label label : subscribedLabels) {
                                if ("RED".equals(label.getTextFill().toString())) {
                                  Platform.runLater(
                                      () ->
                                          label.setStyle(
                                              "-fx-text-fill: black;")); // Flash label between red
                                  // and black
                                } else {
                                  Platform.runLater(
                                      () ->
                                          label.setStyle(
                                              "-fx-text-fill: red;")); // Flash label between red
                                  // and black
                                }
                              }
                            }));
                flashTimeline.setCycleCount(30); // Flash for 15 seconds
                flashTimeline.play();
              }
              if (timeInMilliseconds == 0) { // If time is up
                App.switchScenes(AppUi.GAME_LOST);
              }
              if (!is30SecondTriggered
                  && timeInMilliseconds
                      <= (startingTimeInMilliseconds - 30000)) { // If 30 seconds have passed
                thirtySecondPassed();
                is30SecondTriggered = true;
              }
            }));
  }

  /** This method resets the main timer of the game. */
  public static void reset() {
    subscribedLabels.clear();
    countdownTimeline.stop();
    countdownTimeline = null;
    is30SecondTriggered = false;
  }
}
