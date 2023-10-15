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

  public Suspicion(Rectangle rectangle, SuspicionListener listener, ProgressBar progressBar) {
    super(rectangle);
    this.listener = listener;
    this.progressBar = progressBar;
    timer = new Timer();
    decrementTask = getDecrementTask();
    timer.scheduleAtFixedRate(decrementTask, 0, 100);
  }

  private TimerTask getIncrementTask() {
    return new TimerTask() {
      @Override
      public void run() {
        suspicionLevel += 0.1;
        if (suspicionLevel >= maximumSuspicionLevel) {
          suspicionLevel = maximumSuspicionLevel;
          incrementTask.cancel();
        }
        progressBar.setProgress((1.0 / maximumSuspicionLevel) * suspicionLevel);
      }
    };
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
        System.out.println("decrementing " + suspicionLevel);
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
    incrementTask.cancel();
    decrementTask = getDecrementTask();
    timer.scheduleAtFixedRate(decrementTask, 0, 100);
    touched = false;
  }
}
