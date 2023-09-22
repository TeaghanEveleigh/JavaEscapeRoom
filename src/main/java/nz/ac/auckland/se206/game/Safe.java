package nz.ac.auckland.se206.game;

import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.SafeListener;

public class Safe extends Interactable {

  private SafeListener listener;

  public Safe(Rectangle rectangle, SafeListener listener) {
    super(rectangle);
    this.listener = listener;
  }

  @Override
  public void interact() {
    listener.safeInteracted();
  }

  @Override
  public void touched() {
    if (touched) return;
    listener.safeTouched();
    touched = true;
  }

  @Override
  public void notTouched() {
    if (!touched) return;
    listener.safeNotTouched();
    touched = false;
  }
}
