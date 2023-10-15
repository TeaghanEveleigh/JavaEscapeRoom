package nz.ac.auckland.se206.game;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.ProgressBar;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import nz.ac.auckland.se206.listeners.SuspicionListener;

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

  private void updateSuspicion() {
    if (touched) {
      incrementTask();
    } else {
      decrementTask();
    }
  }

  private void decrementTask() {
    suspicionLevel -= 0.1;
    if (suspicionLevel <= 0.0) {
      suspicionLevel = 0.0;
      timeline.pause();
      progressBar.toBack();
    }
    progressBar.setProgress((1.0 / maximumSuspicionLevel) * suspicionLevel);
  }

  private void incrementTask() {
    flashLight();
    suspicionLevel += 0.1;
    if (suspicionLevel >= maximumSuspicionLevel) {
      suspicionLevel = maximumSuspicionLevel;
      suspicionLevel = 0.0;
      listener.suspicionReached();
    }
    progressBar.setProgress((1.0 / maximumSuspicionLevel) * suspicionLevel);
  }

  protected void flashLight() {
    checkOpacity();
    if (maximumOpacityReached) {
      suspicionLight.setOpacity(suspicionLight.getOpacity() - 0.1);
    } else {
      suspicionLight.setOpacity(suspicionLight.getOpacity() + 0.1);
    }
  }

  private void checkOpacity() {
    double opacity = suspicionLight.getOpacity();
    if (opacity >= maximumOpacity) {
      maximumOpacityReached = true;
    } else if (opacity <= minimumOpactiy) {
      maximumOpacityReached = false;
    }
  }

  @Override
  public void interact() {}

  @Override
  public void touched() {
    if (touched) {
      return;
    }
    timeline.play();
    listener.suspicionTouched();
    touched = true;
  }

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
