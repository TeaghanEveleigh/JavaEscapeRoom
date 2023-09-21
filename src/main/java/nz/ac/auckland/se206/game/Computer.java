package nz.ac.auckland.se206.game;

import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.ComputerListener;

public class Computer extends Interactable {

  private ComputerListener listener;

  public Computer(Rectangle rectangle, ComputerListener listener) {
    super(rectangle);
    this.listener = listener;
  }

  @Override
  public void interact() {
    listener.computerInteracted();
  }

  @Override
  public void touched() {
    if (touched) return;
    listener.computerTouched();
    touched = true;
  }

  @Override
  public void untouched() {
    if (!touched) return;
    listener.computerUntouched();
    touched = false;
  }
}
