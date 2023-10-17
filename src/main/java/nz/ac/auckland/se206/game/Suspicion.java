package nz.ac.auckland.se206.game;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.ProgressBar;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import nz.ac.auckland.se206.listeners.SuspicionListener;

/**
 * This class represents the suspicion object in the game. The player can interact with this object
 * to raise their suspicion level. If the suspicion level reaches the maximum, the player will lose
 * the game.
 */
public class Suspicion extends Interactable {

  private SuspicionListener listener;
  private Timeline timeline;
  private double suspicionLevel = 0.0;
  private double maximumSuspicionLevel = 5.0;
  private ProgressBar progressBar;
  private Rectangle suspicionLight;
  private double minimumOpactiy = 0.1;
  private double maximumOpacity = 0.5;
  private boolean maximumOpacityReached = false;

  /**
   * This constructor creates a new suspicion object.
   *
   * @param rectangle The rectangle that represents the suspicion object.
   * @param listener The listener for the suspicion object.
   * @param progressBar The progress bar for the suspicion object.
   * @param suspicionLight The red light that flashes when the player is raising their suspicion.
   */
  public Suspicion(
      Rectangle rectangle,
      SuspicionListener listener,
      ProgressBar progressBar,
      Rectangle suspicionLight) {
    super(rectangle);
    this.listener = listener;
    this.progressBar = progressBar;
    this.suspicionLight = suspicionLight;
    suspicionLight.setOpacity(0.0);
    timeline = new Timeline(new KeyFrame(Duration.millis(100), e -> updateSuspicion()));
    timeline.setCycleCount(Timeline.INDEFINITE);
  }

  /** This method updates the suspicion level of the player. */
  private void updateSuspicion() {
    if (touched) { // If the player is raising their suspicion
      incrementTask();
    } else {
      decrementTask();
    }
  }

  /** This method decrements the suspicion level of the player. */
  private void decrementTask() {
    suspicionLevel -= 0.1;
    if (suspicionLevel <= 0.0) {
      suspicionLevel = 0.0;
      timeline.pause();
      progressBar.toBack();
    }
    progressBar.setProgress(
        (1.0 / maximumSuspicionLevel) * suspicionLevel); // Update the progress bar
  }

  /** This method increments the suspicion level of the player. */
  private void incrementTask() {
    flashLight();
    suspicionLevel += 0.1;
    if (suspicionLevel >= maximumSuspicionLevel) { // If the suspicion level is at the maximum
      suspicionLevel = maximumSuspicionLevel;
      suspicionLevel = 0.0;
      listener.suspicionReached();
    }
    progressBar.setProgress(
        (1.0 / maximumSuspicionLevel) * suspicionLevel); // Update the progress bar
  }

  /**
   * This method flashes the red light across the screen when the player is raising their suspicion.
   */
  protected void flashLight() {
    checkOpacity();
    if (maximumOpacityReached) {
      suspicionLight.setOpacity(suspicionLight.getOpacity() - 0.1); // Decrease the opacity
    } else {
      suspicionLight.setOpacity(suspicionLight.getOpacity() + 0.1); // Increase the opacity
    }
  }

  /** This method checks if the opacity of the red light is at the maximum or minimum. */
  private void checkOpacity() {
    double opacity = suspicionLight.getOpacity();
    if (opacity >= maximumOpacity) { // If the opacity is at the maximum
      maximumOpacityReached = true;
    } else if (opacity <= minimumOpactiy) { // If the opacity is at the minimum
      maximumOpacityReached = false;
    }
  }

  /** This method runs when the player is interacting with the object. */
  @Override
  public void interact() {}

  /** This method runs when the player is touching the object. */
  @Override
  public void touched() {
    if (touched) {
      return;
    }
    timeline.play();
    listener.suspicionTouched();
    touched = true;
  }

  /** This method runs when the player is no longer touching the object. */
  @Override
  public void notTouched() {
    if (!touched) {
      return;
    }
    suspicionLight.setOpacity(0.0);
    listener.suspicionNotTouched();

    touched = false;
  }
}
