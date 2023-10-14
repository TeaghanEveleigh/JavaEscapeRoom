package nz.ac.auckland.se206.game;

import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.listeners.ObjectListener;

public class Object extends Interactable {

  private ObjectListener listener;

  public Object(Rectangle rectangle, ObjectListener listener) {
    super(rectangle);
    this.listener = listener;
  }

  @Override
  public void interact() {
    listener.objectInteracted();
  }

  @Override
  public void touched() {
    if (touched) return;
    listener.objectTouched();
    touched = true;
  }

  @Override
  public void notTouched() {
    if (!touched) return;
    listener.objectUntouched();
    touched = false;
  }
}
