package nz.ac.auckland.se206.game;

import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.listeners.SuspicionListener;

public class Suspicion extends Interactable {

  private SuspicionListener listener;

  public Suspicion(Rectangle rectangle, SuspicionListener listener) {
    super(rectangle);
    this.listener = listener;
  }

  @Override
  public void interact() {
    return;
  }

  @Override
  public void touched() {
    listener.suspicionTouched();
  }

  @Override
  public void notTouched() {
    listener.suspicionUntouched();
  }
}
