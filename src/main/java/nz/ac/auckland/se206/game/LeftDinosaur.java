package nz.ac.auckland.se206.game;

import javafx.scene.shape.Rectangle;
import nz.ac.auckland.se206.listeners.LeftDinosaurListener;

public class LeftDinosaur extends Interactable {

  private LeftDinosaurListener listener;

  public LeftDinosaur(Rectangle rectangle, LeftDinosaurListener listener) {
    super(rectangle);
    this.listener = listener;
  }

  @Override
  public void interact() {
    listener.leftDinosaurInteracted();
  }

  @Override
  public void touched() {
    if (touched) return;
    listener.leftDinosaurTouched();
    touched = true;
  }

  @Override
  public void notTouched() {
    if (!touched) return;
    listener.leftDinosaurUntouched();
    touched = false;
  }
}
