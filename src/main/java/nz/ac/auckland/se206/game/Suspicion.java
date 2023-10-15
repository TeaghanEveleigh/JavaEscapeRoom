package nz.ac.auckland.se206.game;

import java.util.Timer;
import java.util.TimerTask;
import javafx.scene.control.ProgressBar;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.listeners.SuspicionListener;

public class Suspicion extends Interactable {

  private SuspicionListener listener;
  private Timer timer;
  private TimerTask incrementTask;
  private TimerTask decrementTask;
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
    timer = new Timer();
    decrementTask = getDecrementTask();
    timer.scheduleAtFixedRate(decrementTask, 0, 100);
  }

  private TimerTask getIncrementTask() {
    return new TimerTask() {
      @Override
      public void run() {
        flashLight();
        suspicionLevel += 0.1;
        if (suspicionLevel >= maximumSuspicionLevel) {
          suspicionLevel = maximumSuspicionLevel;
          incrementTask.cancel();
        }
        progressBar.setProgress((1.0 / maximumSuspicionLevel) * suspicionLevel);
      }
    };
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

  private TimerTask getDecrementTask() {
    return new TimerTask() {
      @Override
      public void run() {
        suspicionLevel -= 0.1;
        if (suspicionLevel <= 0.0) {
          suspicionLevel = 0.0;
          decrementTask.cancel();
        }
        progressBar.setProgress((1.0 / maximumSuspicionLevel) * suspicionLevel);
      }
    };
  }

  @Override
  public void interact() {
    return;
  }

  @Override
  public void touched() {
    if (touched) return;
    decrementTask.cancel();
    incrementTask = getIncrementTask();
    timer.scheduleAtFixedRate(incrementTask, 0, 100);
    touched = true;
  }

  @Override
  public void notTouched() {
    if (!touched) return;
    suspicionLight.setOpacity(0.0);
    incrementTask.cancel();
    decrementTask = getDecrementTask();
    timer.scheduleAtFixedRate(decrementTask, 0, 100);
    touched = false;
  }
}
