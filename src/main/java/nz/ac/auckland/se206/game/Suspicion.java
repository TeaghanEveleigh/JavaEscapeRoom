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
  private TimerTask currentTask;
  private double suspicionLevel = 0.0;
  private double maximumSuspicionLevel = 5.0;

  public Suspicion(Rectangle rectangle, SuspicionListener listener, ProgressBar progressBar) {
    super(rectangle);
    initialiseIncrementTask();
    initialiseDecrementTask();
    this.listener = listener;
    timer = new Timer();
  }

  private void initialiseIncrementTask() {
    incrementTask =
        new TimerTask() {
          @Override
          public void run() {
            suspicionLevel += 0.1;
          }
        };
  }

  private void initialiseDecrementTask() {
    decrementTask =
        new TimerTask() {
          @Override
          public void run() {
            suspicionLevel -= 0.1;
          }
        };
  }

  @Override
  public void interact() {
    return;
  }

  @Override
  public void touched() {
    if (currentTask == decrementTask) {
      currentTask.cancel();
      currentTask = incrementTask;
      timer.scheduleAtFixedRate(currentTask, 0, 100);
    }
  }

  @Override
  public void notTouched() {
    if (currentTask == incrementTask) {
      currentTask.cancel();
      currentTask = decrementTask;
      timer.scheduleAtFixedRate(currentTask, 0, 100);
    }
  }
}
