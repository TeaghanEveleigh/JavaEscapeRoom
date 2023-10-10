package nz.ac.auckland.se206.game;

import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.listeners.KeypadListener;

public class Keypad extends Interactable {

  private KeypadListener listener;

  public Keypad(Rectangle rectangle, KeypadListener listener) {
    super(rectangle);
    this.listener = listener;
  }

  @Override
  public void interact() {
    System.out.println("interact");
    listener.keypadInteracted();
  }

  @Override
  public void touched() {
    if (touched) return;
    listener.keypadTouched();
    touched = true;
  }

  @Override
  public void notTouched() {
    if (!touched) return;
    listener.keypadNotTouched();
    touched = false;
  }
}
